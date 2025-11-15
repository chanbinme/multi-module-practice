package com.chanbinme.bank.listener

import com.chanbinme.bank.event.AccountCreatedEvent
import com.chanbinme.bank.event.TransactionCreatedEvent
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

@Component
class EventConsumer(
    private val accountReadViewRepository: AccountReadViewRepository,
    private val transactionReadViewRepository: TransactionReadViewRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
    // TODO -> metrics, txAdvice
) {
    private val logger = LoggerFactory.getLogger(EventConsumer::class.java)

    @EventListener
    @Async("taskExecutor")
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun handleAccountCreated(event: AccountCreatedEvent) {
        // API Main -> Publish (TaskExecutor) -> 실패: RetryProxy -> Method -> RetryProxy(1초 대기) -> Method
    }

    @EventListener
    @Async("taskExecutor")
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun handleTransactionCreated(event: TransactionCreatedEvent) {

    }
}