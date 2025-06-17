package com.cgr.controller;

import com.cgr.ResponseModel;
import com.cgr.constant.Constants;
import com.cgr.dto.PasswordUpdateDTO;
import com.cgr.entity.CPUser;
import com.cgr.mapper.UserMapper;
import com.cgr.service.UserService;
import com.cgr.utils.SecurityUtil;
import com.cgr.vo.LoginUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @GetMapping("/selectById/{id}")
    public ResponseModel selectById(@PathVariable Long id) {
        LoginUserVo loginUserVo = (LoginUserVo)redisTemplate.opsForValue().get(Constants.LOGIN_USER_KEY + id);
        if(!ObjectUtils.isEmpty(loginUserVo)){
            return ResponseModel.success(loginUserVo);
        }
        CPUser user = userMapper.selectById(id);
        LoginUserVo userVo = new LoginUserVo();
        BeanUtils.copyProperties(user, userVo);
        List<String> roleList = SecurityUtil.getLoginUser().getRoleList();
        userVo.setRoleList(roleList);

        System.out.println("-------------------------------");
        System.out.println(user.getAccount());
        System.out.println(userVo.getAccount());
        System.out.println("-------------------------------");
        return ResponseModel.success(userVo);
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
        userService.updateInfo(user);

        CPUser newuser = userMapper.selectById(user.getId());
        LoginUserVo userVo = new LoginUserVo();
        BeanUtils.copyProperties(newuser, userVo);
        List<String> roleList = SecurityUtil.getLoginUser().getRoleList();
        userVo.setRoleList(roleList);

        //删除redis缓存
        redisTemplate.delete(Constants.LOGIN_USER_KEY + user.getId());
        return ResponseModel.success(userVo);

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
