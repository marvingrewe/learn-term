package com.example.learnterm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LearnTermApplication

fun main(args: Array<String>) {
    runApplication<LearnTermApplication>(*args) {
        // setBannerMode(Banner.Mode.OFF)
    }
}
