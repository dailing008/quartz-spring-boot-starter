package com.apemoon.quartz;

import lombok.Data;

import java.io.Serializable;

/**
 * 定时器
 * @author dailing
 * 2016-04-12
 */
@Data
public class QuartzJobModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//定时器名称
	String schedName;
	
	//任务名称
	String jobName;
	
	//描述
	String description;
	
	//任务执行类名称
	Class<?> targetObject;
	
	//执行方法
	String targetMethod;
	
	//定时器表达式
	String cronExpression;
	
	Class<?>[] classes;
	
	Object[] objects;

}
