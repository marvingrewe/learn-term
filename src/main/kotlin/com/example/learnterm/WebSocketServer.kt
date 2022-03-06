package com.example.learnterm

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
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
    // var toContainer = PipedOutputStream()
    // var fromContainer = PipedInputStream()

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
        // println("session class: ${session::class.java}") // StandardWebSocketSession
        // println("session principal class: ${session.principal!!::class.java}")   // OAuth2AuthenticationToken
        // println("connection established")
        // println("session principal: ${session.principal}")   // OAuth2AuthenticationToken, alle Informationen
        // println("session id: ${session.id}") // e.g. 48883f1a-f783-3335-b79f-39d93794f4e7
        // println("principal name at websocket: ${session.principal?.name ?: "no name"}")  // e.g. 88387746
        // val authentication: Authentication = SecurityContextHolder.getContext().authentication
        // val currentPrincipalName: String = authentication.name
        // println("principal name: $currentPrincipalName")
        val toContainer = PipedOutputStream()
        val currentSession = session to toContainer
        session.attributes["pipe"] = toContainer
        // println(session.attributes.keys)
        containerMap[session.principal?.name]?.let { attachToContainer(it, session) }
    }

    // TODO: this
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val containerName = containerMap[session.principal?.name]!!
        (session.attributes["pipe"] as PipedOutputStream).close()
        stopContainer(containerName)
        containerMap.remove(session.principal?.name)
        // removeContainer(containerName)
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
