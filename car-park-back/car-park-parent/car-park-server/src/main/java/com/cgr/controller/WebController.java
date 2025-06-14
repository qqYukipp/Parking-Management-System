package com.cgr.controller;

import com.cgr.ResponseModel;
import com.cgr.dto.LoginBody;
import com.cgr.entity.CPMenu;
import com.cgr.entity.LoginUser;
import com.cgr.service.MenuService;
import com.cgr.service.WebService;
import com.cgr.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebController {


    @Autowired
    private WebService webService;
    @Autowired
    private MenuService menuService;


    @PostMapping("/login")
    public ResponseModel login(@RequestBody LoginBody loginBody){
        return webService.login(loginBody);
    }

    @PostMapping("/register")
    public ResponseModel register(@RequestBody LoginBody loginBody){
        return webService.addUser(loginBody);
    }

    @GetMapping("/getRouters")
    public ResponseModel getRouters()
    {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        Long userId = loginUser.getUser().getId();
        List<CPMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return ResponseModel.success(menus);
    }
}
