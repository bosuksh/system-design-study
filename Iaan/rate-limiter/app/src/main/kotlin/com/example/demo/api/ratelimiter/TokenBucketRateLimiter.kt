package com.example.demo.api.ratelimiter

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class TokenBucketRateLimiter(redisTemplate: RedisTemplate<String, String>) : RateLimiter {

    val opsForValue = redisTemplate.opsForValue()

    companion object {
        const val BUCKET_SIZE = 10
    }

    init {
        initialize(BUCKET_SIZE)
    }


    final override fun initialize(size: Int) {
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

    override fun refill(delta: Long) {
        val value = opsForValue.get("tokenBucket")?.toInt() ?: return

        if (value + delta >= BUCKET_SIZE) {
            opsForValue.set("tokenBucket", "$BUCKET_SIZE")
            return
        }

        opsForValue.increment("tokenBucket", delta)
    }
}