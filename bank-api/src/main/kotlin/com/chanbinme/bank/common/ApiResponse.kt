package com.chanbinme.bank.common

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.ResponseEntity

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val err: Error? = null
) {
    companion object {
        fun <T> success(data: T, msg: String = "Success"): ResponseEntity<ApiResponse<T>> {
            return ResponseEntity.ok(ApiResponse(true, msg, data))
        }

        fun <T> error(
            msg: String,
            errCode: String? = null,
            details: Any? = null,
            path: String? = null
        ): ResponseEntity<ApiResponse<T>> {
            val error = Error(errCode, details, path)
            return ResponseEntity.badRequest().body(ApiResponse(false, msg, null, error))
        }

        fun <T> errorException(
            msg: String,
            errCode: String? = null,
            details: Any? = null,
            path: String? = null
        ): ApiResponse<T> {
            val error = Error(errCode, details, path)
            return ApiResponse(false, msg, null, error)
        }
    }
}

data class Error(
    val code: String? = null,
    val details: Any? = null,
    val path: String? = null
)