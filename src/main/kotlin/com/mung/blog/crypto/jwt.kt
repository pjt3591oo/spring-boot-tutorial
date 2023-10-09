package com.mung.blog.crypto

import com.mung.blog.entity.UserRole
import com.mung.blog.exception.BaseException
import com.mung.blog.exception.BaseResponseCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date

@Component
class JwtTokenUtil {
    fun createToken(loginId: String, roles: MutableSet<UserRole>?, key: String, issueTimeMs: Long, expireTimeMs: Long): String {

        val claims: Claims = Jwts.claims()
        claims["loginId"] = loginId
        claims["roles"] = roles

        val signingKey = Keys.hmacShaKeyFor(key.toByteArray(StandardCharsets.UTF_8))

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date(issueTimeMs)) // 토큰 생성시간
            .setExpiration(Date(expireTimeMs)) // 토큰이 만료되는 시간
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUserId(token: String, secretKey: String): String {
        return parsePayload(token, secretKey)["loginId"].toString()
    }

    fun getRoles(token: String, secretKey: String): Any? {
        return parsePayload(token, secretKey)["roles"]
    }

    private fun parsePayload(token: String, secretKey: String): Claims {
        try {
            val signingKey = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

            return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).body
        } catch (ex: io.jsonwebtoken.security.SignatureException) {
            throw BaseException(BaseResponseCode.VALID_SECRET_KEY)
        } catch (ex: io.jsonwebtoken.MalformedJwtException) {
            throw BaseException(BaseResponseCode.VALID_TOKEN)
        } catch (ex: io.jsonwebtoken.ExpiredJwtException) {
            throw BaseException(BaseResponseCode.EXPIRED_TOKEN)
        }
    }
}