package com.cgr.controller;

import com.cgr.ResponseModel;
import com.cgr.entity.CPUser;
import com.cgr.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *所有用户
 */
@RestController
@RequestMapping("/allUser")
public class UserController {

    @Autowired
    private UserMapper userMapper;


    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Long id) {
        CPUser user = userMapper.selectById(id);
        return ResponseModel.success(user);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public ResponseModel updateById(@RequestBody CPUser user) {
        userMapper.updateById(user);
        return ResponseModel.success();
    }
}
