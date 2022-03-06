package com.example.learnterm

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findByAccountName(name: String): List<User>
}
