package com.cgr.utils;

import com.cgr.entity.CPUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {

    private static final String SECRET = "cgrwdg"; // 秘钥

    private static final long EXPIRATION = 1800000; // 12小时

    /**
     * 生成JWT令牌
     * @param user 用户对象
     * @return 生成的JWT令牌字符串
     */
    public static String generateToken(CPUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", user.getId());
        claims.put("username", user.getUsername());
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token 要解析的JWT令牌字符串
     * @return 包含令牌信息的Claims对象
     * @throws Exception 如果令牌无效或已过期，则抛出异常
     */
    public static Claims parseToken(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


}
