package com.cgr.controller;

import com.cgr.ResponseModel;
import com.cgr.aop.annotation.HasRole;
import com.cgr.entity.Charge;
import com.cgr.mapper.ChargeMapper;
import com.cgr.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/charge")
public class ChargeController {

    @Autowired
    private ChargeMapper chargeMapper;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/update")
    @HasRole("ADMIN")
    public ResponseModel update(@RequestBody Charge charge){

        chargeMapper.updateCharge(charge);
        //存入redis
        redisUtil.setCharge(charge);
        return ResponseModel.success();
    }

    @GetMapping("/getInfo")
    public ResponseModel getInfo(){
        //redis查询
        // 从 Redis 中尝试获取
        Charge temp = redisUtil.getCharge();
        if(!ObjectUtils.isEmpty( temp)){
            return ResponseModel.success(temp);
        }

        Charge charge = chargeMapper.getInfo();

        //存入redis
        redisUtil.setCharge( charge);
        return ResponseModel.success(charge);
    }
}
