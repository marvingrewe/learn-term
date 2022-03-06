package com.example.learnterm

import org.springframework.data.jpa.repository.JpaRepository

interface LevelRepository : JpaRepository<Level, Int> {
    fun findByName(name: String): Level
}
