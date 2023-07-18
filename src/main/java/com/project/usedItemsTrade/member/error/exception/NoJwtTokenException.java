package com.project.usedItemsTrade.member.error.exception;

import org.springframework.http.HttpStatus;

public class NoJwtTokenException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "Jwt 토큰이 전달되지 않았습니다!";
    }
}
