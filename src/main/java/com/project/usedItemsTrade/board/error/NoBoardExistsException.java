package com.project.usedItemsTrade.board.error;

import com.project.usedItemsTrade.member.error.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoBoardExistsException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "게시물이 삭제되었거나 존재하지 않습니다!";
    }
}
