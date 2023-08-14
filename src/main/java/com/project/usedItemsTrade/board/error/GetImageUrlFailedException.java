package com.project.usedItemsTrade.board.error;

import com.project.usedItemsTrade.member.error.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class GetImageUrlFailedException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미지 URL을 불러오는데에 실패하였습니다!";
    }
}
