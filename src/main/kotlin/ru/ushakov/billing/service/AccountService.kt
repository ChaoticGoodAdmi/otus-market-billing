package ru.ushakov.billing.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ushakov.billing.domain.Account
import ru.ushakov.billing.domain.TransactionType
import ru.ushakov.billing.domain.User
import ru.ushakov.billing.repository.AccountRepository
import ru.ushakov.billing.repository.UserRepository
import java.math.BigDecimal

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val billingEventProducer: BillingEventProducer
) {

    @Transactional
    fun createUserAndAccount(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String
    ): Account {
        val user = User(
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber
        )

        val savedUser = userRepository.save(user)
        val account = Account(user = savedUser)
        return accountRepository.save(account)
    }

    fun getAccount(accountNumber: String): Account? {
        return accountRepository.findByAccountNumber(accountNumber)
    }

    @Transactional
    fun deposit(accountNumber: String, amount: BigDecimal) {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        require(amount > BigDecimal.ZERO) { "Deposit amount must be greater than zero" }

        account.balance = account.balance.add(amount)
        accountRepository.save(account)

        billingEventProducer.sendBillingEvent(account, TransactionType.DEPOSIT, amount)
    }

    @Transactional
    fun withdraw(accountNumber: String, amount: BigDecimal) {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        require(amount > BigDecimal.ZERO) { "Withdrawal amount must be greater than zero" }
        if (account.balance < amount) {
            billingEventProducer.sendBillingEvent(
                account,
                TransactionType.INSUFFICIENT_FUNDS,
                amount
            )
            throw IllegalArgumentException("Insufficient funds")
        }

        account.balance = account.balance.subtract(amount)
        accountRepository.save(account)

        billingEventProducer.sendBillingEvent(account, TransactionType.WITHDRAW, amount)
    }
}