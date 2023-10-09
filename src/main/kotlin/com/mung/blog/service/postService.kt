package com.mung.blog.service

import com.mung.blog.entity.Comment
import com.mung.blog.entity.Post
import com.mung.blog.repository.CommentRepository
import com.mung.blog.repository.PostRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.Persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class PostService (val postRepository: PostRepository, val commentRepository: CommentRepository) {

    fun testSelect(): Boolean {
        val post = postRepository.findAllWithCommentsUsingFetchJoin()
        return true
    }

    fun testCreate(): Boolean {
        val post0 = Post(title = "title0", body = "body0")
        postRepository.save(post0)

        val comment0 = Comment(comment = "comment0", post = post0)
        comment0.post.id = post0.id
        commentRepository.save(comment0)

        post0.comments.add(comment0)
        postRepository.save(post0)

        return true
    }


    @Transactional
    fun testUpdate(): Boolean {
        val id0: Long = 4
        val id1: Long = 7
        val post = postRepository.findById(id0).orElse(null) ?: return false
        val post1 = postRepository.findById(id1).orElse(null) ?: return false

        post.title = Date().toString()
        post.body = Date().toString()
//        postRepository.save(post)

        post1.title = Date().toString()
//        postRepository.save(post)

        return true
    }

    fun testDelete(): Boolean {
        val id: Long = 4
        postRepository.deleteById(id)

        return true
    }
}
        