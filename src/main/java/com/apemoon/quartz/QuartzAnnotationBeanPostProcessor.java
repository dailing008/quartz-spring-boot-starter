package com.apemoon.quartz;

import com.apemoon.quartz.annotation.QuartzScheduled;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 版权：APEMOON
 * 作者：dailing
 * 生成日期：2019-06-29 17:45
 * 描述：
 */
public class QuartzAnnotationBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware, ApplicationRunner {

    private static ApplicationContext applicationContext;

    private static List<QuartzJobModel> jobModels = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        try {
            Arrays.asList(bean.getClass().getDeclaredMethods()).stream().filter(m -> m.getAnnotation(QuartzScheduled.class) != null).forEach(m -> {
                String mname = m.getName();
                String beanMethodName = beanName + StringUtils.capitalize(mname);
                String jobBeanName = beanMethodName;

                QuartzJobModel quartzJobModel = new QuartzJobModel();
                quartzJobModel.setJobName(jobBeanName);
                quartzJobModel.setTargetObject(bean.getClass());
                quartzJobModel.setTargetMethod(m.getName());
                quartzJobModel.setCronExpression(m.getAnnotation(QuartzScheduled.class).cron());
                jobModels.add(quartzJobModel);
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("run jobs");
        if (jobModels!=null && jobModels.size()>0) {
            QuartzService quartzService = applicationContext.getBean(QuartzService.class);
            jobModels.forEach(quartzJobModel -> {
                try {
                    quartzService.addQuartzJob(quartzJobModel);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
