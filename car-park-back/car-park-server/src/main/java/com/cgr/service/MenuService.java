package com.cgr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgr.entity.CPMenu;

import java.util.List;

public interface MenuService extends IService<CPMenu> {

    List<CPMenu> buildMenuTree(List<CPMenu> menuList);

    List<CPMenu> selectMenuTreeByUserId(Long userId);


}
