package com.jobstore.jobstore.controller.controlleradvice;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jobstore.jobstore.dto.response.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ExceptionHandler {
    //유효성검사
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResultDto<String>> handlenotFound(NotFoundException ex){
        String em="NotFound:"+ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultDto.of("404",em,null));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResultDto<String>> handleBadRequest(HttpClientErrorException ex){
        String em="BadRequest:"+ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultDto.of("400",em,null));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.MethodNotAllowed.class)
    public ResponseEntity<ResultDto<String>> handleNotallowed(HttpClientErrorException ex){
        String em="Methodnotallowed:"+ex.getMessage();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ResultDto.of("405",em,null));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ResultDto<String>> handleOtherExceptions(Exception ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultDto.of("500", errorMessage, null));
    }
}
