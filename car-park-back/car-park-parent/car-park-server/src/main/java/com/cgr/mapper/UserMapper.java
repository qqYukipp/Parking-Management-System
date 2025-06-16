package com.cgr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgr.entity.CPUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<CPUser> {

    void deleteBatch(@Param("userIds") List<Long> ids);

    List<CPUser> findCommonUser(CPUser user);

    int countByUsername(String username);
}
