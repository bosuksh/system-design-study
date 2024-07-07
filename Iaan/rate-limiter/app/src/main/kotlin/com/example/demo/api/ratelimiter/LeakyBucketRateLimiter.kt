package com.example.demo.api.ratelimiter

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component


private const val BUCKET_SIZE = 10

@Primary
@Component
class LeakyBucketRateLimiter : RateLimiter {

    private lateinit var queue: MutableList<Int>

    init {
        initialize(BUCKET_SIZE)
    }

    override fun initialize(size: Int) {
        queue = ArrayDeque(size)
    }

    override fun isRequestAllowable(): Boolean {
        if (queue.size >= BUCKET_SIZE) {
            return false
        }

        queue.addLast(0)
        return true
    }

    override fun updateBucket(delta: Long) {
        for (i in 1..delta) {
            if (queue.isEmpty()) return
            queue.removeFirst()
        }
    }
}