package com.boot.spring.config;

import java.util.List;
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
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilder;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.chunk.RemoteChunkingMasterStepBuilder;
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

import com.boot.spring.listener.NotificationExecutionListener;
import com.boot.spring.listener.impl.NotificationExecutionListenerImpl;
import com.boot.spring.po.User;

/**
 * @author yaoqiang
 *
 *测试事务相关
 */
//@Configuration
public class RemoteChunkingConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
    @Autowired
    private RemoteChunkingManagerStepBuilderFactory masterStepBuilderFactory;

	@Bean
	public Job remoteJob() {

		return jobBuilderFactory.get("remoteJob")
				.start(masterStep())
				.build();		
	}
	
    @Bean
    public TaskletStep masterStep() {
        return  masterStepBuilderFactory.get("masterStep")
                   .chunk(100)
                   .reader(sampleReader(null))
                   .inputChannel(replies())   // replies received from workers
                   .outputChannel(requests()) // requests sent to workers
                   .build();
    }

    
    /*
     * Configure inbound flow (replies coming from workers)
     */
    @Bean
    public QueueChannel replies() {
        return new QueueChannel();
    }
    
    
    /*
     * Configure outbound flow (requests going to workers)
     */
    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

	@Bean
	@StepScope
	public FlatFileItemReader<String> sampleReader(@Value("#{jobParameters[fielName]}")String resource) {
		System.out.println(resource);
	    FlatFileItemReader<String> flatFileItemReader = new FlatFileItemReader<>();
	    flatFileItemReader.setEncoding("gbk");
	    flatFileItemReader.setResource(new FileSystemResource(resource));
	    flatFileItemReader.setLineMapper(new PassThroughLineMapper());
	    return flatFileItemReader;
	}
	

}
