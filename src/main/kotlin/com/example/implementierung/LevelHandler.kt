package com.example.implementierung

import org.springframework.boot.web.servlet.server.Session
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

lateinit var levels : List<Level>

@Controller
class LevelHandler {

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
        val name = if (hostName.last() == '=') {
            hostName.dropLast(1)
        } else {
            hostName
        }
        println(name)
        return "Hello $name\n"
    }
}
