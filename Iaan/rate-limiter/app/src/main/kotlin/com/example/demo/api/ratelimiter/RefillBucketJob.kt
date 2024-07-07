package com.example.demo.api.ratelimiter

import jakarta.annotation.PostConstruct
import org.quartz.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RefillBucketJob : Job {

    @Autowired
    private lateinit var rateLimiter: RateLimiter

    @Autowired
    private lateinit var scheduler: Scheduler


    companion object {

        const val REFILL_PERIOD = 1 // sec
        const val REFILL_SIZE = 2L
    }

    @PostConstruct
    fun startJob() {
        val jobDetail = JobBuilder.newJob(RefillBucketJob::class.java)
            .withIdentity("refillBucketJob").build()

        val trigger: Trigger = TriggerBuilder.newTrigger().withIdentity("refillBucketJob-Trigger")
            .forJob(jobDetail)
            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(REFILL_PERIOD))
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }

    override fun execute(context: JobExecutionContext?) {
        rateLimiter.refill(REFILL_SIZE)
    }

}

