package com.cgr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("cp_menu")
public class CPMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String menuName;        // 菜单名称
    private String path;            // 路由路径
    private String component;       // 组件路径
    private Long parentId;          // 父菜单ID
    private String menuType;        // 菜单类型：CATALOG-目录 MENU-菜单 BUTTON-按钮
    private String permission;      // 权限标识
    private Boolean visible;        // 是否显示
    /** 菜单图标 */
    private String icon;

    @TableField(exist = false)
    List<CPMenu> children = new ArrayList<>();
}