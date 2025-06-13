package com.cgr.utils;

import com.cgr.entity.CPUser;
import com.cgr.entity.LoginUser;
import com.cgr.vo.LoginUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {
    /**
     * 存入安全上下文
     * @param authentication
     */
    public static void setAuthentication(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 获取authentication
     * @return
     */
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取loginUser
     */
    public static LoginUser getLoginUser(){
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 清空安全上下文
     */
    public static void clearAuthentication(){
        SecurityContextHolder.clearContext();
    }

    public static UsernamePasswordAuthenticationToken tokenAuthenticate(LoginUser loginUser){
        return UsernamePasswordAuthenticationToken.authenticated(loginUser, null, null);
    }

    public static UsernamePasswordAuthenticationToken tokenUnauthenticate(String  username,String  password){
        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }


    public static LoginUserVo getLoginUserVoFromLoginUser(LoginUser loginUser) {
        CPUser user = loginUser.getUser();
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        return loginUserVo;
    }

    public static LoginUser getLoginUserFromLoginUserVo(LoginUserVo loginUserVo) {
        CPUser  user = new CPUser();
        BeanUtils.copyProperties(loginUserVo, user);
        return new LoginUser(user, loginUserVo.getRoleList());
    }
}
