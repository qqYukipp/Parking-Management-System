package com.cgr.controller;


import com.cgr.ResponseModel;
import com.cgr.entity.Pay;
import com.cgr.service.PayService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 缴费信息前端操作接口
 **/
@RestController
@RequestMapping("/pay")
public class PayController {

    @Resource
    private PayService payService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public ResponseModel add(@RequestBody Pay pay) {
        payService.add(pay);
        return ResponseModel.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel deleteById(@PathVariable Integer id) {
        payService.deleteById(id);
        return ResponseModel.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public ResponseModel deleteBatch(@RequestBody List<Integer> ids) {
        payService.deleteBatch(ids);
        return ResponseModel.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ResponseModel updateById(@RequestBody Pay pay) {
        payService.updateById(pay);
        return ResponseModel.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Integer id) {
        Pay pay = payService.selectById(id);
        return ResponseModel.success(pay);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public ResponseModel selectAll(Pay pay) {
        List<Pay> list = payService.selectAll(pay);
        return ResponseModel.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public ResponseModel selectPage(Pay pay,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Pay> page = payService.selectPage(pay, pageNum, pageSize);
        return ResponseModel.success(page);
    }

}