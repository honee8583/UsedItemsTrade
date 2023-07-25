package com.project.usedItemsTrade.board.error;

import com.project.usedItemsTrade.member.error.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UserNotMatchException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "본인이 아닙니다!";
    }
}
