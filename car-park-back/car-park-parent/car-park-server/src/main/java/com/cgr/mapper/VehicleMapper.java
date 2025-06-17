package com.cgr.mapper;


import com.cgr.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作vehicle相关数据接口
*/
@Mapper
public interface VehicleMapper {

    /**
      * 新增
    */
    int insert(Vehicle vehicle);

    /**
      * 删除
    */
    int deleteById(Long id);

    /**
      * 修改
    */
    int updateById(Vehicle vehicle);

    /**
      * 根据ID查询
    */
    Vehicle selectById(Long id);

    /**
      * 查询所有
    */
    List<Vehicle> selectAll(Vehicle vehicle);

    void deleteByUserId(Long id);

    void deleteBatch(@Param("userIds") List<Long> userIds);

    Vehicle selectByName(String name);

    void updateTypeByIds(@Param("vehicleIds")List<Long> ids,@Param("carType") int carType);

    void monthlyCharge(Long vehicleId);

    int selectTypeById(Long vehicleId);

    void updateTypeTempById(Long vehicleId);

    void deleteBatchByVehicleIds(@Param("vehicleIds") List<Long> ids);
}