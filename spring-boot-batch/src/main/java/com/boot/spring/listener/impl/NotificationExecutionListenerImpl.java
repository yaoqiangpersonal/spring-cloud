package com.boot.spring.listener.impl;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.integration.annotation.MessagingGateway;

import com.boot.spring.listener.NotificationExecutionListener;

/**
 * 
 * 	尝试 获取 Job执行后的信息
 * @author yaoqiang
 *
 */

public class NotificationExecutionListenerImpl implements NotificationExecutionListener{

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("init");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("end");
		return ExitStatus.FAILED;
	}

}
