package com.mung.blog.service

import com.mung.blog.entity.User
import com.mung.blog.exception.BaseException
import com.mung.blog.exception.BaseResponseCode
import com.mung.blog.repository.UserRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("dev")
@AutoConfigureMockMvc
@WebMvcTest(UserService::class)
@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    @MockBean
    private lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    @BeforeEach
    fun init() {
        userService = UserService(userRepository)
    }

    @Nested
    @DisplayName("회원가입")
    inner class Signup {
        @DisplayName("이미 존재하는 이메일")
        @Test
        fun alreadyExistEmail() {
            val payload = object {
                val email = "test@test.com"
                val password = "password"
            }
            Mockito.`when`(userRepository.findByEmail(payload.email)).thenReturn(Optional.of(User(email = payload.email, password = payload.password)))

            val exception = assertThrows<BaseException> {
                userService.signup(payload.email, payload.password)
            }

            Assertions.assertEquals(BaseResponseCode.USER_ALREADY, exception.baseResponseCode)
        }

        @DisplayName("성공")
        @Test
        fun success() {
            val payload = object {
                val email = "test@test.com"
                val password = "password"
            }
            Mockito.`when`(userRepository.findByEmail(payload.email)).thenReturn(Optional.empty())

            val user = userService.signup(payload.email, payload.password)

            Assertions.assertEquals(user.email, payload.email)
            Assertions.assertEquals(user.password, payload.password)
            verify(userRepository, times(1)).save(user)
        }
    }

    @Nested
    @DisplayName("로그인")
    inner class Login {
        @DisplayName("이메일이 존재하지 않을 때")
        @Test
        fun notMatchEmail() {
            val payload = object {
                val email = "test@test.com"
                val password = "password"
            }
            Mockito.`when`(userRepository.findByEmail(payload.email)).thenReturn(Optional.empty())

            val exception = assertThrows<BaseException> {
                userService.login(payload.email, payload.password)
            }

            Assertions.assertEquals(BaseResponseCode.USER_NOT_FOUND, exception.baseResponseCode)
        }

        @DisplayName("이메일은 존재하지만 비밀번호가 일치하지 않을 때")
        @Test
        fun notMatchPassword() {
            val payload = object {
                val email = "test@test.com"
                val password = "password"
            }
            val notMatchPassword = "p"
            Mockito.`when`(userRepository.findByEmail(payload.email)).thenReturn(Optional.of(User(email = payload.email, password = notMatchPassword)))

            val exception = assertThrows<BaseException> {
                userService.login(payload.email, payload.password)
            }

            Assertions.assertEquals(BaseResponseCode.USER_NOT_FOUND, exception.baseResponseCode)
        }

        @DisplayName("성공")
        @Test
        fun success() {
            val payload = object {
                val email = "test@test.com"
                val password = "password"
            }
            Mockito.`when`(userRepository.findByEmail(payload.email)).thenReturn(Optional.of(User(email = payload.email, password = payload.password)))

            val user = userService.login(payload.email, payload.password)

            Assertions.assertEquals(user.email, payload.email)
            Assertions.assertEquals(user.password, payload.password)
        }
    }
}