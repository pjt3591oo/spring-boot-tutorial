package com.mung.blog.repository

import com.mung.blog.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>

    @Query("SELECT distinct u FROM User u JOIN FETCH u.roles")
    fun readById(id: Long): List<User>
}
