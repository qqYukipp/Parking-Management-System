package com.cgr.service.impl;

import com.cgr.constant.Role;
import com.cgr.entity.LoginUser;
import com.cgr.entity.Vehicle;
import com.cgr.mapper.VehicleMapper;
import com.cgr.service.VehicleService;
import com.cgr.utils.SecurityUtil;
import jakarta.annotation.Resource;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Resource
    private VehicleMapper vehicleMapper;

    /**
     * 新增
     */
    public void add(Vehicle vehicle) {
        vehicleMapper.insert(vehicle);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        vehicleMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
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
    public Vehicle selectById(Integer id) {
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
            if (userId != null && userId >= Integer.MIN_VALUE && userId <= Integer.MAX_VALUE) {
                vehicle.setId(userId.intValue());
            } else {
                throw new IllegalArgumentException("userId 超出 Integer 范围！");
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Vehicle> list = this.selectAll(vehicle);
        return PageInfo.of(list);
    }
}
