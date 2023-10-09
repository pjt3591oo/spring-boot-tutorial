package com.mung.blog.controller

import com.mung.blog.crypto.JwtTokenUtil
import com.mung.blog.service.UserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebAppConfiguration( )
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@WebMvcTest(AuthController::class)
@ExtendWith(MockitoExtension::class)
class AuthControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @MockBean
    private lateinit var userService: UserService

    @DisplayName("회원가입 성공")
    @WithMockUser
    @Test
    fun success() {
        val email = System.currentTimeMillis().toString()
        val password = System.currentTimeMillis().toString()
        val requestJson = "{\"email\":\"$email\", \"password\":\"$password\"}"

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").value("11")) // 첫 번째 항목의 title 검사
            .andExpect(jsonPath("$.email").value("22")) // 첫 번째 항목의 body 검사
            .andExpect(jsonPath("$.password").value("111")) // 두 번째 항목의 title 검사
            .andExpect(jsonPath("$.roles").value("222")) // 두 번째 항목의 body 검사
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("400: email")
    @Test
    fun badRequestByEmail() {
        val password = System.currentTimeMillis().toString()
        val requestJson = "{\"password\":\"$password\"}"

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("400: password")
    @Test
    fun badRequestByPassword() {
        val email = System.currentTimeMillis().toString()
        val requestJson = "{\"email\":\"$email\"}"

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }
}