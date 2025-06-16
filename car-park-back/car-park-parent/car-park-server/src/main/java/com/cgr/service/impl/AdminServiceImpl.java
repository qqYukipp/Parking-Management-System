package com.cgr.service.impl;

import com.cgr.constant.Role;
import com.cgr.entity.CPUser;
import com.cgr.mapper.*;
import com.cgr.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ParkingMapper parkingMapper;
    @Autowired
    private PayMapper payMapper;
    @Autowired
    private VehicleMapper vehicleMapper;

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(CPUser user) {
        //  添加用户
        userMapper.insert(user);

        //为用户赋予管理员权限
        Long userId = user.getId();
        Long roleId = Role.ROLE_ADMIN_ID;
        roleMapper.insertUserRole(userId, roleId);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        //  删除用户
        userMapper.deleteById(id);
        //  删除user_role中的记录
        roleMapper.deleteByUserId(id);

        // 删除与用户绑定的车辆
        vehicleMapper.deleteByUserId(id);
        // 删除与用户绑定的缴费订单
        payMapper.deleteByUserId(id);
        // 删除与用户绑定的停车订单
        parkingMapper.deleteByUserId(id);
    }

    /**
     * 批量删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        userMapper.deleteBatch(ids);
        roleMapper.deleteBatch(ids);
        vehicleMapper.deleteBatch(ids);
        payMapper.deleteBatch(ids);
        parkingMapper.deleteBatch(ids);
    }

    /**
     * 修改
     */
    public void updateById(CPUser user) {
        userMapper.updateById(user);
    }

    /**
     * 根据ID查询
     */
    public CPUser selectById(Long id) {

        return userMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<CPUser> selectAll(CPUser user) {
        return adminMapper.selectAll(user);
    }

    /**
     * 分页查询
     */
    public PageInfo<CPUser> selectPage(CPUser user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CPUser> list = adminMapper.selectAll(user);
        return PageInfo.of(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void liftToAdmin(List<Long> ids) {
        //将用户的原始角色去除
        roleMapper.deleteBatch(ids);

        //为用户赋予管理员权限
        roleMapper.insertUserRoleBatch(Role.ROLE_ADMIN_ID, ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downToUser(List<Long> ids) {
        //将用户的原始角色去除
        roleMapper.deleteBatch(ids);

        //为用户赋予普通用户权限
        roleMapper.insertUserRoleBatch(Role.ROLE_USER_ID, ids);
    }
}
