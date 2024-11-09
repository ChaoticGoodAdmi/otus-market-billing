package ru.ushakov.billing.controller

import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ushakov.billing.domain.Account
import ru.ushakov.billing.service.AccountService
import java.math.BigDecimal

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @PostMapping
    fun createUserAndAccount(
        @RequestBody request: CreateUserAccountRequest
    ): ResponseEntity<Account> {
        val account = accountService.createUserAndAccount(
            username = request.username,
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            phoneNumber = request.phoneNumber
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(account)
    }

    @GetMapping("/{accountNumber}")
    fun getAccount(@PathVariable accountNumber: String): ResponseEntity<Any> {
        return getAccountForResponse(accountNumber)
    }

    @PutMapping("/{accountNumber}/deposit")
    fun deposit(@PathVariable accountNumber: String, @RequestParam amount: BigDecimal): ResponseEntity<Any> {
        accountService.deposit(accountNumber, amount)
        return getAccountForResponse(accountNumber)
    }

    @PutMapping("/{accountNumber}/withdraw")
    fun withdraw(@PathVariable accountNumber: String, @RequestParam amount: BigDecimal): ResponseEntity<Any> {
        accountService.withdraw(accountNumber, amount)
        return getAccountForResponse(accountNumber)
    }

    private fun getAccountForResponse(accountNumber: String): ResponseEntity<Any> =
        accountService.getAccount(accountNumber)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
}

data class CreateUserAccountRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String
)