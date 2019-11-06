package com.boot.spring.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.boot.spring.po.User;

/**
 * @author yaoqiang
 *
 *测试事务相关
 */
@Configuration
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
	public Step transactionStep() {

		return stepBuilderFactory.get("transactionStep")
				.<User,User>chunk(2)
				.reader(jpaPagingItemReader())
				.processor(processor())
				.writer(writer())
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
	public ItemStreamReader<? extends User> jpaPagingItemReader() {
		JpaPagingItemReaderBuilder<User> builder = new JpaPagingItemReaderBuilder<>();
		JpaNativeQueryProvider<User> queryProvider = new JpaNativeQueryProvider<User>();
		queryProvider.setEntityClass(User.class);
		queryProvider.setSqlQuery("select id,username,password from user");

		return builder
		.entityManagerFactory(entityManagerFactory)
		.pageSize(10)
		.queryProvider(queryProvider)
		.saveState(false)
		.build();
	}

}
