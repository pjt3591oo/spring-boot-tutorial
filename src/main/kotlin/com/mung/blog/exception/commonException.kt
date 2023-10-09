package com.mung.blog.exception

import org.springframework.http.HttpStatus

data class BaseResponseError(var code: BaseResponseCode, var msg: String)

enum class BaseResponseCode(status: HttpStatus, message: String) {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
    USER_ALREADY(HttpStatus.CONFLICT, "해당 유저는 이미 존재합니다."),

    VALID_TOKEN(HttpStatus.FORBIDDEN, "토큰이 유효하지 않습니다."),
    VALID_SECRET_KEY(HttpStatus.FORBIDDEN, "토큰 키가 유효하지 않습니다,"),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "토큰이 만료되어 유효하지 않습니다."),

    OK(HttpStatus.OK, "요청 성공");

    public val status: HttpStatus = status
    public val message: String = message
}

class BaseException(baseResponseCode: BaseResponseCode): RuntimeException() {
    public val baseResponseCode: BaseResponseCode = baseResponseCode
}