package com.project.usedItemsTrade.member.error;

import org.springframework.http.HttpStatus;

public class AlreadyExistMemberException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 회원입니다";
    }
}
