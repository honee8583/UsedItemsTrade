package com.project.usedItemsTrade.member.error;

import com.project.usedItemsTrade.member.error.exception.AbstractException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.SendFailedException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    // 회원정보 존재x 예외 처리
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorResponse> usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                Arrays.asList(e.getMessage()));

        log.warn(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 커스텀 예외클래스 처리
    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<ErrorResponse> customExceptionHandler(AbstractException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                Arrays.asList(e.getMessage()));

        log.warn(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }

    // 엔티티의 유효성 검사 예외
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String msg = violation.getMessage();
            errorMessage.append(fieldName).append(" 필드가 ").append(msg).append("; ");
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .messages(Arrays.asList(errorMessage.toString()))
                .build();

        log.warn(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> illegalExceptionHandler(IllegalArgumentException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .messages(Arrays.asList(e.getMessage()))
                .build();

        log.warn(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 입력데이터의 유효성 검사 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            String errorMessage = fieldError.getDefaultMessage();
            errorMessages.add(errorMessage);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .messages(errorMessages)
                .build();

        log.warn(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 이메일 전송 예외
    @ExceptionHandler(SendFailedException.class)
    protected ResponseEntity<ErrorResponse> sendFailedException(SendFailedException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .messages(Arrays.asList(e.getMessage()))
                .build();

        log.warn(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
