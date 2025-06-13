package com.cgr.service;

import com.cgr.entity.Location;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface LocationService {
    Location selectById(Integer id);

    List<Location> selectAll(Location location);

    PageInfo<Location> selectPage(Location location, Integer pageNum, Integer pageSize);

    void add(Location location);

    void updateById(Location location);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);
}
