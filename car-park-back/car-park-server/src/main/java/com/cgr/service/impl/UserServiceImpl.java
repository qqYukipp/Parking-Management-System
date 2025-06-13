package com.cgr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgr.entity.CPUser;
import com.cgr.mapper.UserMapper;
import com.cgr.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, CPUser> implements UserService {

}
