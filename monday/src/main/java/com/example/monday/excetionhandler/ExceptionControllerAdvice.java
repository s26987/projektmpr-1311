package com.example.monday.excetionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.UUID;


@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundException(RuntimeException e) {
        return new ErrorResponse(UUID.randomUUID(), Instant.now(), e.getMessage());
    }

    @ExceptionHandler(InvalidStudentNameException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestResponse(RuntimeException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(UUID.randomUUID(), Instant.now(), e.getMessage()));
    }
}
