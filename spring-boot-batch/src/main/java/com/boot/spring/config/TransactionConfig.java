package com.boot.spring.config;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManagerFactory;

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
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
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

import com.boot.spring.listener.NotificationExecutionListener;
import com.boot.spring.listener.impl.NotificationExecutionListenerImpl;
import com.boot.spring.po.User;

/**
 * @author yaoqiang
 *
 *测试事务相关
 */
//@Configuration
public class TransactionConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Bean
	public Job transactionJob() {

		return jobBuilderFactory.get("transactionJob")
				.incrementer(new RunIdIncrementer())
				.start(transactionStep())
				.build();
				
	}
	
	@Bean
	public Job fileJob() {

		return jobBuilderFactory.get("fileJob")
				.start(fileStep())
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
	
	@Bean
	@JobScope
	public Step fileStep() {
		return stepBuilderFactory.get("fileStep")
			.<String,String>chunk(10)
			.reader(sampleReader(null))
			.processor(asynProcessor())
			.writer(asynWriter())
		//.listener(notificationExecutionsListener)
		.build();
	}
	
	@Bean
	public AsyncItemProcessor asynProcessor() {
		ItemProcessor<String, String> itemProcessor = s->{System.out.println(s);return s;};
		AsyncItemProcessor asyncItemProcessor = new AsyncItemProcessor<>();
	    asyncItemProcessor.setTaskExecutor(new SyncTaskExecutor());
	    asyncItemProcessor.setDelegate(itemProcessor);
	    return asyncItemProcessor;
	}
	

	@Bean
	public AsyncItemWriter asynWriter() {
		ItemWriter itemWriter = l->System.out.println(l);
	    AsyncItemWriter asyncItemWriter = new AsyncItemWriter<>();
	    asyncItemWriter.setDelegate(itemWriter);
	    return asyncItemWriter;
	}
	

	@Bean
	@JobScope
	public Step transactionStep() {

		return stepBuilderFactory.get("transactionStep")
				.<User,User>chunk(2)
				.reader(jpaPagingItemReader())
				.processor(processor())
				.writer(writer())
				//.transactionManager(new JpaTransactionManager(entityManagerFactory))
				.listener(new StepExecutionListener() {
					
					@Override
					public void beforeStep(StepExecution stepExecution) {
						System.out.println("init");
					}
					
					@Override
					public ExitStatus afterStep(StepExecution stepExecution) {
						// TODO Auto-generated method stub
						System.out.println("end");
						return ExitStatus.COMPLETED;
					}
				})
				.build();
				
	}


	@Bean
	public ItemWriter<? super User> writer() {
		JpaItemWriterBuilder<User> builder = new JpaItemWriterBuilder<>();
		return builder.entityManagerFactory(entityManagerFactory).build();
	}

	@Bean
	public ItemProcessor<? super User, ? extends User> processor() {
		return u -> {
			u.setUsername("success");
			return u;
		};
	}

	@Bean
	public JpaPagingItemReader<User> jpaPagingItemReader() {
		JpaPagingItemReaderBuilder<User> builder = new JpaPagingItemReaderBuilder<>();
		JpaNativeQueryProvider<User> queryProvider = new JpaNativeQueryProvider<User>();
		queryProvider.setEntityClass(User.class);
		queryProvider.setSqlQuery("select * from user");

		return builder
		//.entityManagerFactory(entityManagerFactory)
		
		.pageSize(2)
		.name("test")
		.queryProvider(queryProvider)
		.saveState(true)
		.build();
	}

}
