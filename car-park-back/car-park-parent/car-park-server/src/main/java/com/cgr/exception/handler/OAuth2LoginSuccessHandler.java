package com.cgr.exception.handler;

import cn.hutool.core.lang.UUID;
import com.cgr.constant.Constants;
import com.cgr.entity.CPUser;
import com.cgr.mapper.RoleMapper;
import com.cgr.mapper.UserMapper;
import com.cgr.utils.JwtUtil;
import com.cgr.utils.RedisUtil;
import com.cgr.vo.LoginUserVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.cgr.constant.Constants.LOGIN_USER_KEY;
import static com.cgr.constant.Role.ROLE_USER;
import static com.cgr.constant.Role.ROLE_USER_ID;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisUtil  redisUtil;
    @Autowired
    private UserMapper  userMapper;
    @Autowired
    private RoleMapper roleMapper;

    // 单例 ObjectMapper，避免每次 new
    private static final ObjectMapper JACKSON = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("OAuth2进来");
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String registrationId = ((OAuth2AuthenticationToken) authentication)
                .getAuthorizedClientRegistrationId();

        // 1. 创建临时用户并写入 Redis，拿到 VO
        LoginUserVo loginUserVo = addUserByOAuth2(oAuth2User, registrationId);

        // 2. 把完整对象序列化到 Cookie（URL 编码）
        String json = JACKSON.writeValueAsString(loginUserVo);
        String encoded = URLEncoder.encode(json, StandardCharsets.UTF_8);

        Cookie cookie = new Cookie("loginUserVo", encoded);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 3);
        // cookie.setHttpOnly(false); // 默认就是可被 JS 读取
        response.addCookie(cookie);

        // 3. 最后重定向到前端，不带 query
        response.sendRedirect("http://localhost:5173/index");
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginUserVo addUserByOAuth2(DefaultOAuth2User oAuth2User, String provider) {
        CPUser user = new CPUser();

        user.setUsername(provider + "_user:" + UUID.randomUUID().toString());
        user.setPassword(new BCryptPasswordEncoder().encode("1234"));
        user.setNickName(oAuth2User.getAttribute("name"));
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setAvatar(oAuth2User.getAttribute("avatar_url"));
        user.setPhone(oAuth2User.getAttribute("phone"));

        userMapper.insert(user);
        Long userId = user.getId();

        roleMapper.insertUserRole(ROLE_USER_ID,userId);

        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);

        loginUserVo.setToken(JwtUtil.generateToken(user));
        loginUserVo.setRoleList(Arrays.asList(ROLE_USER));
        loginUserVo.setAccount(0.00);

        //存入redis
        redisUtil.setCacheObject(LOGIN_USER_KEY+userId, loginUserVo, Constants.USER_INFO_TTL, TimeUnit.MINUTES);
        return loginUserVo;
    }
}




