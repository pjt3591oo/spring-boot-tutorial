package com.mung.blog.service

import com.mung.blog.entity.User
import com.mung.blog.exception.*
import com.mung.blog.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(val userRepository: UserRepository) {
    @Transactional
    fun signup (email: String, password: String): User {
        val alreadyUser = userRepository.findByEmail(email).orElse(null)

        if (alreadyUser != null) {
            throw BaseException(BaseResponseCode.USER_ALREADY)
        }

        val user = User(email = email, password = password)
        userRepository.save(user)
        return user
    }

    fun login(email: String, password: String): User {
        val user = userRepository.findByEmail(email).orElseThrow{BaseException(BaseResponseCode.USER_NOT_FOUND)}

        if (user.password != password) {
            throw BaseException(BaseResponseCode.USER_NOT_FOUND)
        }

        return user
    }

    fun readUserById(id: Long): User {
        val user = userRepository.readById(id)

        return user[0]
    }
}