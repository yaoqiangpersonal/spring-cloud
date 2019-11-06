package com.boot.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.MapExecutionContextDao;
import org.springframework.batch.core.repository.dao.MapJobExecutionDao;
import org.springframework.batch.core.repository.dao.MapJobInstanceDao;
import org.springframework.batch.core.repository.dao.MapStepExecutionDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.integration.chunk.ChunkRequest;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;



public class JobTest {

	private JobRepository jobRepository;
	
	
	@Test
	public void run() {
		JsonSerializer<ChunkRequest<String>> serializer = new JsonSerializer<>();
		JsonDeserializer<ChunkRequest<String>> deserializer = new JsonDeserializer<>();
		ChunkRequest<String> request = new ChunkRequest<>(0, Arrays.asList("foo", "bar"),
				111L, MetaDataInstanceFactory.createStepExecution().createStepContribution());
		ChunkRequest<String> result = deserializer.deserialize("topic", serializer.serialize("topic", request));
		assertNotNull(result.getStepContribution());
		assertEquals(111L, result.getJobId());
		assertEquals(2, result.getItems().size());
	}
	
	@Before
	public void jobRepository() {
		JobInstanceDao jobInstanceDao = new MapJobInstanceDao();
		JobExecutionDao jobExecutionDao = new MapJobExecutionDao();
		StepExecutionDao stepExecutionDao = new MapStepExecutionDao();
		ExecutionContextDao ecDao = new MapExecutionContextDao();
		
		jobRepository = new SimpleJobRepository(jobInstanceDao, jobExecutionDao, stepExecutionDao, ecDao);
	}
	
	@Test
	public void contextLoads() {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		ThreadPoolTaskExecutor taskExecutor =  new ThreadPoolTaskExecutor();
		taskExecutor.afterPropertiesSet();
		jobLauncher.setTaskExecutor(taskExecutor);
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("key", "test")
				.toJobParameters();
		try {
			jobLauncher.run(getJob(), jobParameters);
		} catch (JobExecutionAlreadyRunningException 
				| JobRestartException 
				| JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
		try {
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Job getJob() {
		
		 JobBuilderFactory jobBuilderFactory = new JobBuilderFactory(jobRepository);
		
		 return 
				 jobBuilderFactory
				 .get("test")
				 .start(step1()).build();
		
		
	
	}
	
	public Step step1() {
		ResourcelessTransactionManager manager = new ResourcelessTransactionManager();

		StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, manager);
			
		ListItemWriter<Integer> list = new ListItemWriter<Integer>() {
			@Override
			public void write(List<? extends Integer> items) throws Exception {
				List<? extends Integer> is = items.stream().map(i->i + items.get(0)).collect(Collectors.toList());
				super.write(is);
			}
		};
		return stepBuilderFactory.get("step")
		.<Integer,Integer>chunk(2)
		.reader(new ListItemReader<Integer>(Arrays.asList(1,2,3,4,5,6)))
		.writer(list)
		.listener(new ItemWriteListener<Integer>() {
			@Override
			public void afterWrite(List<? extends Integer> items) {
				System.out.println(list.getWrittenItems());
			}
			
			@Override
			public void beforeWrite(List<? extends Integer> items) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onWriteError(Exception exception, List<? extends Integer> items) {
				// TODO Auto-generated method stub
				
			}
		}).build();
		
	}

	
}
