package com.cgr.controller;


import com.cgr.ResponseModel;
import com.cgr.entity.ParkingLot;
import com.cgr.service.ParkingLotService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车位信息前端操作接口
 **/
@RestController
@RequestMapping("/parkingLot")
public class ParkingLotController {

    @Resource
    private ParkingLotService parkingLotService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public ResponseModel add(@RequestBody ParkingLot parkingLot) {
        parkingLotService.add(parkingLot);
        return ResponseModel.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel deleteById(@PathVariable Integer id) {
        parkingLotService.deleteById(id);
        return ResponseModel.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public ResponseModel deleteBatch(@RequestBody List<Integer> ids) {
        parkingLotService.deleteBatch(ids);
        return ResponseModel.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ResponseModel updateById(@RequestBody ParkingLot parkingLot) {
        parkingLotService.updateById(parkingLot);
        return ResponseModel.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Integer id) {
        ParkingLot parkingLot = parkingLotService.selectById(id);
        return ResponseModel.success(parkingLot);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public ResponseModel selectAll(ParkingLot parkingLot) {
        List<ParkingLot> list = parkingLotService.selectAll(parkingLot);
        return ResponseModel.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public ResponseModel selectPage(ParkingLot parkingLot,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ParkingLot> page = parkingLotService.selectPage(parkingLot, pageNum, pageSize);
        return ResponseModel.success(page);
    }

}