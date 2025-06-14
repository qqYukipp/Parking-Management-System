package com.cgr.exception;

/**
 * 对象为空异常类
 * 当对象为空或null时抛出此异常
 */
public class ObjectEmptyException extends RuntimeException {
    
    /**
     * 默认构造函数
     */
    public ObjectEmptyException() {
        super("Object is empty or null");
    }
    
    /**
     * 带自定义消息的构造函数
     * @param message 异常消息
     */
    public ObjectEmptyException(String message) {
        super(message);
    }
    
    /**
     * 带自定义消息和原因的构造函数
     * @param message 异常消息
     * @param cause 异常原因
     */
    public ObjectEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 带原因的构造函数
     * @param cause 异常原因
     */
    public ObjectEmptyException(Throwable cause) {
        super("Object is empty or null", cause);
    }
    
    /**
     * 完整构造函数
     * @param message 异常消息
     * @param cause 异常原因
     * @param enableSuppression 是否启用异常抑制
     * @param writableStackTrace 是否可写堆栈跟踪
     */
    public ObjectEmptyException(String message, Throwable cause, 
                               boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}