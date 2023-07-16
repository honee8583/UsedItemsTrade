package com.project.usedItemsTrade.member.error;

import lombok.Builder;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ErrorResponse {
    private int code;   // 예외 에러 코드
    private String message;     // 예외 에러 메시지
}
