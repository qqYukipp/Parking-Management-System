package com.cgr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgr.dto.PasswordUpdateDTO;
import com.cgr.entity.CPUser;
import com.cgr.mapper.UserMapper;
import com.cgr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, CPUser> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        CPUser user = userMapper.selectById(passwordUpdateDTO.getId());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String rawOldPassword = passwordUpdateDTO.getPassword();      // 用户输入的旧密码（明文）
        String rawNewPassword = passwordUpdateDTO.getNewPassword();   // 用户输入的新密码（明文）

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 验证旧密码是否正确
        if (!bCryptPasswordEncoder.matches(rawOldPassword, user.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        // 对新密码进行加密
        String encodedNewPassword = bCryptPasswordEncoder.encode(rawNewPassword);

        // 更新用户密码
        user.setPassword(encodedNewPassword);
        userMapper.updateById(user);
    }
}
