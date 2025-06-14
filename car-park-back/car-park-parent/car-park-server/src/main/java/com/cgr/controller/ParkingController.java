package com.cgr.controller;


import com.cgr.ResponseModel;
import com.cgr.entity.Parking;
import com.cgr.service.ParkingService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 停车信息前端操作接口
 **/
@RestController
@RequestMapping("/parking")
public class ParkingController {

    @Resource
    private ParkingService parkingService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public ResponseModel add(@RequestBody Parking parking) {
        parkingService.add(parking);
        return ResponseModel.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel deleteById(@PathVariable Integer id) {
        parkingService.deleteById(id);
        return ResponseModel.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public ResponseModel deleteBatch(@RequestBody List<Integer> ids) {
        parkingService.deleteBatch(ids);
        return ResponseModel.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ResponseModel updateById(@RequestBody Parking parking) {
        parkingService.updateById(parking);
        return ResponseModel.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Integer id) {
        Parking parking = parkingService.selectById(id);
        return ResponseModel.success(parking);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public ResponseModel selectAll(Parking parking) {
        List<Parking> list = parkingService.selectAll(parking);
        return ResponseModel.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public ResponseModel selectPage(Parking parking,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Parking> page = parkingService.selectPage(parking, pageNum, pageSize);
        return ResponseModel.success(page);
    }

}