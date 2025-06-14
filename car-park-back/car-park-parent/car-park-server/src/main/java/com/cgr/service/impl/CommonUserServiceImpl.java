package com.cgr.service.impl;

import com.cgr.constant.Role;
import com.cgr.entity.CPUser;
import com.cgr.mapper.RoleMapper;
import com.cgr.mapper.UserMapper;
import com.cgr.service.CommonUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommonUserServiceImpl implements CommonUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CPUser user) {
        userMapper.insert(user);

        Long userId = user.getId();
        Long roleId = Role.ROLE_USER_ID;

        roleMapper.insertUserRole(roleId,userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        userMapper.deleteById(id);
        roleMapper.deleteByUserId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        userMapper.deleteBatch(ids);
        roleMapper.deleteBatch(ids);
    }

    @Override
    public void updateById(CPUser user) {
        userMapper.updateById(user);
    }

    @Override
    public CPUser selectById(Long id) {
        CPUser user = userMapper.selectById(id);
        return user;
    }

    /**
     * 查询所有普通用户
     * @param user
     * @return
     */
    @Override
    public List<CPUser> selectAll(CPUser user) {
        return userMapper.findCommonUser(user);
    }

    @Override
    public PageInfo<CPUser> selectPage(CPUser user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CPUser> list = this.selectAll(user);
        return PageInfo.of(list);
    }
}
