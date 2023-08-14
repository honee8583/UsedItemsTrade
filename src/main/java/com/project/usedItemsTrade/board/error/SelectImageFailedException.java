package com.project.usedItemsTrade.board.error;

import com.project.usedItemsTrade.member.error.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class SelectImageFailedException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미지를 불러오는데 실패했습니다!";
    }
}
