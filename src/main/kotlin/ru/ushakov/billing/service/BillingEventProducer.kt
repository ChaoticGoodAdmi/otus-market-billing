package ru.ushakov.billing.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.ushakov.billing.domain.Account
import ru.ushakov.billing.domain.TransactionType
import java.math.BigDecimal

@Service
class BillingEventProducer(@Suppress("SpringJavaInjectionPointsAutowiringInspection") private val kafkaTemplate: KafkaTemplate<String, String>) {

    val objectMapper: ObjectMapper = ObjectMapper()

    fun sendBillingEvent(account: Account, transactionType: TransactionType, amount: BigDecimal) {
        val event = BillingEvent(
            accountNumber = account.accountNumber,
            phoneNumber = account.user.phoneNumber,
            transactionType = transactionType,
            amount = amount
        )

        kafkaTemplate.send("billing-events", objectMapper.writeValueAsString(event))
    }
}

data class BillingEvent(
    val accountNumber: String,
    val phoneNumber: String,
    val transactionType: TransactionType,
    val amount: BigDecimal
)