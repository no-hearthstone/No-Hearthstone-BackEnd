package com.nohearthstone.No.Hearthstone.auth.global.exption

import org.springframework.http.HttpStatus

class HttpException (
    val statusCode: HttpStatus,
    override val message: String
) : RuntimeException(){
    override fun fillInStackTrace(): Throwable {
        return this
    }
}