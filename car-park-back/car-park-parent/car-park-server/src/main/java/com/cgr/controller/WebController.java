package com.cgr.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cgr.ResponseModel;
import com.cgr.dto.LoginBody;
import com.cgr.entity.CPMenu;
import com.cgr.entity.LoginUser;
import com.cgr.service.MenuService;
import com.cgr.service.WebService;
import com.cgr.utils.RedisUtil;
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
    @Autowired
    private RedisUtil redisUtil;


    @PostMapping("/login")
    public ResponseModel login(@RequestBody LoginBody loginBody){
        return webService.login(loginBody);
    }

    @PostMapping("/register")
    public ResponseModel register(@RequestBody LoginBody loginBody){
        return webService.addUser(loginBody);
    }

    @GetMapping("/getRouters")
    public ResponseModel getRouters() {

        LoginUser loginUser = SecurityUtil.getLoginUser();
        Long userId = loginUser.getUser().getId();

        //查询redis是否有缓存
        String menuTreeJson = redisUtil.getMenuTreeJson(userId);
        if(!StrUtil.isBlank(menuTreeJson)){
            List<CPMenu> menuTree = JSONUtil.toList(menuTreeJson, CPMenu.class);
            return ResponseModel.success(menuTree);
        }

        //缓存未命中，从数据库中查询
        List<CPMenu> menus = menuService.selectMenuTreeByUserId(userId);

        //存入redis
        redisUtil.cacheMenuTree(JSONUtil.toJsonStr(menus), userId);
        return ResponseModel.success(menus);
    }
}
