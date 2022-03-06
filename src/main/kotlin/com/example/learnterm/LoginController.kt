package com.example.learnterm

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ResolvableType
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.function.Consumer


@Controller
class LoginController {
    val oauth2AuthenticationUrls = mutableMapOf<String,String>()

    @Autowired
    private val clientRegistrationRepository: ClientRegistrationRepository? = null
    @GetMapping("/oauth_login")
    fun getLoginPage(model: Model?): String {
        var clientRegistrations: Iterable<ClientRegistration>? = null
        val type = ResolvableType.forInstance(clientRegistrationRepository!!)
            .`as`(Iterable::class.java)
        if (type !== ResolvableType.NONE &&
            ClientRegistration::class.java.isAssignableFrom(type.resolveGenerics()[0])
        ) {
            clientRegistrations = clientRegistrationRepository as Iterable<ClientRegistration>?
        }

        clientRegistrations!!.forEach(Consumer { registration: ClientRegistration ->
            oauth2AuthenticationUrls[registration.clientName] = authorizationRequestBaseUri + "/" + registration.registrationId
        })
        model!!.addAttribute("urls", oauth2AuthenticationUrls)
        return "oauth_login"
    }

    companion object {
        private const val authorizationRequestBaseUri = "oauth2/authorization"
    }
}
