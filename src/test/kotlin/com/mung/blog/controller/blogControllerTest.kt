package com.mung.blog.controller

import com.mung.blog.entity.Post
import com.mung.blog.service.BlogService
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath


@ActiveProfiles("dev")
@WebMvcTest(BlogController::class)
@ExtendWith(MockitoExtension::class)
class BlogControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var blogService: BlogService

    @Test
    @DisplayName("목록조회")
    fun readAllPost() {
        Mockito.`when`(blogService.readAllPost()).thenReturn(listOf(Post(title="11", body="22"), Post(title="111", body="222")))

        mockMvc.perform(MockMvcRequestBuilders.get("/blog"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2))) // 배열 크기 검사
            .andExpect(jsonPath("$[0].title").value("11")) // 첫 번째 항목의 title 검사
            .andExpect(jsonPath("$[0].body").value("22")) // 첫 번째 항목의 body 검사
            .andExpect(jsonPath("$[1].title").value("111")) // 두 번째 항목의 title 검사
            .andExpect(jsonPath("$[1].body").value("222")) // 두 번째 항목의 body 검사
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("400")
    fun createPost() {
        val requestJson = "{\"title\":\"title0\"}"
        mockMvc.perform(
            MockMvcRequestBuilders.post("/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andDo(MockMvcResultHandlers.print())
    }
}
