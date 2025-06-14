package com.cgr.controller;

import com.cgr.ResponseModel;
import com.cgr.entity.CPUser;
import com.cgr.service.CommonUserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 普通用户
 */

@RestController
@RequestMapping("/user")
public class CommonUserController {

    @Resource
    private CommonUserService userService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public ResponseModel add(@RequestBody CPUser user) {
        userService.add(user);
        return ResponseModel.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseModel.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public ResponseModel deleteBatch(@RequestBody List<Long> ids) {
        userService.deleteBatch(ids);
        return ResponseModel.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ResponseModel updateById(@RequestBody CPUser user) {
        userService.updateById(user);
        return ResponseModel.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Long id) {
        CPUser user = userService.selectById(id);
        return ResponseModel.success(user);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public ResponseModel selectAll(CPUser user) {
        List<CPUser> list = userService.selectAll(user);
        return ResponseModel.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public ResponseModel selectPage(CPUser user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        System.out.println("user = " + user);
        PageInfo<CPUser> page = userService.selectPage(user, pageNum, pageSize);
        return ResponseModel.success(page);
    }
}
