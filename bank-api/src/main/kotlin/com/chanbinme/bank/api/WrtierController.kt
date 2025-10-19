package com.chanbinme.bank.api

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/write")
@Tag(name = "Write API", description = "쓰기 전용 API")
class WrtierController {
}