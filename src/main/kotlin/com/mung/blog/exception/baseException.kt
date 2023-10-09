package com.mung.blog.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BaseFilter {
    @ExceptionHandler(*[BaseException::class])
    fun handle(ex: BaseException): ResponseEntity<BaseResponseError> {
        val errorResponse = BaseResponseError(ex.baseResponseCode, ex.baseResponseCode.message)
        return ResponseEntity.status(ex.baseResponseCode.status).body(errorResponse)
    }

    @ExceptionHandler(*[java.lang.NullPointerException::class, ClassCastException::class, java.lang.Exception::class])
    fun handle(ex: Exception): ResponseEntity<String> {
        println("======")
        println(ex)
        println(ex.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청 데이터가 올바르지 않습니다")
    }
}