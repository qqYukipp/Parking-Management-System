package com.cgr.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.cgr.constant.Role;
import com.cgr.entity.LoginUser;
import com.cgr.entity.Parking;
import com.cgr.entity.ParkingLot;
import com.cgr.entity.Pay;
import com.cgr.exception.CustomException;
import com.cgr.mapper.ParkingMapper;
import com.cgr.service.ParkingLotService;
import com.cgr.service.ParkingService;
import com.cgr.service.PayService;
import com.cgr.utils.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingServiceImpl implements ParkingService {
    @Resource
    private ParkingMapper parkingMapper;
    @Resource
    private ParkingLotService parkingLotService;
    @Resource
    private PayService payService;

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
    public void updateById(Parking parking) {
        if (ObjUtil.isEmpty(parking.getEndTime())) {
            throw new CustomException(500, "请选择出场时间");
        }
        parking.setStatus("已出场");
        parkingMapper.updateById(parking);
        // 初始化缴费
        Pay pay = new Pay();
        BeanUtils.copyProperties(parking, pay);
        Long minutes = DateUtil.between(DateUtil.parse(pay.getStartTime()), DateUtil.parse(pay.getEndTime()), DateUnit.MINUTE);
        pay.setMinutes(minutes.intValue());
        pay.setPrice(0.1 * pay.getMinutes());
        pay.setStatus("未缴费");
        payService.add(pay);
        // 更新停车位状态
        ParkingLot parkingLot = parkingLotService.selectById(parking.getParkingLotId());
        if (ObjUtil.isNotEmpty(parkingLot)) {
            parkingLot.setStatus("空闲");
            parkingLotService.updateById(parkingLot);
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
        LoginUser  currentUser = SecurityUtil.getLoginUser();
        List<String> roleList = currentUser.getRoleList();
        if (!roleList.contains(Role.ROLE_ADMIN)) {
            Long userId = currentUser.getUser().getId();
            if (userId != null && userId >= Integer.MIN_VALUE && userId <= Integer.MAX_VALUE) {
                parking.setId(userId.intValue());
            } else {
                throw new IllegalArgumentException("userId 超出 Integer 范围！");
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Parking> list = this.selectAll(parking);
        return PageInfo.of(list);
    }
}
