package com.cgr.mapper;


import com.cgr.entity.Pay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作pay相关数据接口
*/
@Mapper
public interface PayMapper {

    /**
      * 新增
    */
    int insert(Pay pay);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Pay pay);

    /**
      * 根据ID查询
    */
    Pay selectById(Integer id);

    /**
      * 查询所有
    */
    List<Pay> selectAll(Pay pay);

}