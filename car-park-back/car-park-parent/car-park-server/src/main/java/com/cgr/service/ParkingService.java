package com.cgr.service;

import com.cgr.entity.Parking;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ParkingService {

    void add(Parking parking);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    void updateById(Parking parking);

    Parking selectById(Integer id);

    List<Parking> selectAll(Parking parking);

    PageInfo<Parking> selectPage(Parking parking, Integer pageNum, Integer pageSize);
}
