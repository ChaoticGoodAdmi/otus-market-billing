package ru.ushakov.billing.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.ushakov.billing.domain.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}