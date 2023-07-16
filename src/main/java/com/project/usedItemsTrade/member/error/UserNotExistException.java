package com.project.usedItemsTrade.member.error;

import org.springframework.http.HttpStatus;

public class UserNotExistException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 회원입니다";
    }
}
