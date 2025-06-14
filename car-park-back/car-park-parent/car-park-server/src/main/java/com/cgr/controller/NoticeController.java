package com.cgr.controller;


import com.cgr.ResponseModel;
import com.cgr.entity.Notice;
import com.cgr.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统公告前端操作接口
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public ResponseModel add(@RequestBody Notice notice) {
        noticeService.add(notice);
        return ResponseModel.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel deleteById(@PathVariable Integer id) {
        noticeService.deleteById(id);
        return ResponseModel.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public ResponseModel deleteBatch(@RequestBody List<Integer> ids) {
        noticeService.deleteBatch(ids);
        return ResponseModel.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ResponseModel updateById(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return ResponseModel.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Integer id) {
        Notice notice = noticeService.selectById(id);
        return ResponseModel.success(notice);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public ResponseModel selectAll(Notice notice) {
        List<Notice> list = noticeService.selectAll(notice);
        return ResponseModel.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public ResponseModel selectPage(Notice notice,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Notice> page = noticeService.selectPage(notice, pageNum, pageSize);
        return ResponseModel.success(page);
    }

}