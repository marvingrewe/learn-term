package com.example.implementierung

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ImplementierungApplication

fun main(args: Array<String>) {
    runApplication<ImplementierungApplication>(*args) {
        // setBannerMode(Banner.Mode.OFF)
    }
}
