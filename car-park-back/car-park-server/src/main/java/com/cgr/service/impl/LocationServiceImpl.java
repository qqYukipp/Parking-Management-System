package com.cgr.service.impl;

import com.cgr.entity.Location;
import com.cgr.entity.ParkingLot;
import com.cgr.exception.CustomException;
import com.cgr.mapper.LocationMapper;
import com.cgr.mapper.ParkingLotMapper;
import com.cgr.service.LocationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Resource
    private LocationMapper locationMapper;
    @Resource
    private ParkingLotMapper parkingLotMapper;

    /**
     * 新增
     */
    public void add(Location location) {
        locationMapper.insert(location);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {

        Location location = locationMapper.selectById(id);

        if (location.getTotal() > 0) {
            throw new CustomException(500, "该区域有停车位信息，暂不支持删除");
        }

        locationMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            locationMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Location location) {
        locationMapper.updateById(location);
    }

    /**
     * 根据ID查询
     */
    public Location selectById(Integer id) {
        return locationMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Location> selectAll(Location location) {
        List<Location> locations = locationMapper.selectAll(location);
        for (Location dbLocation : locations) {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setLocationId(dbLocation.getId());
            List<ParkingLot> parkingLots = parkingLotMapper.selectAll(parkingLot);
            dbLocation.setParkingLots(parkingLots);
        }
        return locations;
    }

    /**
     * 分页查询
     */
    public PageInfo<Location> selectPage(Location location, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Location> list = this.selectAll(location);
        return PageInfo.of(list);
    }
}
