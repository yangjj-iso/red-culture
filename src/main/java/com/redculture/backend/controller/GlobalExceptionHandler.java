package com.redculture.backend.controller;

import com.redculture.backend.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 数据库连接异常
    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ResponseEntity<ApiResponse<Object>> handleDbConnection(CannotGetJdbcConnectionException ex) {
        logger.error("数据库连接失败", ex);
        ApiResponse<Object> response = ApiResponse.error(503, "数据库连接失败，请设置 DB_URL/DB_USERNAME/DB_PASSWORD 后重试");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    // 参数验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.badRequest("参数验证失败");
        response.setData(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 参数类型不匹配异常
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("参数 '%s' 的值 '%s' 类型不正确", ex.getName(), ex.getValue());
        logger.warn(message, ex);
        ApiResponse<Object> response = ApiResponse.badRequest(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 非法参数异常
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("非法参数异常", ex);
        ApiResponse<Object> response = ApiResponse.badRequest(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 认证失败异常
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        logger.warn("认证失败", ex);
        ApiResponse<Object> response = ApiResponse.unauthorized("用户名或密码错误");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 权限不足异常
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        logger.warn("访问被拒绝", ex);
        ApiResponse<Object> response = ApiResponse.forbidden("权限不足，无法访问该资源");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 空指针异常
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Object>> handleNullPointer(NullPointerException ex) {
        logger.error("空指针异常", ex);
        ApiResponse<Object> response = ApiResponse.error("系统内部错误：空指针异常");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // 通用异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        logger.error("系统异常", ex);
        ApiResponse<Object> response = ApiResponse.error("系统内部错误，请联系管理员");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


