package com.cgr.controller;


import com.cgr.ResponseModel;
import com.cgr.entity.Vehicle;
import com.cgr.service.VehicleService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆信息前端操作接口
 **/
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Resource
    private VehicleService vehicleService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public ResponseModel add(@RequestBody Vehicle vehicle) {
        vehicleService.add(vehicle);
        return ResponseModel.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel deleteById(@PathVariable Integer id) {
        vehicleService.deleteById(id);
        return ResponseModel.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public ResponseModel deleteBatch(@RequestBody List<Integer> ids) {
        vehicleService.deleteBatch(ids);
        return ResponseModel.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ResponseModel updateById(@RequestBody Vehicle vehicle) {
        vehicleService.updateById(vehicle);
        return ResponseModel.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Integer id) {
        Vehicle vehicle = vehicleService.selectById(id);
        return ResponseModel.success(vehicle);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public ResponseModel selectAll(Vehicle vehicle) {
        List<Vehicle> list = vehicleService.selectAll(vehicle);
        return ResponseModel.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public ResponseModel selectPage(Vehicle vehicle,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Vehicle> page = vehicleService.selectPage(vehicle, pageNum, pageSize);
        return ResponseModel.success(page);
    }

}