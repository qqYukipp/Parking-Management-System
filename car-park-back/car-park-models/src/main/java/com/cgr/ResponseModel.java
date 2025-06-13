package com.cgr;

public class ResponseModel<T> {
    /**
     * 响应码
     */
    private int code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public ResponseModel(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseModel<T> success(T data) {
        return new ResponseModel<T>(200, "success", data);
    }

    public static <T> ResponseModel<T> success() {
        return new ResponseModel<T>(200, "success", null);
    }

    public static <T> ResponseModel<T> error(T data) {
        return new ResponseModel<T>(401, "error", data);
    }

    public static <T> ResponseModel<T> error(){
        return new ResponseModel<T>(500, "error", null);
    }

    public static <T> ResponseModel<T> error(int code, String msg){
        return new ResponseModel<T>(code, "msg", null);
    }


    public static <T> ResponseModel<T> error(String msg) {

        return new ResponseModel<T>(500, msg, null);
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
