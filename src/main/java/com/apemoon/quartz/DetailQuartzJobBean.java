package com.apemoon.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

public class DetailQuartzJobBean implements Job {

	private final static Logger logger = LoggerFactory.getLogger(DetailQuartzJobBean.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

		Class<?> targetObject = (Class<?>) jobDataMap.get("targetObject");
		String targetMethod = jobDataMap.getString("targetMethod");

		Class<?>[] classes = (Class<?>[]) jobDataMap.get("classes");
		Object[] objects = (Object[]) jobDataMap.get("objects");

		try {
			ApplicationContext applicationContext = QuartzAnnotationBeanPostProcessor.getApplicationContext();

			Object otargetObject = applicationContext.getBean(targetObject);
			Method m = otargetObject.getClass().getMethod(targetMethod, classes);
			m.invoke(otargetObject, objects);
		} catch (Exception e) {
			logger.error("执行定时任务失败-", e);
		}
	}
}