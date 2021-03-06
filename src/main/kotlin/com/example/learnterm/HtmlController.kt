package com.example.learnterm

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*


@Controller
class HtmlController(val dbManager: DBManager) {

    @GetMapping("/")
    fun default(): String {
        return "../static/index"
    }

    @GetMapping("/levels")
    fun getLevels(model: Model, authentication: Authentication): String {
        check(authentication is OAuth2AuthenticationToken) { "authentication is not OAuth2AuthenticationToken" }
        val attributes = authentication.principal.attributes
        model.addAttribute("user", attributes["name"].toString())
        val levelMap = dbManager.getAllLevels().associate { it.levelID.toString() to it.name }
        model.addAttribute("levels", levelMap)
        return "levels"
    }

    @GetMapping("/levels/{levelName}")
    fun blog(model: Model, authentication: Authentication, @PathVariable levelName: String): String {
        check(authentication is OAuth2AuthenticationToken) { "authentication is not OAuth2AuthenticationToken" }
        val attributes = authentication.principal.attributes
        // println("authentication name: ${authentication.name}")   // e.g. 88387746
        // println(authentication)  // OAuth2AuthenticationToken, alle Informationen
        // println("authentication principal: ${authentication.principal::class.java}") // DefaultOAuth2User
        // model["title"] = levelName
        model.addAttribute("title", levelName)
        // val userName = attributes["name"].toString()
        // model["user"] = userName
        // model.addAttribute("user", userName)

        val accountName = attributes["login"].toString()

        val containerName = levelName + attributes["login"]
        containerMap[authentication.name] = containerName
        // println(levels)

        // TODO: maybe validate levelID
        val containerID = createContainer(levelName, containerName)
        var user = dbManager.getUserByAccountName(accountName)
        if (user == null) {
            user = dbManager.createUser(attributes["name"] as String, attributes["email"] as String, attributes["login"] as String)
        }
        val level = dbManager.getLevelByName(levelName)
        containerIDMap[containerID] = user to level

        return "terminal"
    }

    @PostMapping("/verify")
    @ResponseBody
    fun verify(@RequestBody hostName: String): String? {
        val containerID = hostName.take(12)
        // println(containerID)
        val (user, level) = containerIDMap[containerID]!!
        dbManager.markCompletedLevel(user, level)
        return "verified container $containerID\n"
    }

    // @RequestMapping("/verifytest")
    // @ResponseBody
    // fun verifyTest(session: Session): String? {
    //     println(session.toString())
    //     println(session::class.java)
    //     println("I'm verifying!")
    //     return "this page is for testing purposes only\n"
    // }
    //
    // @RequestMapping(value = ["/username"], method = [RequestMethod.GET])
    // @ResponseBody
    // fun currentUserName(principal: Principal): String? {
    //     return principal.name
    // }

    // @GetMapping("/secret")
    // fun secret() {
    //     createSecret()
    // }
    //
    // @GetMapping("/container")
    // fun container(model: Model) {
    //     createContainer("mvp", "hallihallo")
    // }
    //
    // @GetMapping("/attach")
    // fun attach(model: Model) {
    //     // attachToContainer("hallihallo", ByteArrayInputStream(ByteArray(1048576)))
    // }
    //
    // @GetMapping("/changes")
    // @ResponseBody
    // fun changes(model: Model): String {
    //     return containerDiff("hallihallo")
    // }
    //
    // @GetMapping("/archive")
    // @ResponseBody
    // fun archive(model: Model): String {
    //     return containerArchive("hallihallo")
    // }
    //
    // @GetMapping("/test")
    // @ResponseBody()
    // fun terminal(model: Model): String {
    //
    //     val config: DockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
    //         .withApiVersion("1.41")
    //         .build()
    //
    //     println(config.dockerHost.toString())
    //
    //     val httpClient: DockerHttpClient = ApacheDockerHttpClient.Builder()
    //         .dockerHost(config.dockerHost)
    //         .sslConfig(config.sslConfig)
    //         .maxConnections(100)
    //         .connectionTimeout(Duration.ofSeconds(30))
    //         .responseTimeout(Duration.ofSeconds(45))
    //         .build()
    //
    //     println(httpClient.toString())
    //
    //     val dockerClient = DockerClientImpl.getInstance(config, httpClient)
    //
    //     val containers = dockerClient.listContainersCmd()
    //         .withStatusFilter(mutableListOf("running")).exec().firstOrNull()?.id
    //     println(containers)
    //
    //
    //     val result = html {
    //         head {
    //             title { +"xTerm + Docker test" }
    //         }
    //         body {
    //             h1 { +"xTerm + Docker test" }
    //             p {
    //                 +"Dies ist eine Testumgebung"
    //             }
    //
    //             a(href = "http://kotlinlang.org") { +"Kotlin" }
    //
    //             // mixed content
    //             p {
    //                 +"This is some"
    //                 b { +"mixed" }
    //                 +"text. For more see the"
    //                 a(href = "http://kotlinlang.org") {
    //                     +"Kotlin"
    //                 }
    //                 +"project"
    //             }
    //             p {
    //                 ul {
    //                     for (i in 1..5)
    //                         li { +"${i}*2 = ${i * 2}" }
    //                 }
    //             }
    //         }
    //     }
    //
    //     return result.toString()
    // }
}
