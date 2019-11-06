package com.cloud.spring.integration.config;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.integration.chunk.RemoteChunkingMasterStepBuilderFactory;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.batch.item.file.DefaultBufferedReaderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.scheduling.quartz.SimpleThreadPoolTaskExecutor;

/**
 * @author yaoqiang
 *
 *
 */
//@Configuration
public class WorkerConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
    @Autowired
    private RemoteChunkingWorkerBuilder<String,String>  workerBuilder;

	
    @Bean
    public IntegrationFlow workerFlow() {
        return this.workerBuilder

                   .itemProcessor(asynProcessor())
                   .itemWriter(asynWriter())
                   .inputChannel(replies()) // requests received from the master
                   .outputChannel(requests()) // replies sent to the master
                   .build();
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

	
	/**
	 * 
	 *   	包含了messageGetaway测试
	 * @return
	 */
	
	@Bean
	public ItemProcessor asynProcessor() {
		ItemProcessor itemProcessor = s->{System.out.println("processor" +s.toString());return s;};
		AsyncItemProcessor asyncItemProcessor = new AsyncItemProcessor<>();
	    asyncItemProcessor.setTaskExecutor(new SyncTaskExecutor());
	    asyncItemProcessor.setDelegate(itemProcessor);
	    return asyncItemProcessor;
	}
	

	@Bean
	public AsyncItemWriter asynWriter() {
		ItemWriter itemWriter = l->System.out.println("writer" + l);
	    AsyncItemWriter asyncItemWriter = new AsyncItemWriter<>();
	    asyncItemWriter.setDelegate(itemWriter);
	    return asyncItemWriter;
	}
	







}
