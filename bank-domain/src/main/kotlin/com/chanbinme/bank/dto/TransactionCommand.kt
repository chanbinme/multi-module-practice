package com.chanbinme.bank.dto

import com.chanbinme.bank.entity.TransactionType
import java.math.BigDecimal

data class TransactionCommand(
    val accountNumber: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val description: String
) {
}