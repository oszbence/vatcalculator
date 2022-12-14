package com.gb.vatcalculator.exception;

import com.gb.vatcalculator.dto.ErrorDto;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(20)
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<ErrorDto> responseEntityFactory(HttpStatus status, String message) {
        return new ResponseEntity<>(new ErrorDto(status.name(), message), status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        return responseEntityFactory(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
