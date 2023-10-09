package com.mung.blog.controller

import com.mung.blog.aop.Performance
import com.mung.blog.entity.Comment
import com.mung.blog.entity.Post
import com.mung.blog.service.PostService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/post")
class PostController(val postService: PostService) {
    @GetMapping("test/select")
    @Performance()
    fun testSelect(): Boolean {
        return postService.testSelect()
    }

    @PostMapping("test/create")
    @Performance()
    fun testCreate(): Boolean {
        return postService.testCreate()
    }

    @PutMapping("test/update")
    @Performance()
    fun testUpdate(): Boolean {
        return postService.testUpdate()
    }

    @DeleteMapping("test/delete")
    @Performance()
    fun testDelete(): Boolean {
        return postService.testDelete()
    }

}