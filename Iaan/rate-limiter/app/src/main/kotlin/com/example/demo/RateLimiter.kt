package com.example.demo

interface RateLimiter {

    fun initialize(size: Int)

    fun isRequestAllowable(): Boolean

}