package com.cgr.utils;

import com.cgr.entity.Charge;
import com.cgr.vo.LoginUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    public static final Long USER_TOKEN_TTL = 10L;
    public static final String MENU_TREE_KEY = "menu_tree:";
    public static final String CHARGE_CONFIG = "charge:config";


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


    public void setCharge(Charge charge) {
        Map<String, Object> chargeMap = new HashMap<>();
        chargeMap.put("firstHour", charge.getFirstHour());
        chargeMap.put("perHour", charge.getPerHour());
        chargeMap.put("dailyCap", charge.getDailyCap());
        chargeMap.put("monthlyFee", charge.getMonthlyFee());
        chargeMap.put("freeMinutes", charge.getFreeMinutes());

        redisTemplate.opsForHash().putAll(CHARGE_CONFIG, chargeMap);
    }

    public Charge getCharge() {
        Map<Object, Object> chargeMap = redisTemplate.opsForHash().entries(CHARGE_CONFIG);
        if (chargeMap == null || chargeMap.isEmpty()) {
            return null; // 或者你可以抛出异常，说明 Redis 中没有缓存
        }

        Charge charge = new Charge();

        Object firstHour = chargeMap.get("firstHour");
        Object perHour = chargeMap.get("perHour");
        Object dailyCap = chargeMap.get("dailyCap");
        Object monthlyFee = chargeMap.get("monthlyFee");
        Object freeMinutes = chargeMap.get("freeMinutes");

        if (firstHour != null) {
            charge.setFirstHour(Double.valueOf(firstHour.toString()));
        }
        if (perHour != null) {
            charge.setPerHour(Double.valueOf(perHour.toString()));
        }
        if (dailyCap != null) {
            charge.setDailyCap(Double.valueOf(dailyCap.toString()));
        }
        if (monthlyFee != null) {
            charge.setMonthlyFee(Double.valueOf(monthlyFee.toString()));
        }
        if (freeMinutes != null) {
            charge.setFreeMinutes(Integer.valueOf(freeMinutes.toString()));
        }

        return charge;
    }

}
