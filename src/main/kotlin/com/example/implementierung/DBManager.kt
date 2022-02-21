package com.example.implementierung

import org.springframework.boot.web.servlet.server.Session
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

lateinit var levels : List<Level>

@Controller
class DBManager(val userRepository: UserRepository, val levelRepository: LevelRepository) {

    @RequestMapping("/verifytest")
    @ResponseBody
    fun verifyTest(session: Session): String? {
        println(session.toString())
        println(session::class.java)
        println("I'm verifying!")
        return "this page is for testing purposes only\n"
    }

    @PostMapping("/verify")
    @ResponseBody
    fun verify(@RequestBody hostName: String): String? {
        val containerID = if (hostName.last() == '=') {
            hostName.dropLast(1)
        } else {
            hostName
        }
        // println(containerID)
        val (user, level) = containerIDMap[containerID]!!
        markCompletedLevel(user, level)
        return "verified container $containerID\n"
    }

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

    fun getLevelByName(name: String): Level {
        return levelRepository.findByName(name)
    }
}
