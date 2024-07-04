package com.example.demo

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class TokenBucketRateLimiter(redisTemplate: RedisTemplate<String, String>) : RateLimiter {

    val opsForValue = redisTemplate.opsForValue()

    override fun initialize(size: Int) {
        opsForValue.set("tokenBucket", size.toString())
    }

    @Synchronized
    override fun isRequestAllowable(): Boolean {
        val valueStr = opsForValue.get("tokenBucket")
        val value = valueStr?.toInt() ?: 0

        if (value > 0) {
            opsForValue.decrement("tokenBucket")
            return true
        }

        return false
    }
}