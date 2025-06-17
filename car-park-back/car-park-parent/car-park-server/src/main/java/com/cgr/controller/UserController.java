package com.cgr.controller;

import com.cgr.ResponseModel;
import com.cgr.dto.PasswordUpdateDTO;
import com.cgr.entity.CPUser;
import com.cgr.mapper.UserMapper;
import com.cgr.service.UserService;
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
    @Autowired
    private UserService userService;


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
        String username = user.getUsername();
        Integer count = userMapper.countByUsername(username);
        if(count > 0){
            CPUser oldUser = userMapper.selectByUsername(username);
            if(oldUser.getId()!=user.getId()){
                return ResponseModel.error("用户名已存在");
            }
        }
        userService.updateById(user);
        return ResponseModel.success();
    }

    /**
     * 修改密码
     * @param passwordUpdateDTO
     * @return
     */
    @PutMapping("/updatePassword")
    public ResponseModel updatePassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        userService.updatePassword(passwordUpdateDTO);
        return ResponseModel.success();
    }
}
