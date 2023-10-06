package com.mung.blog.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView


@Component
class HttpInterceptor : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info("[preHandle]")
        return true
    }

    @Throws(Exception::class)
    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        logger.info("[postHandle]")
    }

    @Throws(Exception::class)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        `object`: Any,
        ex: Exception?
    ) {
        logger.info("[afterCompletion]")
    }
}