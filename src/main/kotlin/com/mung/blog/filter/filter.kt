package com.mung.blog.filter

import jakarta.servlet.*
import org.springframework.stereotype.Component
import java.io.IOException


@Component
class MyFilter : Filter {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig?) {
        println("init filter")
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        println("before filter")
        chain.doFilter(request, response)
        println("after filter")
    }

    @Throws(IOException::class, ServletException::class)
    override fun destroy() {
        println("destroy filter")
    }
}