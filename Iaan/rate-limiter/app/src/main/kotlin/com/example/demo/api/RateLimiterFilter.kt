package com.example.demo.api

import com.example.demo.api.ratelimiter.RateLimiter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
            response.contentType = MediaType.TEXT_PLAIN_VALUE
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            val writer = response.writer
            writer.write("Blocked!!")
            writer.flush()
            return
        }

        filterChain.doFilter(request, response)
    }


}