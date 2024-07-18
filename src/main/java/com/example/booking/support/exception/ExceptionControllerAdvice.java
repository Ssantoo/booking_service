package com.example.booking.support.exception;

import com.example.booking.support.exception.AlreadyOccupiedException;
import com.example.booking.support.exception.NotReservableException;
import com.example.booking.support.exception.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        logger.error("CustomException 발생: {}", exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(exception.getStatus(), exception.getMessage()), exception.getStatus());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse resourceNotFoundException(ResourceNotFoundException exception) {
        logger.error("ResourceNotFoundException 발생: {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    public ErrorResponse handleValidationExceptions(Exception ex) {
        String errorMessage = ex.getMessage();
        logger.error("ValidationException 발생: {}", errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse genericException(Exception exception) {
        logger.error("Exception 발생: {}", exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "에러가 발생했습니다.");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({NotReservableException.class, AlreadyOccupiedException.class})
    public ErrorResponse handleConflictExceptions(RuntimeException ex) {
        logger.error("ConflictException 발생: {}", ex.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }


    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorResponse {
        @JsonProperty("status")
        private HttpStatus status;
        @JsonProperty("message")
        private String message;
    }
}
