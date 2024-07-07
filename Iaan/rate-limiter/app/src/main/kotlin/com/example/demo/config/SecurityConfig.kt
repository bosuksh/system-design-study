package com.example.demo.config

import com.example.demo.api.ratelimiter.RateLimiter
import com.example.demo.api.RateLimiterFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.SecurityContextHolderFilter

@Configuration
class SecurityConfig(
    private val rateLimiter: RateLimiter
    ) {

    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {

        http.csrf { csrf -> csrf.disable() }
        http.addFilterBefore(RateLimiterFilter(rateLimiter), SecurityContextHolderFilter::class.java)

        return http.build()
    }
}