package ru.ushakov.billing.domain

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val firstName: String,

    @Column(nullable = false)
    val lastName: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val phoneNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val preferredNotificationMethod: NotificationMethod

) {
    constructor() : this(0, "", "", "", "", "", NotificationMethod.EMAIL)
}

enum class NotificationMethod {
    EMAIL, SMS, PUSH
}
