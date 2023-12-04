package com.jobstore.jobstore.controller.controlleradvice;

import com.jobstore.jobstore.dto.response.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    //유효성검사
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultDto<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "Validation failed: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultDto.of("404",errorMessage,null));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ResultDto<String>> handleOtherExceptions(Exception ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultDto.of("500", errorMessage, null));
    }
}
