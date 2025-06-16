package com.cgr.exception.handler;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cgr.ResponseModel;
import com.cgr.exception.CustomException;
import com.cgr.exception.ObjectEmptyException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();


    @ExceptionHandler(CustomException.class)
    public ResponseModel customError(HttpServletRequest request, CustomException e){
        return ResponseModel.error(e.getCode(), e.getMsg());
    }

    /**
     * 处理 Spring Security 的认证异常（如用户名或密码错误等）
     * 返回 401 Unauthorized
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseModel handleAuthenticationException(AuthenticationException ex) {
        // ResponseModel.authenticatedError(code=402, msg)
        ResponseModel body = new ResponseModel(401,  ex.getMessage(),"认证异常");
        return body;
    }

    /**
     * 处理 DuplicateKeyException 异常（如用户名重复）
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseModel handleDuplicateKeyException(DuplicateKeyException ex) {
        ResponseModel<?> body = ResponseModel.error(ex.getMessage());
        return body;
    }

    /**
     * 处理 Spring Security 的授权异常（如权限不足）
     * 返回 403 Forbidden
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseModel<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ResponseModel<?> body = ResponseModel.error("权限不足");
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    /**
     * 捕获文件上传大小超出异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseModel<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        // 你可以自定义返回格式
        String msg = "文件大小超出限制，上传失败（最大允许大小："
                + (ex.getMaxUploadSize() / 1024 / 1024) + "MB）";
        return ResponseModel.error(msg);
    }

    /**
     * 捕获对象为空异常
     */
    @ExceptionHandler(ObjectEmptyException.class)
    public ResponseModel handleObjectEmptyException(ObjectEmptyException ex) {
        ResponseModel<?> body = ResponseModel.error(ex.getMessage());
        return body;
    }

    /**
     * 捕获其他未处理的运行时异常
     * 返回 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseModel handleOtherExceptions(Exception ex) {
        ex.printStackTrace();
        ResponseModel<?> body = ResponseModel.error( ex.getMessage());
        return body;
    }


}
