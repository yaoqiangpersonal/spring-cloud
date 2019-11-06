package com.cloud.spring.integration.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.integration.partition.RemotePartitioningWorkerStepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;

@Configuration
public class PartitionerWorkerConfig {

    @Autowired
    private RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory;


    /**
     *	 接收通道
     * 
     * @return
     */
    @Bean
    public QueueChannel receiveChannel() {
        return new QueueChannel();
    }
    
    
    /**
     * 	返回通道
     * 
     * @return
     */
    @Bean
    public DirectChannel returnChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public Step workerStep() {
             return this.workerStepBuilderFactory
                .get("workerStep")
                .inputChannel(receiveChannel())
                .outputChannel(returnChannel())
                .chunk(10)
                .reader(sampleReader(null))
                .processor(asynProcessor())
                .writer(asynWriter())
                .build();
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
