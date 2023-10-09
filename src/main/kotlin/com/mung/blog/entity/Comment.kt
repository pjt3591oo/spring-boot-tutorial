package com.mung.blog.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@Entity
@Table(name = "comment")
@EntityListeners(AuditingEntityListener::class)
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var comment: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    @JsonBackReference
    var post: Post,

    var createdAt: LocalDateTime  = LocalDateTime.now(),

    var updatedAt: LocalDateTime =  LocalDateTime.now(),
)
