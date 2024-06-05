package com.dattran.ecommerceapp.exception;

import com.dattran.ecommerceapp.dto.response.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    HttpResponse handlingAppException(AppException exception, HttpServletRequest httpServletRequest) {
        return HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(exception.getResponseStatus().getCode())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();
    }
}
