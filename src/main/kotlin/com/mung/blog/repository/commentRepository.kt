package com.mung.blog.repository

import com.mung.blog.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface CommentRepository : JpaRepository<Comment, Long> {}