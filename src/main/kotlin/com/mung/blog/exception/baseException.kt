package com.mung.blog.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BaseFilter {
    @ExceptionHandler(*[java.lang.NullPointerException::class, ClassCastException::class, BaseException::class, java.lang.Exception::class])
//    @ResponseStatus(code = HttpStatus.OK)
    fun handle(ex: BaseException): ResponseEntity<BaseResponseError> {
        val errorResponse = BaseResponseError(ex.baseResponseCode, ex.baseResponseCode.message)
        return ResponseEntity.status(ex.baseResponseCode.status).body(errorResponse)
    }
}