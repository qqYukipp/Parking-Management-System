package com.cgr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgr.entity.CPMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<CPMenu> {
    List<Long> selectMenuIdsByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<CPMenu> selectMenusByMenuIds(@Param("menuIds") List<Long> menuIds);
}
