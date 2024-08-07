package com.nohearthstone.No.Hearthstone.auth.domain.auth.service

import com.nohearthstone.No.Hearthstone.auth.domain.auth.controller.dto.request.UserJoinRequest
import com.nohearthstone.No.Hearthstone.auth.domain.user.entity.Role
import com.nohearthstone.No.Hearthstone.auth.domain.user.entity.User
import com.nohearthstone.No.Hearthstone.auth.domain.user.repository.UserRepository
import com.nohearthstone.No.Hearthstone.auth.global.exption.HttpException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun joinMember(userJoinRequest: UserJoinRequest) {
        if (userRepository.existsByEmail(userJoinRequest.email)) {
            throw HttpException(HttpStatus.BAD_REQUEST, "이미 해당 이메일을 사용하는 사용자가 존재합니다.")
        }

        val encodedPassword = passwordEncoder.encode(userJoinRequest.password)
        val user = User(
            id = UUID.randomUUID(),
            email = userJoinRequest.email,
            password = encodedPassword,
            role = Role.ROLE_USER,
        )

        userRepository.save(user)
    }
}