package com.chanbinme.bank.common

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class CircuitBreakerConfig {

    fun circuitBreakerRegstry() : CircuitBreakerRegistry {
        val config = CircuitBreakerConfig.custom()
            .failureRateThreshold(50.0f)    // 실패율 임계값 == 50%
            .waitDurationInOpenState(Duration.ofSeconds(30))    // 오픈 상태 유지 시간 == 30초
            .permittedNumberOfCallsInHalfOpenState(3)   // 하프 오픈 상태에서 허용되는 호출 수 == 3
            .slidingWindowSize(5)               // 슬라이딩 윈도우 크기 == 5
            .minimumNumberOfCalls(3)      // 최소 호출 수 == 3
            .build()

        /*
            1. 최근 5번의 호출 중에서 (slidingWindowSize)
            2. 최소 3번의 호출이 있어야 (minimumNumberOfCalls)
            3. 그 중에서 50% 이상이 실패하면 (failureRateThreshold)
            4. 서킷 브레이커가 오픈 상태로 전환되고, 30초 동안 유지된다. (waitDurationInOpenState)
            5. 이후 서킷 브레이커는 하프 오픈 상태로 전환되어, 3번의 호출을 허용한다. (permittedNumberOfCallsInHalfOpenState)
            6. 이 3번의 호출 중에서 성공률이 높으면 서킷 브레이커는 닫힌 상태로 돌아간다.
         */
        return CircuitBreakerRegistry.ofDefaults()
    }
}

object CircuitBreakerUtils {

    fun <T> CircuitBreaker.execute(
        operation : () -> T,
        fallback : (Exception) -> T
    ): T {
        return try {
            val supplier = CircuitBreaker.decorateSupplier(this) {
                operation()
            }
            supplier.get()
        } catch (e: Exception) {
            fallback(e)
        }
    }
}