package com.example.implementierung

import org.springframework.boot.web.servlet.server.Session
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

lateinit var levels : List<Level>

@Controller
class DBManager(val userRepository: UserRepository, val levelRepository: LevelRepository) {



    @Transactional
    fun markCompletedLevel(user: User, level: Level) {
        /*user.completedLevels.add(level)
        userRepository.save(user)

         */
        println("completed level $level as user $user")
    }

    fun getUserByAccountName(name : String): User? {
        return userRepository.findByAccountName(name).firstOrNull()
    }

    fun createUser(name: String, email: String, login: String): User {
        val user = User(name, email, login)
        userRepository.save(user)
        return user
    }

    fun getAllLevels(): List<Level> {
        return levelRepository.findAll()
    }

    fun getLevelByName(name: String): Level {
        return levelRepository.findByName(name)
    }
}
