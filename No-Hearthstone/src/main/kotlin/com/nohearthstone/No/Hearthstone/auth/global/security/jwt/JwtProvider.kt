package com.nohearthstone.No.Hearthstone.auth.global.security.jwt

import io.jsonwebtoken.*
import com.nohearthstone.No.Hearthstone.auth.domain.auth.controller.dto.response.UserLoginResponse
import com.nohearthstone.No.Hearthstone.auth.global.exption.HttpException
import com.nohearthstone.No.Hearthstone.auth.global.security.dto.TokenType
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

import java.util.Base64
import java.util.Date
import java.util.UUID

@Component
class JwtProvider(
    private val userDetailsService: UserDetailsService,

    @Value("\${spring.jwt.access-key}") private val accessKey: String,
    @Value("\$(spring.jwt.access-expired") private val accessExpireTime: Long,
    @Value("\${spring.jwt.refresh-expired}") private val refreshExpireTime: Long
) {
    fun generateToken(id: UUID): UserLoginResponse {
        return UserLoginResponse(
            generateToken(id, TokenType.ACCESS_TOKEN),
            generateToken(id, TokenType.REFRESH_TOKEN)
        )
    }

    fun generateToken(id: UUID, tokenType: TokenType): String {
        val isAccessToken = tokenType == TokenType.ACCESS_TOKEN

        val expired = if (isAccessToken) accessExpireTime else refreshExpireTime
        val keyBytes = Base64.getEncoder().encode(accessKey.encodeToByteArray())
        val signingKey = Keys.hmacShaKeyFor(keyBytes)

        return Jwts.builder()
            .signWith(signingKey)
            .subject(id.toString())
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis().plus(expired)))
            .compact()
    }

    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val userDetails = userDetailsService.loadUserByUsername(token)
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    private fun getTokenSubject(subject: String?): String {
        return ""
    }

    fun getClaims(token: String?): Claims {
        if (token == null) {
            throw HttpException(HttpStatus.UNAUTHORIZED, "토큰이 없습니다")
        }

        val keyBytes = Base64.getEncoder().encode(accessKey.encodeToByteArray())
        val signingKey = Keys.hmacShaKeyFor(keyBytes)

        try {
            return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            throw HttpException(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            throw HttpException(HttpStatus.FORBIDDEN, "형식이 일치하지 않는 토큰입니다.")
        } catch (e: MalformedJwtException) {
            throw HttpException(HttpStatus.FORBIDDEN, "올바르지 않은 구성의 토큰입니다.")
        } catch (e: RuntimeException) {
            throw HttpException(HttpStatus.FORBIDDEN, "알 수 없는 토큰입니다.")
        }
    }

    fun validateToken(token: String?): Boolean {
        val keyBytes = Base64.getEncoder().encode(accessKey.encodeToByteArray())
        val signingKey = Keys.hmacShaKeyFor(keyBytes)

        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .expiration
            .before(Date())
    }
}