package com.chanbinme.bank.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account,

    @Column(nullable = false)
    val amount: java.math.BigDecimal,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: TransactionType,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER
}