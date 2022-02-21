package com.example.implementierung

import org.springframework.data.jpa.repository.JpaRepository

interface LevelRepository : JpaRepository<Level, Int>
