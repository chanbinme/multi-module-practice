package com.chanbinme.bank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankDomainApplication

fun main(args: Array<String>) {
    runApplication<BankDomainApplication>(*args)
}
