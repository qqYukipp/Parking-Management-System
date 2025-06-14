package com.cgr.service.impl;


import com.cgr.ResponseModel;
import com.cgr.constant.Constants;
import com.cgr.dto.LoginBody;
import com.cgr.entity.CPUser;
import com.cgr.entity.LoginUser;
import com.cgr.mapper.RoleMapper;
import com.cgr.mapper.UserMapper;
import com.cgr.service.WebService;
import com.cgr.utils.JwtUtil;
import com.cgr.utils.RedisUtil;
import com.cgr.utils.SecurityUtil;
import com.cgr.vo.LoginUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cgr.constant.Role.ROLE_USER;
import static com.cgr.constant.Role.ROLE_USER_ID;


@Service
public class WebServiceImpl implements WebService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper  roleMapper;

    @Override
    public ResponseModel login(LoginBody loginBody) {
        //封装到待认证对象
        UsernamePasswordAuthenticationToken unauthenticate =
                SecurityUtil.tokenUnauthenticate(loginBody.getUsername(), loginBody.getPassword());

        //这里会调用loadUserByUsername方法及密码加密
        Authentication authenticate = authenticationManager.authenticate(unauthenticate);
        //登录成功
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        // 生成token
        CPUser user = loginUser.getUser();
        String token = JwtUtil.generateToken(user);

        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        loginUserVo.setToken(token);
        loginUserVo.setRoleList(loginUser.getRoleList());

        //存入Secuirty上下文
        SecurityUtil.setAuthentication(authenticate);

        //存入redis
        String key = Constants.LOGIN_USER_KEY + user.getId();
        redisUtil.setCacheObject(key, loginUserVo, Constants.USER_INFO_TTL, TimeUnit.MINUTES);

        return ResponseModel.success(loginUserVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel addUser(LoginBody loginBody) {
        loginBody.setPassword(new BCryptPasswordEncoder().encode(loginBody.getPassword()));
        CPUser user = new CPUser();
        BeanUtils.copyProperties(loginBody, user);
        //添加用户
        userMapper.insert(user);

        Long userId = user.getId();

        //添加角色
        roleMapper.insertUserRole(ROLE_USER_ID,userId);

        //生成token
        String token = JwtUtil.generateToken(user);

        LoginUserVo loginUserVo = new LoginUserVo();

        BeanUtils.copyProperties(user, loginUserVo);
        loginUserVo.setToken(token);

        List<String> roleList = Arrays.asList(ROLE_USER);
        loginUserVo.setRoleList(roleList);

        loginUserVo.setAccount(0.00);

        //存入Secuirty上下文
        UsernamePasswordAuthenticationToken authenticate =
                SecurityUtil.tokenAuthenticate(
                        SecurityUtil.getLoginUserFromLoginUserVo(loginUserVo)
                );
        SecurityUtil.setAuthentication(authenticate);

        //存入redis
        String key = Constants.LOGIN_USER_KEY + user.getId();
        redisUtil.setCacheObject(key, loginUserVo, Constants.USER_INFO_TTL, TimeUnit.MINUTES);

        return ResponseModel.success(loginUserVo);
    }

}
