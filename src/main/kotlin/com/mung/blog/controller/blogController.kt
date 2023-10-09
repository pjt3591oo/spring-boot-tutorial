package com.mung.blog.controller

import com.mung.blog.aop.NativeKotlinPerformence
import com.mung.blog.aop.Performance
import com.mung.blog.entity.Post
import com.mung.blog.service.BlogService

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import java.util.*

data class CreatePostReqDto (
    val title: String,
    val body: String
)

@RestController
@RequestMapping("/blog")
class BlogController (val blogService: BlogService) {
    @Value("\${test.a}")
    private val a:Int = 0

    @Value("\${test.b}")
    private val b:String = ""

    @GetMapping("/{idx}")
    fun readOnePost(@PathVariable("idx") idx:Long, @RequestParam("page", required = false, defaultValue = 0.toString()) page: Int): Post {
        return blogService.readOnePost(idx)
    }

    @GetMapping()
    @Performance()
    fun readAllPost(): List<Post> {
        return blogService.readAllPost()
    }

    @PostMapping()
    @Performance()
    fun createPost(@RequestBody() createPostReqDto: CreatePostReqDto) = NativeKotlinPerformence("test") {
        return@NativeKotlinPerformence blogService.createPost(createPostReqDto.title, createPostReqDto.body)
    }

    @PutMapping("/{idx}")
    fun modifyPost(@PathVariable("idx") idx:Long, @RequestBody() createPostReqDto: CreatePostReqDto): Boolean {
        return blogService.modifyPost(idx, createPostReqDto.title, createPostReqDto.body)
    }

    @DeleteMapping("/{idx}")
    fun deletePost(@PathVariable("idx") idx:Long): Boolean {
        return blogService.deletePost(idx)
    }
}