package com.project.usedItemsTrade.member.error.exception;

import org.springframework.http.HttpStatus;

public class PasswordResetLimitTimeInvalidException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "비밀번호 초기화 가능 시간이 지났습니다";
    }
}
