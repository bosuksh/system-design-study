package com.example.demo.api.ratelimiter

import jakarta.annotation.PostConstruct
import org.quartz.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UpdateBucketJob : Job {

    @Autowired
    private lateinit var rateLimiter: RateLimiter

    @Autowired
    private lateinit var scheduler: Scheduler


    companion object {

        const val UPDATE_PERIOD = 1 // sec
        const val UPDATE_SIZE = 2L
    }

    @PostConstruct
    fun startJob() {
        val jobDetail = JobBuilder.newJob(UpdateBucketJob::class.java)
            .withIdentity("refillBucketJob").build()

        val trigger: Trigger = TriggerBuilder.newTrigger().withIdentity("refillBucketJob-Trigger")
            .forJob(jobDetail)
            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(UPDATE_PERIOD))
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }

    override fun execute(context: JobExecutionContext?) {
        rateLimiter.updateBucket(UPDATE_SIZE)
    }

}

