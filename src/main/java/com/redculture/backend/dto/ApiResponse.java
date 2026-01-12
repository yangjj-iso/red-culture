package com.redculture.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ApiResponse(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应（无数据）
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "操作成功");
    }

    // 成功响应（带数据）
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    // 成功响应（自定义消息和数据）
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    // 失败响应
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message);
    }

    // 失败响应（自定义状态码）
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message);
    }

    // 参数错误
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message);
    }

    // 未授权
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(401, message);
    }

    // 禁止访问
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, message);
    }

    // 未找到
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message);
    }
}
