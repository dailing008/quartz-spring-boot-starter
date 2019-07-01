package com.apemoon.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 版权：APEMOON
 * 作者：dailing
 * 生成日期：2019-06-29 17:26
 * 描述：
 */
@Configuration
public class QuartzAnnotationConfiguration {

    @Bean
    public QuartzAnnotationBeanPostProcessor quartzAnnotationBeanPostProcessor() {
        return new QuartzAnnotationBeanPostProcessor();
    }

    @Bean
    public SchedulerFactory schedulerFactory() {
        System.out.println("init SchedulerFactory");
        SchedulerFactory schedFact = new StdSchedulerFactory();
        return schedFact;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactory schedFact) throws SchedulerException {
        System.out.println("init Scheduler");
        Scheduler sched = schedFact.getScheduler();
        sched.start();
        return sched;
    }

    @Bean
    public QuartzService quartzService(Scheduler scheduler) {
        System.out.println("init QuartzService");
        return new QuartzService(scheduler);
    }

}
