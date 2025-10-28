package com.chanbinme.bank.exception

abstract class BankingException(
    message: String?, cause: Throwable? = null
) : RuntimeException(message, cause)

class AccountNotFoundException(
    accountNumber: String
) : BankingException("Account with number $accountNumber not found.")