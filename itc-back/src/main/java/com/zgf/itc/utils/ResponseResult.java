package com.zgf.itc.utils;

import lombok.Data;

/**
 * @author ZGF
 */

@Data
public class ResponseResult {

    private int code;
    private String msg;
    private Object data;

    private ResponseResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static ResponseResult ok() {
        return new ResponseResult(200, null, null);
    }
    public static ResponseResult ok(String msg) {
        return new ResponseResult(200, msg, null);
    }
    public static ResponseResult ok(Object data) {
        return new ResponseResult(200, null, data);
    }
    public static ResponseResult ok(String msg, Object data) {
        return new ResponseResult(200, msg, data);
    }

    public static ResponseResult error() {
        return new ResponseResult(400, null, null);
    }
    public static ResponseResult error(String msg) {
        return new ResponseResult(400, msg, null);
    }
    public static ResponseResult e404() {
        return new ResponseResult(404, "资源未找到", null);
    }
    public static ResponseResult e500() {
        return new ResponseResult(500, "服务器错误", null);
    }
    public static ResponseResult e500(String msg) {
        return new ResponseResult(500, msg, null);
    }
}
