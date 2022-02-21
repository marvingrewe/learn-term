package com.example.implementierung

import org.springframework.web.socket.WebSocketSession
import java.io.PipedOutputStream

val activeSessions = mutableMapOf<WebSocketSession, PipedOutputStream>()
val containerMap = mutableMapOf<String, String>()
