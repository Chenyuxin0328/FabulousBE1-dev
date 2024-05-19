package com.creatorsn.fabulous.controller;

import com.creatorsn.fabulous.dto.StatusCode;
import com.creatorsn.fabulous.dto.StdResult;
import com.creatorsn.fabulous.exception.UserException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@ControllerAdvice
public class InputHandler {

    private final Logger logger = LoggerFactory.getLogger(InputHandler.class);

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        var result = StdResult.Status(StatusCode.UserInputError).setErrors(errors).setMessage(errors.isEmpty() ? e.getMessage() : errors.get(0));
        var objectMapper = new ObjectMapper();
        try {
            logger.error(objectMapper.writeValueAsString(result), e);
        } catch (JsonProcessingException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleArgumentNotValidException(ConstraintViolationException e, WebRequest request) {
        List<String> errors = e.getConstraintViolations().stream().map(
                ConstraintViolation::getMessage
        ).toList();
        var result = StdResult.Status(StatusCode.UserInputError).setErrors(errors).setMessage(errors.isEmpty() ? e.getMessage() : errors.get(0));
        var objectMapper = new ObjectMapper();
        try {
            logger.error(objectMapper.writeValueAsString(result), e);
        } catch (JsonProcessingException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserException.class})
    protected ResponseEntity<Object> handleUserException(UserException e, WebRequest request) {
        var result = StdResult.Status(e.getCode(), e.getData()).setMessage(e.getMessage());
        var objectMapper = new ObjectMapper();
        try {
            logger.error(objectMapper.writeValueAsString(result), e);
        } catch (JsonProcessingException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
