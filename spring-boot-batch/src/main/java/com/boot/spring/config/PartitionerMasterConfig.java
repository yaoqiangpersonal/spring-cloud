package com.boot.spring.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.RemoteStepExecutionAggregator;
import org.springframework.batch.core.partition.support.SimplePartitioner;
import org.springframework.batch.core.partition.support.StepExecutionAggregator;
import org.springframework.batch.integration.partition.MessageChannelPartitionHandler;
import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.AggregatorFactoryBean;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
public class PartitionerMasterConfig {

    @Autowired
    private RemotePartitioningManagerStepBuilderFactory masterStepBuilderFactory;

    
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
    
	@Bean(name=PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata pollerMetadata() {
		return Pollers.fixedDelay(30000).get();
	}
	
    /*
     * Configure outbound flow (requests going to workers)
     */
    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }
	
    /*
     * Configure inbound flow (replies coming from workers)
     */
    @Bean
    public QueueChannel replies() {
        return new QueueChannel();
    }
    
	@Bean
	public Job partitionJob() {
		return jobBuilderFactory
				.get("partitionJob")
				.start(masterStep())
				.build();		
	}


    @Bean
    public Step masterStep() {
             return this.masterStepBuilderFactory
                .get("masterStep")
                .partitioner("workerStep", partitioner())
                .gridSize(1)
                .messagingTemplate(messagingTemplate())
                .inputChannel(replies())
                .build();
    }

    @Bean 
    public Partitioner partitioner() {
    	return new SimplePartitioner();
    }
    
    //@Bean 
    public MultiResourcePartitioner multiResourcePartitioner() {
    	MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
    	partitioner.partition(1);
    	Resource[] rs = new Resource[] { new ClassPathResource("C:\\Users\\yaoqiang\\Desktop\\123\\B订单.csv")};
    	partitioner.setResources(rs);
    	return partitioner;
    }
	
    @Bean
    public MessagingTemplate messagingTemplate() {
    	//消息模板
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		//默认发送通道 适配
		messagingTemplate.setDefaultChannel(requests());
		//超时时间
		messagingTemplate.setReceiveTimeout(10000);
		return messagingTemplate;
    }
    
	//@Bean
	public PartitionHandler partitionHandler(MessagingTemplate messagingTemplate) {
		MessageChannelPartitionHandler partitionHandler = new MessageChannelPartitionHandler();
		
		partitionHandler.setMessagingOperations(messagingTemplate);
		//返回通道 适配
		partitionHandler.setReplyChannel(replies());
		//分区
		partitionHandler.setGridSize(1);
		
		partitionHandler.setStepName("workerStep");
		return partitionHandler;
	}
	
	//@Bean
	public StepExecutionAggregator stepExecutionAggregator(JobExplorer jobExplorer) {
		RemoteStepExecutionAggregator aggregator = new RemoteStepExecutionAggregator();
		aggregator.setJobExplorer(jobExplorer);
		return aggregator;
	}
	
	//@Bean
	//@ServiceActivator(inputChannel = "inboundStaging")
	public AggregatorFactoryBean aggregatorFactoryBean(MessageChannelPartitionHandler partitionHandler,
			StepExecutionAggregator aggregator) throws Exception {
	    AggregatorFactoryBean aggregatorFactoryBean = new AggregatorFactoryBean();
	    aggregatorFactoryBean.setProcessorBean(partitionHandler(null));
	    aggregatorFactoryBean.setOutputChannel(replies());
	    return aggregatorFactoryBean;
	}
}
