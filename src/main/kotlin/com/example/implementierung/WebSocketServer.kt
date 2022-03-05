package com.example.implementierung

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.io.*


@Configuration
@EnableWebSocket
class WebSocketServerConfiguration(@Autowired private val wsHandler: MyWebSocketHandler) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(wsHandler, "/terminal")
            // .addInterceptors(MyHandShakeInterceptor())
    }

}

@Component
class MyWebSocketHandler : TextWebSocketHandler() {
    var toContainer = PipedOutputStream()
    var fromContainer = PipedInputStream()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        // println("Container Input  " + System.currentTimeMillis())
        (session.attributes["pipe"] as PipedOutputStream).write(message.payload.toByteArray())
        // println("Input sent       " + System.currentTimeMillis())
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println(exception.localizedMessage)
    }

    // TODO: this
    override fun afterConnectionEstablished(session: WebSocketSession) {
        // check(session.principal is OAuth2User)
        println("session class: ${session::class.java}")
        println("session principal class: ${session.principal!!::class.java}")
        println("connection established")
        println("session principal: ${session.principal}")
        println("session id: ${session.id}")
        println("principal name at websocket: ${session.principal?.name ?: "no name"}")
        println("hallo! hihihahahohoho")
        // val authentication: Authentication = SecurityContextHolder.getContext().authentication
        // val currentPrincipalName: String = authentication.name
        // println("principal name: $currentPrincipalName")
        toContainer = PipedOutputStream()
        val currentSession = session to toContainer
        session.attributes["pipe"] = toContainer
        // println(session.attributes.keys)
        attachToContainer(containerMap[session.principal?.name]!!, session)
    }

    // TODO: this
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val containerName = containerMap[session.principal?.name]!!
        stopContainer(containerName)
        containerMap.remove(session.principal?.name)
        removeContainer(containerName)
        println("connection closed with status $status")
    }
}

/*
class MyHandShakeInterceptor : HandshakeInterceptor {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        println("before handshake: ${response.headers}")
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        println("after handshake: ${response.body}")
    }

}
 */
