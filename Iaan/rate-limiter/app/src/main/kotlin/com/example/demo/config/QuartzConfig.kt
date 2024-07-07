package com.example.demo.config

import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SpringBeanJobFactory

@Configuration
class QuartzConfig {

    @Bean
    fun schedulerFactoryBean(context: ApplicationContext): SchedulerFactoryBean {
        val schedulerFactoryBean = SchedulerFactoryBean()

        val jobFactory = AutowiringSpringBeanJobFactory()
        jobFactory.setApplicationContext(context)
        schedulerFactoryBean.setJobFactory(jobFactory)
        return schedulerFactoryBean
    }

    companion object {
        class AutowiringSpringBeanJobFactory : SpringBeanJobFactory(), ApplicationContextAware {
            private var beanFactory: AutowireCapableBeanFactory? = null

            override fun setApplicationContext(context: ApplicationContext) {
                beanFactory = context.autowireCapableBeanFactory
            }

            /*
            * super 를 통해 job instance 를 만들고, job 을 beanFactory 에 주입시켜줌.
            * 그렇게 함으로써 만들어진 Job 이 application Context 의 bean 에 접근 가능
            * */
            @Throws(Exception::class)
            override fun createJobInstance(bundle: TriggerFiredBundle): Any {
                val job = super.createJobInstance(bundle)
                beanFactory!!.autowireBean(job)
                return job
            }
        }
    }
}