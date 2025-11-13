package com.webkit.travel_safety_backend.global;

import com.webkit.travel_safety_backend.global.api.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFoundException(
            EntityNotFoundException e,
            HttpServletRequest request) {
        log.error("EntityNotFoundException: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.fail(e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(
            Exception e,
            HttpServletRequest request) {
        log.error("Exception: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.fail(e.getMessage())
        );
    }

}
