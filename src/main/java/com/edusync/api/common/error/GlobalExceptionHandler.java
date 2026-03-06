package com.edusync.api.common.error;

import com.edusync.api.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorModel> handleServiceException(ServiceException ex) {
        var error = ErrorModel.of(ex.getStatus().value(), ex.getStatus().getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorModel> handleValidation(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage() != null ? e.getDefaultMessage() : "This field is invalid",
                        (a, b) -> a
                ));
        var error = ErrorModel.withFieldErrors(400, "Bad Request",
                "Please check the highlighted fields and try again", fieldErrors);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorModel> handleUnreadable(HttpMessageNotReadableException ex) {
        var error = ErrorModel.of(400, "Bad Request", "The request body is missing or not valid JSON");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorModel> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var message = String.format("'%s' is not a valid value for %s", ex.getValue(), ex.getName());
        var error = ErrorModel.of(400, "Bad Request", message);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        var error = ErrorModel.of(500, "Internal Server Error", "Something went wrong. Please try again later");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
