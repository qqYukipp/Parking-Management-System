package com.cgr.controller;

import com.cgr.ResponseModel;
import com.cgr.aop.annotation.DelRedisMenu;
import com.cgr.aop.annotation.HasRole;
import com.cgr.constant.Role;
import com.cgr.entity.CPUser;
import com.cgr.service.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@HasRole(Role.ROLE_ADMIN)
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 给用户增加管理员权限
     * @param ids
     * @return
     */
    @PutMapping("/liftToAdmin")
    @DelRedisMenu
    public ResponseModel liftToAdmin(@RequestBody List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return ResponseModel.error("请选择用户");
        }
        adminService.liftToAdmin(ids);
        return ResponseModel.success();
    }

    @PutMapping("/downToUser")
    @DelRedisMenu
    public ResponseModel downToUser(@RequestBody List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return ResponseModel.error("请选择用户");
        }
        adminService.downToUser(ids);
        return ResponseModel.success();
    }

    @PostMapping("/add")
    public ResponseModel add(@RequestBody CPUser user) {
        /*System.out.println("user = " + user);
        adminService.add(user);*/
        return ResponseModel.error("暂不支持此功能");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel delete(@PathVariable Long id) {
        adminService.deleteById(id);
        return ResponseModel.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public ResponseModel delete(@RequestBody List<Long> ids) {
        adminService.deleteBatch(ids);
        return ResponseModel.success();
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public ResponseModel update(@RequestBody CPUser user) {
        adminService.updateById(user);
        return ResponseModel.success();
    }

    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Long id) {
        CPUser user = adminService.selectById(id);
        return ResponseModel.success(user);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public ResponseModel selectAll(CPUser user) {
        List<CPUser> list = adminService.selectAll(user);
        return ResponseModel.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectPage")
    public ResponseModel selectPage(
            CPUser user,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<CPUser> pageInfo = adminService.selectPage(user, pageNum, pageSize);
        return ResponseModel.success(pageInfo);
    }

}
