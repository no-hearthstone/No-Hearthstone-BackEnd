package com.nohearthstone.No.Hearthstone.auth.global.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.nohearthstone.No.Hearthstone.auth.global.security.dto.ExceptionResponse
import com.nohearthstone.No.Hearthstone.auth.global.utill.getLogger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler (
    private val objectMapper: ObjectMapper
): AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val forbidden = HttpStatus.FORBIDDEN

        val responseString = objectMapper.writeValueAsString(
            ExceptionResponse(forbidden.value(), "해당 엔드포인트에 대한 권한이 없습니다.")
        )

        getLogger().error("CustomAccessDeniedHandler: {}", responseString)

        response.characterEncoding = "UTF-8"
        response.status = forbidden.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(responseString)
    }
}