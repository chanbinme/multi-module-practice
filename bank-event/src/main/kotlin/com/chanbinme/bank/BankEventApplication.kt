package com.chanbinme.bank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankEventApplication

fun main(args: Array<String>) {
    runApplication<BankEventApplication>(*args)
}
