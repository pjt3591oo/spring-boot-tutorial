package com.mung.blog.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener::class)
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var title: String,

    var body: String,

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    var comments: MutableList<Comment> = mutableListOf(),

    var createdAt: LocalDateTime  = LocalDateTime.now(),

    var updatedAt: LocalDateTime =  LocalDateTime.now(),
)