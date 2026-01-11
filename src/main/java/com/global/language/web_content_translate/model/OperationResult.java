package com.global.language.web_content_translate.model;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * 使用 Java 21 record，天然不可变并自动生成 equals/hashCode/toString，序列化开销更小，
 * 同时避免大小写混用的字段命名。
 *
 * @author zz
 */
public record OperationResult<T>(boolean success, int code, String message, T data) implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int MESSAGE_SUCCESS = 200;
    public static final int MESSAGE_FAIL = 500;
    public static final boolean SUCCESS = true;
    public static final boolean FAIL = false;

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
        return new OperationResult<>(success, code, msg, data);
    }
}
