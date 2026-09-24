package com.indra.retail.orders.web;

import com.indra.retail.orders.service.OrderNotFoundException;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return response(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(OrderNotFoundException exception) {
        return response(HttpStatus.NOT_FOUND, List.of(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, List.of("Error interno del servidor"));
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, List<String> errors) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(Instant.now(), status.value(), errors));
    }
}