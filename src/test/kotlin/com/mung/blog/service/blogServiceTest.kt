package com.mung.blog.service

import com.mung.blog.entity.Post
import com.mung.blog.exception.BaseException
import com.mung.blog.exception.BaseResponseCode
import com.mung.blog.repository.PostRepository
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("dev")
@SpringBootTest
@ExtendWith(MockitoExtension::class)
class BlogServiceTests {

    @MockBean
    private lateinit var postRepository: PostRepository

    private lateinit var blogService: BlogService

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            println("setup")
        }

        @AfterAll
        @JvmStatic
        fun teardown() {
            println("teardown")
        }
    }

    @BeforeEach
    fun init() {
        blogService = BlogService(postRepository)
    }

    @Test
    fun testAssertThatHasItems() {
        assertAll(
            { assertThat(listOf("one", "two", "three"), hasItems("one")) },
            { assertThat(listOf("one", "two", "three"), hasItems("one", "three")) }
        );
    }

    @Nested
    inner class read {
        @Test
        @DisplayName("없는 아이디 조회 시 익셉션 발생")
        fun readOnePostToFailure() {
            val id: Long = 1
            Mockito.`when`(postRepository.findById(id)).thenReturn(Optional.empty())

            val exception = assertThrows<BaseException> {
                blogService.readOnePost(id)
            }

            assertEquals(BaseResponseCode.POST_NOT_FOUND, exception.baseResponseCode)
        }

//        @Test
        @RepeatedTest(value=2, name="{displayName} {currentRepetition} / {totalRepetitions} 실행")
        @DisplayName("존재하는 아이디 조회")
        fun readOnePostToSuccess() {
            val id: Long = 1

            Mockito.`when`(postRepository.findById(id)).thenReturn(Optional.of(Post(title="11", body="22")))

            val rst = blogService.readOnePost(id)

            assertEquals("11", rst.title)
            assertEquals("22", rst.body)
        }

        @Test
        @DisplayName("목록 조회")
        fun readAllPost() {
            Mockito.`when`(postRepository.findAll()).thenReturn(listOf(Post(title="title1", body="body1"), Post(title="title2", body="body2")))

            blogService.readAllPost()

            // postRepository의 findAll()이 몇 번 호출되었는지 검사
            // 만약 서비스에서 findAll() 호출 시 인자를 전달한다면 전돨되는 인자를 명시한다
            verify(postRepository, times(1)).findAll()
        }
    }

    @Test
    @DisplayName("추가")
    fun createPost() {
        Mockito.`when`(postRepository.save(Mockito.any<Post>())).thenReturn(Post(title="title1", body="body1"))
        val result = blogService.createPost("title00", "body00")
        assertEquals(result, true)
    }

    @Test
    @DisplayName("존재하지 않는 아이디 업데이트 시도 시 익셉션 발생")
    fun modifyPostToFailure() {
        val id: Long = 1
        Mockito.`when`(postRepository.findById(id)).thenReturn(Optional.empty())

        val exception = assertThrows<BaseException> {
            blogService.modifyPost(id, "title0", "body0")
        }

        assertEquals(BaseResponseCode.POST_NOT_FOUND, exception.baseResponseCode)
    }

    @Test
    @DisplayName("존재하는 아이디 업데이트")
    fun modifyPostToSuccess() {
        val id: Long = 1
        Mockito.`when`(postRepository.findById(id)).thenReturn(Optional.of(Post(title="title1", body="body1")))

        val rst = blogService.modifyPost(id, "title2", "body2")

        assertEquals(rst, true)
    }

    @DisplayName("병렬 테스트 수행")
    @ParameterizedTest
    @ValueSource(strings = ["mung0", "mung1", "mung2"])
    fun match(candidate: String?) {
        assertEquals(candidate, candidate)
    }

    @Test
    fun testByCondition() {
        assumeTrue(true)
    }
}
