package com.cgr.aop.aspect;

import com.cgr.aop.annotation.hasRole;
import com.cgr.entity.LoginUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class AuthorizeAspect {

    /**
     * 定义切入点：方法上有@hasRole注解 或 类上有@hasRole注解
     */
    @Pointcut("@annotation(com.cgr.aop.annotation.hasRole) || @within(com.cgr.aop.annotation.hasRole)")
    public void hasRolePointCut() {}

    /**
     * 前置通知：在目标方法执行前进行权限校验
     */
    @Before("hasRolePointCut()")
    public void checkRole(JoinPoint joinPoint) {
        try {
            // 获取当前认证信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AccessDeniedException("用户未登录");
            }

            // 获取LoginUser对象（假设您的UserDetails实现类是LoginUser）
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof LoginUser)) {
                throw new AccessDeniedException("无效的用户信息");
            }

            LoginUser loginUser = (LoginUser) principal;
            List<String> userRoles = loginUser.getRoleList();

            // 获取所需的角色
            String requiredRole = getRequiredRole(joinPoint);

            // 校验角色
            if (requiredRole != null && !hasRequiredRole(userRoles, requiredRole)) {
                throw new AccessDeniedException("权限不足，需要角色：" + requiredRole);
            }

            // 校验通过
            System.out.println("权限校验通过");

        } catch (Exception e) {
            if (e instanceof AccessDeniedException) {
                throw e;
            }
            throw new AccessDeniedException("权限校验失败：" + e.getMessage());
        }
    }

    /**
     * 获取所需角色
     * 优先获取方法上的注解，如果方法上没有则获取类上的注解
     */
    private String getRequiredRole(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 先检查方法上的注解
        hasRole methodAnnotation = method.getAnnotation(hasRole.class);
        if (methodAnnotation != null) {
            return methodAnnotation.value();
        }

        // 再检查类上的注解
        hasRole classAnnotation = joinPoint.getTarget().getClass().getAnnotation(hasRole.class);
        if (classAnnotation != null) {
            return classAnnotation.value();
        }

        return null;
    }

    /**
     * 检查用户是否具有所需角色
     */
    private boolean hasRequiredRole(List<String> userRoles, String requiredRole) {
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }
        return userRoles.contains(requiredRole);
    }
}
