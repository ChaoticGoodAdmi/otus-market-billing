package ru.ushakov.billing.domain

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val accountNumber: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    var balance: BigDecimal = BigDecimal.ZERO
) {
    constructor(user: User) : this(0, generateRandomAccountNumber(), user)
    constructor() : this(0, "", User(0, "", "", "", "", ""), BigDecimal.ZERO) {

    }

    companion object {
        fun generateRandomAccountNumber(): String {
            return (1..16).map { (0..9).random() }.joinToString("")
        }
    }
}

enum class TransactionType {
    DEPOSIT, WITHDRAW, INSUFFICIENT_FUNDS
}