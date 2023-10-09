package com.mung.blog.controller

import com.mung.blog.entity.User
import com.mung.blog.entity.UserRole
import com.mung.blog.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ReadUserResDto (
    val id: Long?,
    val email: String,
    var roles: MutableSet<UserRole>? = null,
)

@RestController
@RequestMapping("/user")
class UserController (val userService: UserService){
    @GetMapping()
    fun readUser(auth: Authentication): User {
        val userId = auth.principal.toString().toLong()
        return userService.readUserById(userId)
    }
}