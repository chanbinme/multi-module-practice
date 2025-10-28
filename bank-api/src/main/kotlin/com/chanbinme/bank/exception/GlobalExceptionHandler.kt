package com.chanbinme.bank.exception

import com.chanbinme.bank.common.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(AccountNotFoundException::class)
    fun handlerAccountNotFound(
        ex: AccountNotFoundException,
        req: WebRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.error("Account not found: ${ex.message}", ex)

        val response = ApiResponse.errorException<Nothing>(
            msg = ex.message ?: "Account not found",
            errCode = "ACCOUNT_NOT_FOUND",
            path = getPath(req)
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    private fun getPath(req: WebRequest): String {
        return req.getDescription(false).removePrefix("uri=")
    }
}