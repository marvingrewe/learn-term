package com.example.implementierung

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class StartupRunner(val levelRepository: LevelRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        levels = levelRepository.findAll()
    }
}
