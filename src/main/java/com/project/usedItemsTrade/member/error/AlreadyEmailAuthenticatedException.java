package com.project.usedItemsTrade.member.error;

import org.springframework.http.HttpStatus;

public class AlreadyEmailAuthenticatedException extends AbstractException{

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 이메일 인증을 한 회원입니다";
    }
}
