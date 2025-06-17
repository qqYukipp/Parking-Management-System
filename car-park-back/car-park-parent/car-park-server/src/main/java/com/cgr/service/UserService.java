package com.cgr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgr.dto.PasswordUpdateDTO;
import com.cgr.entity.CPUser;

public interface UserService extends IService<CPUser> {
    void updatePassword(PasswordUpdateDTO passwordUpdateDTO);

}
