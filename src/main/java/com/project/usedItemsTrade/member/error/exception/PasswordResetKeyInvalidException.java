package com.project.usedItemsTrade.member.error.exception;

import org.springframework.http.HttpStatus;

public class PasswordResetKeyInvalidException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "비밀번호 초기화 키값이 유효하지 않습니다";
    }
}
