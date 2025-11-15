package com.chanbinme.bank.common

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * 이 클래스를 만든 이유는 @Transactional 어노테이션이 같은 클래스 내의 메서드 호출에는 적용되지 않기 때문이다.
 * 따라서 트랜잭션이 필요한 코드를 별도의 컴포넌트로 분리하여 트랜잭션 경계를 명확히 한다.
 * 트랜잭션이 필요한 코드 블록을 람다로 받아서 실행하는 방식을 사용한다.
 * 트랜잭션 전파 속성도 메서드별로 다르게 지정할 수 있다.
 * 예를 들어, 기본 트랜잭션, 읽기 전용 트랜잭션, 새로운 트랜잭션을 각각 지원한다.
 * 트랜잭션이 필요한 서비스 클래스에서는 이 TransactionRunner 인터페이스를 주입받아 사용하면 된다.
 */
interface TransactionRunner {
    fun <T> run(func: () -> T?): T?    // @Transactional
    fun <T> readOnly(func: () -> T?): T?  // @Transactional(readOnly = true)
    fun <T> runNew(func: () -> T?): T?  // @Transactional(propagation = Propagation.REQUIRES_NEW)
}

@Component
class TransactionAdvice : TransactionRunner {
    @Transactional
    override fun <T> run(func: () -> T?): T? = func()

    @Transactional(readOnly = true)
    override fun <T> readOnly(func: () -> T?): T? = func()

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun <T> runNew(func: () -> T?): T? = func()
}

@Component
class TxAdvice(
    private val advice: TransactionAdvice
) {
    fun <T> run(func: () -> T?): T? = advice.run(func)
    fun <T> readOnly(func: () -> T?): T? = advice.readOnly(func)
    fun <T> runNew(func: () -> T?): T? = advice.runNew(func)
}