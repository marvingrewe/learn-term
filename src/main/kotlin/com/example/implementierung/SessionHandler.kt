package com.example.implementierung

import org.springframework.web.socket.WebSocketSession
import java.io.PipedOutputStream

val activeSessions = mutableMapOf<WebSocketSession, PipedOutputStream>()
val userSessions = mutableMapOf<AuthenticationName, User>()
val containerMap = mutableMapOf<AuthenticationName, ContainerName>()
val containerIDMap = mutableMapOf<ContainerID, Pair<User, Level>>()

typealias ContainerName = String
typealias ContainerID = String
typealias AuthenticationName = String
