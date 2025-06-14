package com.cgr.aop.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface hasRole {
    /**
     * 所需要的角色
     * @return 角色名称
     */
    String value();
}
