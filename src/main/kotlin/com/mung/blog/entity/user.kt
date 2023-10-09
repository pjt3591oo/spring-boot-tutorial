package com.mung.blog.entity

import jakarta.persistence.*

enum class UserRole {
    ADMIN, USER
}

@Entity
@Table(name = "user")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var email: String,
    var password: String,

    @Enumerated(EnumType.STRING)
//    @ElementCollection(fetch = FetchType.EAGER)
    @ElementCollection(fetch = FetchType.LAZY)
    var roles: MutableSet<UserRole>? = null,
)