package com.project.usedItemsTrade.member.error.exception;

import org.springframework.http.HttpStatus;

public class JwtTokenNotValidException extends AbstractException{
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "유효하지 않은 Jwt 토큰입니다!";
    }
}
