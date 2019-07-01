package com.apemoon.quartz;

import lombok.Data;
import org.quartz.*;

/**
 * 定时任务
 * @author dailing
 * 2016-11-17
 */
@Data
public class QuartzService {

	private Scheduler scheduler;

	public QuartzService(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 添加定时任务
	 */
	public void addQuartzJob(QuartzJobModel job) throws SchedulerException {
		String jobName = job.getJobName();//任务名称
		Class<?> targetObject = job.getTargetObject();//执行类名称
		String targetMethod = job.getTargetMethod();//执行方法
		String description = job.getDescription();//任务描述
		String cronExpression = job.getCronExpression();//表达式
		Class<?>[] classes = job.getClasses();
		Object[] objects = job.getObjects();
		
		//初始化JobDetail  
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("targetObject", targetObject);
		dataMap.put("targetMethod", targetMethod);  
		dataMap.put("classes", classes);
		dataMap.put("objects", objects);

		JobDetail jobDetail = JobBuilder.newJob(DetailQuartzJobBean.class)
				.withIdentity(jobName, Scheduler.DEFAULT_GROUP).withDescription(description)
				.usingJobData(dataMap).build();  

		//初始化CronTrigger  
		CronTrigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(jobName + "_trigger", Scheduler.DEFAULT_GROUP)
				.forJob(jobDetail).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();

		//删除旧的定时任务
		deleteQuartzJob(jobName);

		//添加cornjob
		scheduler.scheduleJob(jobDetail, trigger);
	}  
	  
	/**
	 * 删除定时任务
	 */
	public void deleteQuartzJob(String jobName) throws SchedulerException {
		scheduler.deleteJob(new JobKey(jobName, Scheduler.DEFAULT_GROUP));
	}
	
}
