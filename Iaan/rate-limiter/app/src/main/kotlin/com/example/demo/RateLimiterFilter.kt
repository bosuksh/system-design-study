package com.example.demo

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class RateLimiterFilter(
    private val rateLimiter: RateLimiter
    ) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestAllowable = rateLimiter.isRequestAllowable()
        if (!requestAllowable) {
            return
        }
    }


}