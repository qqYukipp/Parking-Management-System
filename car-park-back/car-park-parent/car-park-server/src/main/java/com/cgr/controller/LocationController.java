package com.cgr.controller;


import com.cgr.ResponseModel;
import com.cgr.entity.Location;
import com.cgr.service.LocationService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 停车区域表前端操作接口
 **/
@RestController
@RequestMapping("/location")
public class LocationController {

    @Resource
    private LocationService locationService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public ResponseModel add(@RequestBody Location location) {
        locationService.add(location);
        return ResponseModel.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel deleteById(@PathVariable Integer id) {
        locationService.deleteById(id);
        return ResponseModel.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public ResponseModel deleteBatch(@RequestBody List<Integer> ids) {
        locationService.deleteBatch(ids);
        return ResponseModel.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ResponseModel updateById(@RequestBody Location location) {
        locationService.updateById(location);
        return ResponseModel.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Integer id) {
        Location location = locationService.selectById(id);
        return ResponseModel.success(location);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public ResponseModel selectAll(Location location) {
        List<Location> list = locationService.selectAll(location);
        return ResponseModel.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public ResponseModel selectPage(Location location,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Location> page = locationService.selectPage(location, pageNum, pageSize);
        return ResponseModel.success(page);
    }

}