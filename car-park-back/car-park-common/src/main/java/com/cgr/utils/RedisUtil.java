package com.cgr.utils;

import com.cgr.vo.LoginUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    public static final Long USER_TOKEN_TTL = 10L;

    @Autowired
    private RedisTemplate redisTemplate;
    public <T> void setCacheObject(String key, T value){
        if(value instanceof LoginUserVo){
            LoginUserVo loginUserVo = (LoginUserVo) value;
            loginUserVo.setToken("");
        }
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> void setCacheObject(String key, T value, Long timeout,  TimeUnit unit){

        if(!(value instanceof LoginUserVo)){
            redisTemplate.opsForValue().set(key,value,timeout,unit);
            return;
        }
        //避免将token存入 redis
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties((LoginUserVo) value, loginUserVo);
        loginUserVo.setToken("");
        redisTemplate.opsForValue().set(key,loginUserVo,timeout,unit);
    }
}
