package com.cgr.mapper;

import com.cgr.entity.Parking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParkingMapper {

    /**
     * 新增
     */
    int insert(Parking parking);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Parking parking);

    /**
     * 根据ID查询
     */
    Parking selectById(Integer id);

    /**
     * 查询所有
     */
    List<Parking> selectAll(Parking parking);

    void deleteByUserId(Long id);

    void deleteBatch(@Param("userIds") List<Long> userIds);
}
