package com.mung.blog.exception

import org.springframework.http.HttpStatus

data class BaseResponseError(var code: BaseResponseCode, var msg: String)

enum class BaseResponseCode(status: HttpStatus, message: String) {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),

    OK(HttpStatus.OK, "요청 성공");

    public val status: HttpStatus = status
    public val message: String = message
}

class BaseException(baseResponseCode: BaseResponseCode): RuntimeException() {
    public val baseResponseCode: BaseResponseCode = baseResponseCode
}