package com.chanbinme.bank.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong

/**
 * 은행 애플리케이션의 다양한 메트릭을 기록하는 클래스
 */
@Component
class BankMetrics(
    private val meterRegistry: MeterRegistry
) {
    private val accountGauge = AtomicLong(0)    // 계좌 수를 저장하는 AtomicLong

    init {
        // 계좌 수를 나타내는 게이지 등록
        meterRegistry.gauge("bank.accounts.count", accountGauge) { it.get().toDouble() }
    }

    fun incrementAccountCreated() {
        Counter.builder("bank.account.created")
            .description("Number of accounts created")
            .register(meterRegistry)
            .increment()

    }

    fun updateAccountCount(count: Long) {
        accountGauge.set(count)
    }

    fun incrementTransaction(type: String) {
        Counter.builder("bank.transaction.count")
            .description("Number of transactions")
            .tag("type", type)
            .register(meterRegistry)
            .increment()
    }

    /**
     * 거래 금액을 기록하는 메서드
     */
    fun recordTransactionAmount(amount: BigDecimal, type: String) {
        DistributionSummary.builder("bank.transaction.amount")
            .description("Transaction amount distribution")
            .tag("type", type)
            .register(meterRegistry)
            .record(amount.toDouble())
    }

    fun recordEventProcessingTime(duration: Duration, eventType: String) {
        Timer.builder("bank.event.processing.time")
            .description("Event processing time")
            .tag("type", eventType)
            .register(meterRegistry)
            .record(duration)
    }

    fun incrementLockAcquisitionFailure(lockKey: String) {
        Counter.builder("bank.lock.acquisition.failed")
            .description("Number of failed lock acquisitions")
            .tag("lock_key", lockKey)
            .register(meterRegistry)
            .increment()
    }

    fun incrementLockAcquisitionSuccess(lockKey: String) {
        Counter.builder("bank.lock.acquisition.success")
            .description("Number of successful lock acquisitions")
            .tag("lock_key", lockKey)
            .register(meterRegistry)
            .increment()
    }

    fun recordApiResponseTime(duration: Duration, endpoint: String, method: String) {
        Timer.builder("bank.api.response.time")
            .description("API response time")
            .tag("endpoint", endpoint)
            .tag("method", method)
            .register(meterRegistry)
            .record(duration)
    }}