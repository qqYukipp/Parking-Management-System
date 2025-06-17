package com.cgr.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.cgr.constant.Role;
import com.cgr.constant.VehicleConstant;
import com.cgr.entity.LoginUser;
import com.cgr.entity.Parking;
import com.cgr.entity.ParkingLot;
import com.cgr.entity.Pay;
import com.cgr.exception.CustomException;
import com.cgr.mapper.LocationMapper;
import com.cgr.mapper.ParkingMapper;
import com.cgr.mapper.VehicleMapper;
import com.cgr.service.ParkingLotService;
import com.cgr.service.ParkingService;
import com.cgr.service.PayService;
import com.cgr.utils.RedisUtil;
import com.cgr.utils.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ParkingServiceImpl implements ParkingService {
    @Resource
    private ParkingMapper parkingMapper;
    @Resource
    private ParkingLotService parkingLotService;
    @Resource
    private PayService payService;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private LocationMapper locationMapper;

    /**
     * 新增
     */
    public void add(Parking parking) {
        // 查询当前用户当前车辆是否已经在停车场了
        Parking parking1 = new Parking();
        parking1.setUserId(parking1.getUserId());
        parking1.setVehicleId(parking.getVehicleId());
        parking1.setStatus("已入场");
        List<Parking> parkings = parkingMapper.selectAll(parking1);
        if (CollectionUtil.isNotEmpty(parkings)) {
            throw new CustomException(500, "当前车辆还未驶出停车场");
        }
        // 查询当前用户当前车辆有没有未交费的记录
        Pay pay = new Pay();
        pay.setStatus("未缴费");
        pay.setUserId(parking1.getUserId());
        pay.setVehicleId(parking.getVehicleId());
        List<Pay> pays = payService.selectAll(pay);
        if (CollectionUtil.isNotEmpty(pays)) {
            throw new CustomException(500, "当前车辆还有未缴费的停车记录，禁止入场");
        }
        parking.setStatus("已入场");
        parkingMapper.insert(parking);
        // 更新停车位状态
        ParkingLot parkingLot = parkingLotService.selectById(parking.getParkingLotId());
        if (ObjUtil.isNotEmpty(parkingLot)) {
            parkingLot.setStatus("占用");
            parkingLotService.updateById(parkingLot);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        parkingMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            parkingMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateById(Parking parking) {
        if (ObjUtil.isEmpty(parking.getEndTime())) {
            throw new CustomException(500, "请选择出场时间");
        }

        //查询车辆类型
        int type = vehicleMapper.selectTypeById(parking.getVehicleId());

        //入场出场时间
        Long vehicleId = parking.getVehicleId();
        String startTimeStr = parking.getStartTime();
        String endTimeStr = parking.getEndTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        LocalDateTime now = LocalDateTime.now();

        if (type == VehicleConstant.TYPE_INTERNAL) {
            //内部车
            initPay(parking, 0.0);
            updateParkingLotStatus(parking);

        } else if (type == VehicleConstant.TYPE_BLACKLIST) {
            throw new RuntimeException("黑名单车辆无法出场");
        } else if (type == VehicleConstant.TYPE_MONTHLY) {
            //判断是否处在月租时间内
            LocalDateTime monthStartTime = vehicleMapper.selectById(vehicleId).getStartTime();
            LocalDateTime monthEndTime = vehicleMapper.selectById(vehicleId).getEndTime();
            if (now.isBefore(monthStartTime) || now.isAfter(monthEndTime)) {
                //类型改为临时车
                vehicleMapper.updateTypeTempById(vehicleId);
                throw new CustomException(500, "月租已到期");
            }

            //处在时间内
            initPay(parking, 0.0);
            updateParkingLotStatus(parking);

        } else {
            // 临时车
            Double FIRST_HOUR = redisUtil.getCharge().getFirstHour();
            Double PRE_HOUR = redisUtil.getCharge().getPerHour();
            Double DAILY_CAP = redisUtil.getCharge().getDailyCap();
            Integer FREE_MINUTES = redisUtil.getCharge().getFreeMinutes();

            Double price = 0.0;
            Duration duration = Duration.between(startTime, endTime);
            long totalMinutes = duration.toMinutes();

            // 免费时间内
            if (totalMinutes <= FREE_MINUTES) {
                initPay(parking, price);
                updateParkingLotStatus(parking);
                return;
            }
            // 首小时内
            if(totalMinutes>FREE_MINUTES && totalMinutes<= 60){
                initPay(parking, FIRST_HOUR);
                updateParkingLotStatus(parking);
                return;
            }

            // 超过首小时

            long totalDay = totalMinutes / 1440;    // 60 * 24

            //不足一天
            if(totalDay == 0 ){
                price = totalMinutes / 60 * PRE_HOUR >= DAILY_CAP - FIRST_HOUR ? DAILY_CAP : totalMinutes / 60 * PRE_HOUR + FIRST_HOUR - PRE_HOUR;
                initPay(parking, price);
                updateParkingLotStatus(parking);
                return;
            }

            // 超过一天
            if(totalDay > 0){
                price += totalDay * DAILY_CAP;
            }

            // 最后的时间
            long restMinutes = totalMinutes % 1440;
            if(restMinutes == 0){
                //刚刚好是天数的整数倍
                initPay(parking, price);
                updateParkingLotStatus(parking);
                return;
            }

            long restHour = restMinutes / 60;
            price += (restHour + 1) * PRE_HOUR >= DAILY_CAP ? DAILY_CAP : (restHour + 1) * PRE_HOUR;

            initPay(parking, price);
            updateParkingLotStatus(parking);
        }

    }

    /**
     * 根据ID查询
     */
    public Parking selectById(Integer id) {
        return parkingMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Parking> selectAll(Parking parking) {
        return parkingMapper.selectAll(parking);
    }

    /**
     * 分页查询
     */
    public PageInfo<Parking> selectPage(Parking parking, Integer pageNum, Integer pageSize) {

        /*Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            parking.setUserId(currentUser.getId());
        }*/
        //如果不是管理员，设置id，查询当前用户相关信息
        LoginUser currentUser = SecurityUtil.getLoginUser();
        List<String> roleList = currentUser.getRoleList();
        if (!roleList.contains(Role.ROLE_ADMIN)) {
            Long userId = currentUser.getUser().getId();
            /*if (userId != null && userId >= Integer.MIN_VALUE && userId <= Integer.MAX_VALUE) {
                parking.setUserId(userId);
            } else {
                throw new IllegalArgumentException("userId 超出 Integer 范围！");
            }*/
            parking.setUserId(userId);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Parking> list = this.selectAll(parking);
        return PageInfo.of(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void initPay(Parking parking, Double price) {
        parking.setStatus("已出场");
        parkingMapper.updateById(parking);
        Pay pay = new Pay();
        BeanUtils.copyProperties(parking, pay);
        Long minutes = DateUtil.between(DateUtil.parse(pay.getStartTime()), DateUtil.parse(pay.getEndTime()), DateUnit.MINUTE);
        pay.setMinutes(minutes.intValue());
        pay.setPrice(price);
        pay.setStatus("已缴费");
        payService.add(pay);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateParkingLotStatus(Parking parking) {
        // 更新停车位状态
        ParkingLot parkingLot = parkingLotService.selectById(parking.getParkingLotId());
        if (ObjUtil.isNotEmpty(parkingLot)) {
            parkingLot.setStatus("空闲");
            parkingLotService.updateById(parkingLot);
            locationMapper.freeParkingLot(parking.getLocationId());
        }
    }
}
