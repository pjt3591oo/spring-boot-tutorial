package com.mung.blog.controller

import com.mung.blog.crypto.JwtTokenUtil
import com.mung.blog.entity.User

import com.mung.blog.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class LoginReqDto (
    val email: String,
    val password: String,
)

data class SignupReqDto (
    val email: String,
    val password: String,
)

@RestController
@RequestMapping("/auth")
class AuthController(val userService: UserService, val jwtTokenUtil: JwtTokenUtil) {

    @Value("\${jwt.secretKey}")
    private val secretKey:String = ""

    @PostMapping("/login")
    fun login(@RequestBody() loginReqDto: LoginReqDto): String {
        println("asdf")
        val expireTimeMs = (1000 * 60 * 60).toLong() // Token 유효 시간 = 60분
        val issueTimeMs = System.currentTimeMillis()
        val user = userService.login(loginReqDto.email, loginReqDto.password)

        return jwtTokenUtil.createToken(user.id.toString(), user.roles, secretKey, issueTimeMs, issueTimeMs + expireTimeMs)
    }

    @PostMapping("/signup")
    fun signup(@RequestBody() signupReqDto: SignupReqDto): User {
        return userService.signup(signupReqDto.email, signupReqDto.password)
    }
}