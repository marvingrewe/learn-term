package com.example.learnterm

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.io.*
import java.time.Duration


val config: DockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
    .withApiVersion("1.41")
    .build()

val httpClient: DockerHttpClient = ApacheDockerHttpClient.Builder()
    .dockerHost(config.dockerHost)
    .sslConfig(config.sslConfig)
    .maxConnections(100)
    .connectionTimeout(Duration.ofSeconds(30))
    .responseTimeout(Duration.ofSeconds(45))
    .build()

val dockerClient: DockerClient = DockerClientImpl.getInstance(config, httpClient)

fun createContainer(image: String, name: String): String {
    val result = dockerClient.createContainerCmd(image)
        .withName(name)
        .withTty(true)
        .withStdinOpen(true)
        .exec()
    dockerClient.startContainerCmd(name).exec()
    println(result)
    return result.id.take(12)
}

fun attachToContainer(id: String, currentSession: WebSocketSession) {
    val stdIn = PipedInputStream(currentSession.attributes["pipe"] as PipedOutputStream)
    // val toTerminal = PipedInputStream()
    dockerClient.attachContainerCmd(id)
        .withLogs(true)
        .withStdIn(stdIn)
        .withStdOut(true)
        .withStdErr(true)
        .withFollowStream(true)
        .exec(object : ResultCallback.Adapter<Frame>() {
            override fun onNext(frame: Frame) {
                // requireNotNull(frame)
                // println("Container Output " + System.currentTimeMillis())
                currentSession.sendMessage(TextMessage(frame.payload))
                // println("Output sent      " + System.currentTimeMillis())
            }
        })
}

fun stopContainer(id: String) {
    dockerClient.stopContainerCmd(id)
        .exec()
    dockerClient.removeContainerCmd(id)
        .exec()
}

// fun removeContainer(id: String) {
//     dockerClient.removeContainerCmd(id)
//         .exec()
// }
//
// fun createSecret() {
//     val secretSpec = SecretSpec()
//     println(secretSpec)
//     dockerClient.createSecretCmd(secretSpec)
//         .exec()
// }
//
// fun containerDiff(id: String): String {
//     val changeLog = dockerClient.containerDiffCmd(id).exec()
//     return changeLog.toString()
// }
//
// fun containerArchive(id: String): String {
//     val archive = dockerClient.inspectContainerCmd(id).exec()
//     return archive.path
// }


// fun start(): ResultCallback.Adapter<PushResponseItem?>? {
//     return exec(object : ResultCallback.Adapter<PushResponseItem>() {
//         private var latestItem: PushResponseItem? = null
//         override fun onNext(item: PushResponseItem) {
//             latestItem = item
//         }
//
//         override fun throwFirstError() {
//             super.throwFirstError()
//             if (latestItem == null) {
//                 throw DockerClientException("Could not push image")
//             } else if (latestItem!!.isErrorIndicated) {
//                 throw DockerClientException("Could not push image: " + latestItem!!.error)
//             }
//         }
//     })
// }
