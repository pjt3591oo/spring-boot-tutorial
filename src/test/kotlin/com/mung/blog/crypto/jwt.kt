package com.mung.blog.crypto

import com.mung.blog.entity.UserRole
import com.mung.blog.exception.BaseException
import com.mung.blog.exception.BaseResponseCode
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles

import java.util.*

@ActiveProfiles("dev")
@AutoConfigureMockMvc
@WebMvcTest(JwtTokenUtil::class)
@ExtendWith(MockitoExtension::class)
class Jwt {
    @Value("\${jwt.secretKey}")
    private val secretKey:String = ""

    @Test
    @DisplayName("JWT 생성")
    fun createToken() {
        val jwtTokenUtil: JwtTokenUtil = JwtTokenUtil()

        val token = jwtTokenUtil.createToken("1", mutableSetOf(), secretKey, (0).toLong(), (10000).toLong())

        assertNotNull(token)
    }

    @Test
    @DisplayName("페이로드 파싱 테스트")
    fun parsePayload() {
        val jwtTokenUtil: JwtTokenUtil = JwtTokenUtil()
        val userIdForPayload = "1"
        val rolesForPayload = mutableSetOf<UserRole>()
        val issueTimeMs = System.currentTimeMillis()
        val expiredTimeMs = issueTimeMs + (60 * 60 * 60 * 1000).toLong()

        val token = jwtTokenUtil.createToken(userIdForPayload, rolesForPayload, secretKey, issueTimeMs, expiredTimeMs)
        val userId = jwtTokenUtil.getUserId(token, secretKey)
        val roles = jwtTokenUtil.getRoles(token, secretKey) as? MutableSet<*> ?: mutableSetOf<UserRole>()

        assertEquals(userId, userIdForPayload)
        assertEquals(rolesForPayload.size, roles.size)
    }

    @Test
    @DisplayName("만료 테스트")
    fun expiredToken() {
        val jwtTokenUtil: JwtTokenUtil = JwtTokenUtil()

        val issueTimeMs = System.currentTimeMillis()
        val expiredTimeMs = issueTimeMs - 1
        val token = jwtTokenUtil.createToken("1", mutableSetOf(), secretKey, issueTimeMs, expiredTimeMs)
        val exception = assertThrows<BaseException> {
            jwtTokenUtil.getUserId(token, secretKey)
        }

        Assertions.assertEquals(BaseResponseCode.EXPIRED_TOKEN, exception.baseResponseCode)
    }


    @Test
    @DisplayName("잘못된 토큰 파싱 시도")
    fun validToken() {
        val jwtTokenUtil: JwtTokenUtil = JwtTokenUtil()

        val token = "eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjoiMSIsInJvbGVzIjpbIlVTRVIiL1JBRE1JTiJdLCJpYXQiOjE2OTY4MTIxOTgsImV4cCI6MTY5NjgxNTc5OH0.A72fNrZ7Ap91t0qmzibO2eEf7fOYICxSLnmM0KAF2d4"
        val exception = assertThrows<BaseException> {
            jwtTokenUtil.getUserId(token, secretKey)
        }

        Assertions.assertEquals(BaseResponseCode.VALID_TOKEN, exception.baseResponseCode)
    }

    @Test
    @DisplayName("잘못된 secretKey로 파싱시도")
    fun validSecretKey() {
        val jwtTokenUtil: JwtTokenUtil = JwtTokenUtil()

        val issueTimeMs = System.currentTimeMillis()
        val expiredTimeMs = issueTimeMs + 10000000
        val otherSecretKey = "testSecretKey20230327testSecretKey20230327testSecretKey20230326"
        val token = jwtTokenUtil.createToken("1", mutableSetOf(), secretKey, issueTimeMs, expiredTimeMs)
        val exception = assertThrows<BaseException> {
            jwtTokenUtil.getUserId(token, otherSecretKey)
        }

        Assertions.assertEquals(BaseResponseCode.VALID_SECRET_KEY, exception.baseResponseCode)
    }
}