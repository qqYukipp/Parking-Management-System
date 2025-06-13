package com.cgr.service;

import com.cgr.entity.Vehicle;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface VehicleService {
    void add(Vehicle vehicle);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    void updateById(Vehicle vehicle);

    Vehicle selectById(Integer id);

    List<Vehicle> selectAll(Vehicle vehicle);

    PageInfo<Vehicle> selectPage(Vehicle vehicle, Integer pageNum, Integer pageSize);


}
