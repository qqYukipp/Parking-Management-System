package com.cgr.mapper;


import com.cgr.entity.Location;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作location相关数据接口
*/
@Mapper
public interface LocationMapper {

    /**
      * 新增
    */
    int insert(Location location);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Location location);

    /**
      * 根据ID查询
    */
    Location selectById(Integer id);

    /**
      * 查询所有
    */
    List<Location> selectAll(Location location);

    void freeParkingLot(Integer locationId);
}