package com.cgr.service;

import com.cgr.entity.ParkingLot;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ParkingLotService {

    void add(ParkingLot parkingLot);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);

    void updateById(ParkingLot parkingLot);

    ParkingLot selectById(Integer id);

    List<ParkingLot> selectAll(ParkingLot parkingLot);

    PageInfo<ParkingLot> selectPage(ParkingLot parkingLot, Integer pageNum, Integer pageSize);
}
