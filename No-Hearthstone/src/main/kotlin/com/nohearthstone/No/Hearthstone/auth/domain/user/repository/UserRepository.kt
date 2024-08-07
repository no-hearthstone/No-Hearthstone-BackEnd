package com.nohearthstone.No.Hearthstone.auth.domain.user.repository


import com.nohearthstone.No.Hearthstone.auth.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<User>
}