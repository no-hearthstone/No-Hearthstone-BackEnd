package com.nohearthstone.No.Hearthstone.auth.domain.auth.controller

import com.nohearthstone.No.Hearthstone.auth.domain.auth.controller.dto.request.UserJoinRequest
import com.nohearthstone.No.Hearthstone.auth.domain.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController (
    private val authService: AuthService
){
    @PostMapping("/join")
    fun joinMember(@RequestBody @Valid userJoinRequest: UserJoinRequest): ResponseEntity<Void> {
        authService.joinMember(userJoinRequest)
        return ResponseEntity.ok().build()
    }
}