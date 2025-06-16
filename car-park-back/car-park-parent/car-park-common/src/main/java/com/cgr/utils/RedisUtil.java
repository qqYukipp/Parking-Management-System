package com.cgr.utils;

import com.cgr.vo.LoginUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    public static final Long USER_TOKEN_TTL = 10L;
    private static final String MENU_TREE_KEY = "menu_tree:";

    @Autowired
    private RedisTemplate redisTemplate;

    //菜单树采用stringRedisTemplate
    @Autowired
    private StringRedisTemplate  stringRedisTemplate;
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

    /**
     * 缓存整个菜单树，默认过期 1 小时
     */
    public void cacheMenuTree(String menuTreeJson,  Long userId) {
        stringRedisTemplate.opsForValue()
                .set(MENU_TREE_KEY + userId, menuTreeJson, Duration.ofHours(1));
    }

    /**
     * 从缓存读取菜单树 JSON 字符串
     */
    public String getMenuTreeJson(Long userId) {
        return stringRedisTemplate.opsForValue().get(MENU_TREE_KEY + userId);
    }

    /**
     * 删除缓存
     */
    public void evictMenuTree(Long userId) {
        stringRedisTemplate.delete(MENU_TREE_KEY + userId);
    }
}
