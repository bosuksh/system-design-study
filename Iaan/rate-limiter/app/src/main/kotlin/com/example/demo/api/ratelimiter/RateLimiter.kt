package com.example.demo.api.ratelimiter

interface RateLimiter {

    fun initialize(size: Int)

    fun isRequestAllowable(): Boolean

    fun updateBucket(delta : Long)

}