package com.chanbinme.bank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.chanbinme.bank"])
class BankApiApplication

fun main(args: Array<String>) {
    runApplication<BankApiApplication>(*args)
}
