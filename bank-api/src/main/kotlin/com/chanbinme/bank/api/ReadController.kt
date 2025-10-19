package com.chanbinme.bank.api

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/read")
@Tag(name = "Read API", description = "읽기 전용 API")
class ReadController {

    private val logger = LoggerFactory.getLogger(ReadController::class.java)

    @GetMapping("/{accountNumber}")
    fun getAccount(
        @Parameter(description = "Account number",  required = true)
        @PathVariable accountNumber: String
    ) {
        logger.info("Getting account $accountNumber")
        // 여기에 계좌 정보를 조회하는 로직을 추가합니다.
    }
}