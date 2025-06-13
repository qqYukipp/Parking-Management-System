package com.cgr.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.cgr.entity.Location;
import com.cgr.entity.ParkingLot;
import com.cgr.mapper.ParkingLotMapper;
import com.cgr.service.LocationService;
import com.cgr.service.ParkingLotService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Resource
    private ParkingLotMapper parkingLotMapper;
    @Resource
    private LocationService locationService;

    /**
     * 新增
     */
    public void add(ParkingLot parkingLot) {
        parkingLotMapper.insert(parkingLot);
        Location location = locationService.selectById(parkingLot.getLocationId());
        // 新增一个车位，同步该区域的总车位和空闲车位
        if (ObjUtil.isNotEmpty(location)) {
            location.setTotal(location.getTotal() + 1);
            if ("空闲".equals(parkingLot.getStatus())) {
                location.setNum(location.getNum() + 1);
            }
            locationService.updateById(location);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        ParkingLot parkingLot = parkingLotMapper.selectById(id);
        parkingLotMapper.deleteById(id);
        Location location = locationService.selectById(parkingLot.getLocationId());
        location.setTotal(location.getTotal() - 1);
        if ("空闲".equals(parkingLot.getStatus())) {
            location.setNum(location.getNum() - 1);
        }
        locationService.updateById(location);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            parkingLotMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(ParkingLot parkingLot) {
        ParkingLot dbLot = parkingLotMapper.selectById(parkingLot.getId());
        parkingLotMapper.updateById(parkingLot);
        // 判断是否需要更新该区域的空闲车位状态
        Location location = locationService.selectById(parkingLot.getLocationId());
        if ("空闲".equals(dbLot.getStatus()) && "占用".equals(parkingLot.getStatus())) {
            location.setNum(location.getNum() - 1);
        }
        if ("占用".equals(dbLot.getStatus()) && "空闲".equals(parkingLot.getStatus())) {
            location.setNum(location.getNum() + 1);
        }
        locationService.updateById(location);
    }

    /**
     * 根据ID查询
     */
    public ParkingLot selectById(Integer id) {
        return parkingLotMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<ParkingLot> selectAll(ParkingLot parkingLot) {
        return parkingLotMapper.selectAll(parkingLot);
    }

    /**
     * 分页查询
     */
    public PageInfo<ParkingLot> selectPage(ParkingLot parkingLot, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ParkingLot> list = this.selectAll(parkingLot);
        return PageInfo.of(list);
    }
}
