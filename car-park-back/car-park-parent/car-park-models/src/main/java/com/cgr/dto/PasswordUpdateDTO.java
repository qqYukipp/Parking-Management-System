package com.cgr.dto;

import lombok.Data;

@Data
public class PasswordUpdateDTO {
    private Long id;           // 用户ID
    private String password;   // 旧密码
    private String newPassword;// 新密码
}
