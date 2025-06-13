package com.cgr.filter;

import com.cgr.constant.Constants;
import com.cgr.entity.LoginUser;
import com.cgr.utils.JwtUtil;
import com.cgr.utils.RedisUtil;
import com.cgr.utils.SecurityUtil;
import com.cgr.vo.LoginUserVo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDetailsService  userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //放行 登录相关的请求
        String  url = request.getRequestURI();
        if (url.contains("/login")||url.contains("/register")||url.contains("/captchaImage")) {
            filterChain.doFilter(request, response);
            return;
        }

        //获取请求头中的token
        String token = parseJwt(request);

        if(token == null){
            throw new BadCredentialsException("token缺失");
        }

        String key = Constants.LOGIN_USER_KEY;
        Claims  claims;
        //取得token中的用户标识（注意解析的异常处理）
        try{
            claims = JwtUtil.parseToken(token);
            Integer id = claims.get("userid",  Integer.class);
            key = key + id;
        }catch (Exception e){
            throw new BadCredentialsException("token无效");
        }

        //从 redis 中获取用户信息
        LoginUserVo loginUserVo =(LoginUserVo) redisTemplate.opsForValue().get(key);

        if(loginUserVo == null){
            //redis数据过期，从数据库从重新获取信息
            String username = claims.get("username", String.class);
            LoginUser loginUser =  (LoginUser) userDetailsService.loadUserByUsername(username);

            //重新存入redis
            redisUtil.setCacheObject(key, loginUserVo, Constants.USER_INFO_TTL, TimeUnit.MINUTES);
        }


        //将用户信息存入安全上下文
        UsernamePasswordAuthenticationToken authentication =
                SecurityUtil.tokenAuthenticate(SecurityUtil.getLoginUserFromLoginUserVo(loginUserVo));
        SecurityUtil.setAuthentication(authentication);

        //放行
        filterChain.doFilter(request, response);

    }




    /**
     * 从 HTTP 请求的 "Authorization" 头中提取并解析出 JWT（JSON Web Token）。
     * <p>
     * 如果该请求头存在，并且以 "Bearer " 为前缀，则截取令牌部分并返回。
     * 否则返回 null，表示请求中没有有效的 JWT。
     *
     * @param request 用于提取 HTTP 头信息的 HttpServletRequest 对象
     * @return 提取出的 JWT 字符串，或在未找到时返回 null
     */
    private String parseJwt(HttpServletRequest request) {
        // 从请求头中获取 "Authorization" 字段的值
        String headerAuth = request.getHeader(Constants.AUTHORIZATION);

        // 校验 headerAuth 是否有文本内容，且以 "Bearer " 前缀开头
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(Constants.TOKEN_PREFIX)) {
            // 去掉 "Bearer " 前缀，只保留实际的 JWT 部分
            return headerAuth.substring(7);
        }

        // 未能从请求头中获取到有效的 Bearer Token，则返回 null
        return null;
    }
}
