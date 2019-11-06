package com.boot.spring.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * 	以下 two-part example 显示了 listener 如何配置为Gateway发送消息StepExecution events 并 log 将其输出发送到logging-channel-adapter。
 *  	您需要将@IntegrationComponentScan annotation 添加到 configuration 中。
 * 
 * @author yaoqiang
 *
 */
@MessagingGateway(	name = "notificationExecutionsListener",
					defaultRequestChannel = "stepExecutionsChannel",
					defaultReplyTimeout = "2",
					defaultReplyChannel = "stepContinue",
					defaultRequestTimeout = "200")
					
public interface NotificationExecutionListener extends StepExecutionListener {
}
