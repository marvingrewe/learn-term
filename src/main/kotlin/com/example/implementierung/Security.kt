package com.example.implementierung

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import javax.ws.rs.POST


@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST,"/verify").permitAll()
            .antMatchers(HttpMethod.GET,"/oauth_login", "/verifytest").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
            .loginPage("/oauth_login")
    }
}
