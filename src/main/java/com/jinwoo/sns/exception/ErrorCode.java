package com.jinwoo.sns.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"유저이름이 중복됩니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"인터널서버 에러"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"유저가 없습니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"패스워드가 불일치 합니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"토큰이 유효하지 않습니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"포스트가 존재하지 않습니다"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED,"권한이 없습니다")


    ;


    private final HttpStatus status;
    private final String message;
}
