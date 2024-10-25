package ru.ushakov.billing.service

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.ushakov.billing.domain.Account
import ru.ushakov.billing.domain.NotificationMethod
import ru.ushakov.billing.domain.TransactionType
import java.math.BigDecimal

@Service
class BillingEventProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {

    fun sendBillingEvent(account: Account, transactionType: TransactionType, amount: BigDecimal) {
        val event = BillingEvent(
            accountNumber = account.accountNumber,
            userId = account.user.id,
            username = account.user.username,
            email = account.user.email,
            phoneNumber = account.user.phoneNumber,
            preferredNotificationMethod = account.user.preferredNotificationMethod,
            transactionType = transactionType,
            amount = amount
        )

        kafkaTemplate.send("billing-events", event.toString())
    }
}

data class BillingEvent(
    val accountNumber: String,
    val userId: Long,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val preferredNotificationMethod: NotificationMethod,
    val transactionType: TransactionType,
    val amount: BigDecimal
)