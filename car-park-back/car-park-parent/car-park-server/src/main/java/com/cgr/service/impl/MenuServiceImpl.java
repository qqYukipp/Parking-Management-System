package com.cgr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgr.entity.CPMenu;
import com.cgr.mapper.MenuMapper;
import com.cgr.mapper.RoleMapper;
import com.cgr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, CPMenu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 将菜单数据构建成树形结构
     * @param menus
     * @return
     */
    @Override
    public List<CPMenu> buildMenuTree(List<CPMenu> menus) {
        //找出所有顶级菜单
        List<CPMenu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId() == -1)
                .collect(Collectors.toList());

        menuTree.forEach(menu -> {
            findChildren(menu, menus);
        });

        return menuTree;
    }

    private void findChildren(CPMenu menu, List<CPMenu> menus) {
        menus.forEach(m-> {
            if(m.getParentId().equals(menu.getId())){
                menu.getChildren().add(m);

                findChildren(m, menus);
            }
        });

    }

    @Override
    public List<CPMenu> selectMenuTreeByUserId(Long userId) {
        List<Long> roleIds = roleMapper.selectRoleIdsByUserId(userId);

        List<Long> menuIds = menuMapper.selectMenuIdsByRoleIds(roleIds);

        List<CPMenu> menuList = menuMapper.selectMenusByMenuIds(menuIds);

        return buildMenuTree(menuList);
    }

    private List<CPMenu> getMenuListByUserId(Long userId) {
        return null;
    }
}
