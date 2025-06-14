package com.cgr.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVo implements Serializable {

    private Long id;

    private String username;

    private String nickName;

    private String avatar;

    private String email;

    private String phone;

    private String sex;

    /**
     * 角色列表
     */
    private List<String> roleList;

    private String token;

    private Double account;
}
