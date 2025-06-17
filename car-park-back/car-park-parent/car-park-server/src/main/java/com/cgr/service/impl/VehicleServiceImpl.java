package com.cgr.service.impl;

import com.cgr.constant.Role;
import com.cgr.constant.VehicleConstant;
import com.cgr.entity.CPUser;
import com.cgr.entity.LoginUser;
import com.cgr.entity.Vehicle;
import com.cgr.mapper.UserMapper;
import com.cgr.mapper.VehicleMapper;
import com.cgr.service.VehicleService;
import com.cgr.utils.RedisUtil;
import com.cgr.utils.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Resource
    private VehicleMapper vehicleMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;


    /**
     * 新增
     */
    public void add(Vehicle vehicle) {
        //查询数据库中是否已经存在
        Vehicle temp = vehicleMapper.selectByName(vehicle.getName());
        if(!ObjectUtils.isEmpty(temp)){
            throw new DuplicateKeyException("车辆已存在");
        }
        vehicle.setStartTime(null);
        vehicle.setEndTime(null);
        vehicle.setType(VehicleConstant.TYPE_TEMPORARY);
        vehicleMapper.insert(vehicle);
    }

    /**
     * 删除
     */
    public void deleteById(Long id) {
        vehicleMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            vehicleMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Vehicle vehicle) {
        vehicleMapper.updateById(vehicle);
    }

    /**
     * 根据ID查询
     */
    public Vehicle selectById(Long id) {
        return vehicleMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Vehicle> selectAll(Vehicle vehicle) {
        return vehicleMapper.selectAll(vehicle);
    }

    /**
     * 分页查询
     */
    public PageInfo<Vehicle> selectPage(Vehicle vehicle, Integer pageNum, Integer pageSize) {
        //如果不是管理员，设置id，查询当前用户相关信息
        LoginUser currentUser = SecurityUtil.getLoginUser();
        List<String> roleList = currentUser.getRoleList();
        if (!roleList.contains(Role.ROLE_ADMIN)) {
            Long userId = currentUser.getUser().getId();
            /*if (userId != null && userId >= Integer.MIN_VALUE && userId <= Integer.MAX_VALUE) {
                vehicle.setId(userId.intValue());
            } else {
                throw new IllegalArgumentException("userId 超出 Integer 范围！");
            }*/
            vehicle.setUserId(userId);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Vehicle> list = this.selectAll(vehicle);
        return PageInfo.of(list);
    }

    @Override
    public void updateTypeByIds(List<Long> ids,int carType) {
        vehicleMapper.updateTypeByIds(ids,carType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void monthlyCharge(Long vehicleId) {

        Double monthlyFee = redisUtil.getCharge().getMonthlyFee();
        Long userId = SecurityUtil.getLoginUser().getUser().getId();
        CPUser user = userMapper.selectById(userId);
        Double account = user.getAccount();
        if(account < monthlyFee){
            System.out.println(account);
            throw new RuntimeException("账户余额不足,请前往个人中心充值");
        }
        user.setAccount(account - monthlyFee);
        userMapper.updateById(user);
        vehicleMapper.monthlyCharge(vehicleId);
    }
}
