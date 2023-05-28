package com.global.language.web_content_translate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author zz
 */
@Data
@NoArgsConstructor
public class OperationResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     *
     */
    public static final int MESSAGE_SUCCESS = 200;

    /**
     * 失败
     *
     */
    public static final int MESSAGE_FAIL = 500;

    /**
     * 成功
     *
     */
    public static final boolean SUCCESS = true;

    /**
     * 失败
     *
     */
    public static final boolean FAIL = false;

    private boolean Success;

    private int Code;

    private String Message;

    private T Data;

    public static <T> OperationResult<T> ok() {
        return restResult(SUCCESS, null, MESSAGE_SUCCESS, "操作成功");
    }

    public static <T> OperationResult<T> ok(T data) {
        return restResult(SUCCESS, data, MESSAGE_SUCCESS, "操作成功");
    }



    public static <T> OperationResult<T> ok(String msg, T data) {
        return restResult(SUCCESS, data, MESSAGE_SUCCESS, msg);
    }

    public static <T> OperationResult<T> fail() {
        return restResult(FAIL, null, MESSAGE_FAIL, "操作失败");
    }

    public static <T> OperationResult<T> fail(String msg) {
        return restResult(FAIL, null, MESSAGE_FAIL, msg);
    }

    public static <T> OperationResult<T> fail(T data) {
        return restResult(FAIL, data, MESSAGE_FAIL, "操作失败");
    }

    public static <T> OperationResult<T> fail(String msg, T data) {
        return restResult(FAIL, data, MESSAGE_FAIL, msg);
    }

    public static <T> OperationResult<T> fail(int code, String msg) {
        return restResult(FAIL, null, code, msg);
    }

    private static <T> OperationResult<T> restResult(boolean success, T data, int code, String msg) {
        OperationResult<T> r = new OperationResult<>();
        r.setCode(code);
        r.setData(data);
        r.setMessage(msg);
        r.setSuccess(success);
        return r;
    }

}
