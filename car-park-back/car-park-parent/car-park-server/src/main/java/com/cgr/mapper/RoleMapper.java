package com.cgr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgr.entity.CPRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<CPRole> {
    List<String> selectRoleByUserId(Long id);

    List<Long> selectRoleIdsByUserId(Long id);

    void insertUserRole(Long roleId, Long userId);

    void deleteByUserId(Long userId);

    void deleteBatch(@Param("userIds") List<Long> userIds);

    void insertUserRoleBatch(
            @Param("roleId") Long roleId,
            @Param("userIds") List<Long> userIds
    );

}
