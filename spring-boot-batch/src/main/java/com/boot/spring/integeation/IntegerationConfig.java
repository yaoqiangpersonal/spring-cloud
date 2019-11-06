package com.boot.spring.integeation;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;;

@Configuration
public class IntegerationConfig {
	
	@Autowired
	@Qualifier("partitionJob")
	private Job job;
	
	@Bean
	public MessageChannel re() {
		return new PublishSubscribeChannel();
	}
	
	@Bean
	public PollableChannel exeQueue() {
		return new QueueChannel();
	}
	
	@Bean
	public MessageChannel stepExecutionsChannel() {
		return new PublishSubscribeChannel();
	}
	
	@Bean
	public MessageChannel stepContinue() {
		return new PublishSubscribeChannel();
	}
	
	/**
	 * 
	 * 	接收来自 StepExecutionListener 的消息
	 * 
	 * @return
	 */
	@Bean
	@ServiceActivator(inputChannel = "stepExecutionsChannel")
	public LoggingHandler loggingHandler() {
	    LoggingHandler adapter = new LoggingHandler(LoggingHandler.Level.WARN);
	    adapter.setLoggerName("TEST_LOGGER");
	    adapter.setLogExpressionString("headers.id + ': ' + payload");
	    return adapter;
	}
	
	
	
	@Bean
	public FileMessageToJobRequest fileMessageToJobRequest() {
	    FileMessageToJobRequest fileMessageToJobRequest = new FileMessageToJobRequest();
	    fileMessageToJobRequest.setFileParameterName("fielName");
	    fileMessageToJobRequest.setJob(job);
	    return fileMessageToJobRequest;
	}

	/**
	 * 轮询通道
	 * 
	 * @param jobLauncher
	 * @return
	 */
	@Bean
	@ServiceActivator(inputChannel = "exeQueue",poller = {@Poller(fixedRate="1000")})
	public JobLaunchingGateway jobLaunchingGateway(JobLauncher jobLauncher) {
		((SimpleJobLauncher)jobLauncher).setTaskExecutor(new SyncTaskExecutor());
	    JobLaunchingGateway jobLaunchingGateway = new JobLaunchingGateway(jobLauncher);
	    //将结果发送至通道
	    jobLaunchingGateway.setOutputChannel(re());
	    return jobLaunchingGateway;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "re")
	public MessageHandler m() {
		
	    return m->{
	    	System.out.println("re:" + m.getHeaders());
	    	System.out.println("re:" + m.getPayload());
	    };
	}


	/**
	 * 
	 * 读取文件
	 * 流程设置
	 * 
	 * @return
	 */
	@Bean
	public IntegrationFlow integrationFlow() {
		
	    return IntegrationFlows.from(Files.inboundAdapter(new File("C:\\Users\\yaoqiang\\Desktop\\123")).
	                    filter(new SimplePatternFileListFilter("*.csv")),
	            c -> c.poller(Pollers.fixedRate(10000).maxMessagesPerPoll(1)))
	    		//包装成request准备出战
	            .handle(fileMessageToJobRequest())
	            //发送至通道,也可以直接使用getaway
	            .channel(exeQueue())
	            .log(LoggingHandler.Level.WARN, "headers.id + ': ' + payload").
	            get();
	}

}
