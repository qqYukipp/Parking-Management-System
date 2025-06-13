package com.cgr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody {

    private String username;

    private String password;

    private String email;

    private String sex;

    private String nickName;

    private String avatar;

    private String phone;

    //  验证码
    private String code;
}
