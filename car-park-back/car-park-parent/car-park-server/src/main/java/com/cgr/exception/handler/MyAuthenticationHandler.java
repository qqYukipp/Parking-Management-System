package com.cgr.exception.handler;

import com.cgr.ResponseModel;
import com.cgr.utils.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//目前只处理token异常

/**
 * 自定义认证异常处理器
 */
@Component
public class MyAuthenticationHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseUtil.write(response, ResponseModel.authenticatedError(authException.getMessage()));
    }
}