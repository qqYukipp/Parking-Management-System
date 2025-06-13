package com.cgr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cgr.entity.CPUser;
import com.cgr.entity.LoginUser;
import com.cgr.mapper.RoleMapper;
import com.cgr.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CPUser user = userMapper.selectOne(
                new QueryWrapper<CPUser>()
                .eq("username", username)
        );

        if(ObjectUtils.isEmpty(user)){
            log.error("用户不存在");
            throw new UsernameNotFoundException(username +"不存在");
        }

        List<String> roleList = roleMapper.selectRoleByUserId(user.getId());
        LoginUser loginUser = new LoginUser(user, roleList);
        return loginUser;
    }

    private UserDetails createLoginUser(CPUser user) {
        List<String> roleList = roleMapper.selectRoleByUserId(user.getId());
        return new LoginUser(user,roleList);
    }
}
