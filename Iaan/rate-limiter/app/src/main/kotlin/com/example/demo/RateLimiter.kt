package com.example.demo

interface RateLimiter {

    fun initialize(size: Int): Unit

    fun isRequestAllowable(): Boolean

}