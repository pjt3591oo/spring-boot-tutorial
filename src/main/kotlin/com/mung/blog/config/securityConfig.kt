package com.mung.blog.config

import com.mung.blog.crypto.JwtTokenUtil
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.system.measureTimeMillis


@Configuration
@EnableWebSecurity
class SecurityConfig() {
    @Value("\${jwt.secretKey}")
    private val secretKey:String = ""

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .addFilterBefore(LoggingFilter(), BasicAuthenticationFilter::class.java)
            .addFilterBefore(JwtTokenFilter(JwtTokenUtil(), secretKey), UsernamePasswordAuthenticationFilter::class.java)

        http.csrf{
            it.disable()
        }

        http.authorizeHttpRequests {
            it.requestMatchers("/user/**").hasAnyAuthority("ADMIN", "USER")
            it.requestMatchers("/post/**").hasAnyAuthority("ADMIN", "USER")

            it.requestMatchers("/auth/login").permitAll() // 인증없이 사용가능
            it.requestMatchers("/auth/signup").permitAll() // 인증없이 사용가능
            it.requestMatchers("/blog/**").permitAll() // 인증없이 사용가능
            it.anyRequest().authenticated() // 위 설정을 제외한 나머지 요청은 전부 인증이 필요
        }

        return http.build()

    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder?) {
        println("configureGlobal")
    }
}

class LoggingFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {

        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        println("[REQ] ${httpServletRequest.method}  ${httpServletRequest.servletPath}")

        val measureTimeMillis = measureTimeMillis {
            chain.doFilter(request, response)
        }

        println("[RES] ${httpServletResponse.status}, $measureTimeMillis ms")
    }

}

class JwtTokenFilter(private val jwtTokenUtil: JwtTokenUtil, private val secretKey: String): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)


        if (authorizationHeader == null) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authorizationHeader.substring(7)

        // JWT의 payload에서 userId와 roles 가져옴
        println(1)
        val userId = jwtTokenUtil.getUserId(token, secretKey)
        println(2)
        val roles = jwtTokenUtil.getRoles(token, secretKey) as List<*>
        println(3)
        val authorities: MutableList<GrantedAuthority> = ArrayList()

        // roles을 authrities에 추가함
        roles.forEach{
            println(it)
            authorities.add(SimpleGrantedAuthority(it.toString()))
        }

        val authenticationToken = UsernamePasswordAuthenticationToken(
            userId,
            null,
            authorities
        )

        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        // 권한 부여
        // 스프링 시큐리티에 미리 구현된 필터에서 authentication.authorities를 검사를 수행함
        SecurityContextHolder.getContext().authentication = authenticationToken;
        filterChain.doFilter(request, response);

        return

    }
}