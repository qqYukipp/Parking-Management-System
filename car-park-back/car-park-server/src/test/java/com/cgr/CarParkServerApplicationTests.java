package com.cgr;

import com.cgr.entity.CPMenu;
import com.cgr.mapper.MenuMapper;
import com.cgr.mapper.RoleMapper;
import com.cgr.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CarParkServerApplicationTests {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuMapper  menuMapper;
    @Autowired
    private RoleMapper  roleMapper;

    @Test
    void contextLoads() {
        //List<Long> roleIds = roleMapper.selectRoleIdsByUserId(1L);
        //List<Long> menuIds = menuMapper.selectMenuIdsByRoleIds(roleIds);

        List<Long> menuIds = Arrays.asList(1L,2L,3L,6L);
        List<CPMenu> menuList = menuMapper.selectMenusByMenuIds(menuIds);

        List<CPMenu> menuTree = menuService.buildMenuTree(menuList);

        System.out.println(menuTree);
    }
}
