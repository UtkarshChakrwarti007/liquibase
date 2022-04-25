package com.utkarsh.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(com.utkarsh.demo.exception.ResourceNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public com.utkarsh.demo.exception.ErrorMessage resourceNotFoundException(com.utkarsh.demo.exception.ResourceNotFoundException ex, WebRequest request) {
    com.utkarsh.demo.exception.ErrorMessage message = new com.utkarsh.demo.exception.ErrorMessage(
        HttpStatus.NOT_FOUND.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    
    return message;
  }
  
  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public com.utkarsh.demo.exception.ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
    com.utkarsh.demo.exception.ErrorMessage message = new com.utkarsh.demo.exception.ErrorMessage(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    
    return message;
  }
}