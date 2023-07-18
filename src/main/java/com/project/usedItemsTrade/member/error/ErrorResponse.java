package com.project.usedItemsTrade.member.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ErrorResponse {
    private int code;
    private List<String> messages;

    public ErrorResponse(int code, List<String> messages) {
        this.code = code;
        this.messages = messages;
    }
}
