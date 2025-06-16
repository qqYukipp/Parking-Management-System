package com.cgr.aop.aspect;

import com.cgr.entity.LoginUser;
import com.cgr.utils.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DelRedisMenuAspect {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 定义切入点：方法上有@delRedisMenu注解
     */
    @Pointcut("@annotation(com.cgr.aop.annotation.DelRedisMenu)")
    public void delMenuPointCut() {}

    /**
     * 后置通知，先更新数据库后删除缓存
     */
    @After("delMenuPointCut()")
    public void delMenu(JoinPoint joinPoint) {
        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("用户未登录");
        }

        // 获取LoginUser对象
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof LoginUser)) {
            throw new AccessDeniedException("无效的用户信息");
        }

        LoginUser loginUser = (LoginUser) principal;
        Long userId = loginUser.getUser().getId();

        redisUtil.evictMenuTree(userId);
    }
}
