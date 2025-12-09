package com.chanbinme.bank.listener

import com.chanbinme.bank.common.TxAdvice
import com.chanbinme.bank.entity.AccountReadView
import com.chanbinme.bank.entity.TransactionReadView
import com.chanbinme.bank.event.AccountCreatedEvent
import com.chanbinme.bank.event.TransactionCreatedEvent
import com.chanbinme.bank.metrics.BankMetrics
import com.chanbinme.bank.repository.AccountReadViewRepository
import com.chanbinme.bank.repository.AccountRepository
import com.chanbinme.bank.repository.TransactionReadViewRepository
import com.chanbinme.bank.repository.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime

@Component
class EventConsumer(
    private val accountReadViewRepository: AccountReadViewRepository,
    private val transactionReadViewRepository: TransactionReadViewRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val metrics: BankMetrics,
    private val txAdvice: TxAdvice
    // TODO -> metrics, txAdvice
) {
    private val logger = LoggerFactory.getLogger(EventConsumer::class.java)

    @EventListener
    @Async("taskExecutor")
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun handleAccountCreated(event: AccountCreatedEvent) {
        // API Main -> Publish (TaskExecutor) -> 실패: RetryProxy -> Method -> RetryProxy(1초 대기) -> Method
        val startTime = Instant.now()
        val eventType = "AccountCreatedEvent"

        logger.info("Received AccountCreatedEvent: $event")

        try {
            txAdvice.runNew {
                val account = accountRepository.findById(event.accountId).orElseThrow {
                    IllegalArgumentException("Account not found with id: ${event.accountId}")
                }

                val accountReadView = AccountReadView(
                    id = account.id,
                    accountNumber = account.accountNumber,
                    accountHolderName = account.accountHolderName,
                    balance = account.balance,
                    createdAt = account.createdAt,
                    lastUpdatedAt = LocalDateTime.now(),
                    transactionCount = 0,
                    totalDeposits = BigDecimal.ZERO,
                    totalWithdrawals = BigDecimal.ZERO
                )

                accountReadViewRepository.save(accountReadView)
                logger.info("Account ${account.id} created")
            }

            val duration = Duration.between(startTime, Instant.now())
            metrics.recordEventProcessingTime(duration, eventType)
            metrics.incrementEventProcessed(eventType)
        } catch (e: Exception) {
            logger.error("Error processing AccountCreatedEvent: $event", e)
            metrics.incrementEventFailed(eventType)
            throw e
        }
    }

    @EventListener
    @Async("taskExecutor")
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun handleTransactionCreated(event: TransactionCreatedEvent) {
        val startTime = Instant.now()
        val eventType = "TransactionCreatedEvent"

        logger.info("event received $eventType")

        try {
            txAdvice.runNew {
                val transaction = transactionRepository.findById(event.transactionId).orElseThrow {
                    IllegalStateException("Transaction with id ${event.transactionId} not found")
                }

                val account = accountRepository.findById(event.accountId).orElseThrow {
                    IllegalStateException("Account with id ${event.accountId} not found")
                }

                val transactionReadView = TransactionReadView(
                    id = transaction.id,
                    accountId = account.id,
                    accountNumber = account.accountNumber,
                    amount = transaction.amount,
                    type = transaction.type,
                    description = transaction.description,
                    createdAt = transaction.createdAt,
                    balanceAfter = transaction.account.balance
                )

                transactionReadViewRepository.save(transactionReadView)
                logger.info("Transaction ${transaction.id} updated")

                val accountReadView = accountReadViewRepository.findById(account.id).orElseThrow {
                    IllegalStateException("AccountReadView with id ${account.id} not found")
                }

                val updatedAccountReadView = accountReadView.copy(
                    balance = account.balance,
                    lastUpdatedAt = LocalDateTime.now(),
                    transactionCount = accountReadView.transactionCount + 1,
                    totalDeposits = if (transaction.type.name.contains("DEPOSIT"))
                        accountReadView.totalDeposits + transaction.amount
                    else accountReadView.totalDeposits,
                    totalWithdrawals = if (transaction.type.name.contains("WITHDRAWAL"))
                        accountReadView.totalWithdrawals + transaction.amount
                    else accountReadView.totalWithdrawals
                )

                accountReadViewRepository.save(updatedAccountReadView)
                logger.info("AccountReadView ${account.id} updated")
            }

            val duration = Duration.between(startTime, Instant.now())
            metrics.recordEventProcessingTime(duration, eventType)
            metrics.incrementEventProcessed(eventType)
        } catch (e: Exception) {
            logger.error("Error processing AccountCreatedEvent: $event", e)
            metrics.incrementEventFailed(eventType)
            throw e
        }
    }
}