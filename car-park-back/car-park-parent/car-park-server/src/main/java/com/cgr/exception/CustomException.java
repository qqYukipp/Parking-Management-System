package com.cgr.exception;

import com.cgr.enums.ResultCodeEnum;

public class CustomException extends RuntimeException {
    private final int code;
    private final String msg;

    // 枚举构造器，也要把 msg 传给父类
    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.msg);
        this.code = resultCodeEnum.code;
        this.msg = resultCodeEnum.msg;
    }

    // 自定义 code/msg 构造器，同样传给父类
    public CustomException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public String getMsg() {
        return msg;
    }
}
