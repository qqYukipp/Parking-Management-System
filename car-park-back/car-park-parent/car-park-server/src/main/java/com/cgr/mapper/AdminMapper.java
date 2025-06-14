package com.cgr.mapper;

import com.cgr.entity.CPUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 操作admin相关数据接口
 */
@Mapper
public interface AdminMapper {

    /**
     * 新增
     */
    int insert(CPUser user);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(CPUser user);

    /**
     * 根据ID查询
     */
    CPUser selectById(Integer id);

    /**
     * 查询所有
     */
    List<CPUser> selectAll(CPUser user);

    @Select("select * from `cp_user` where username = #{username}")
    CPUser selectByUsername(String username);

}


