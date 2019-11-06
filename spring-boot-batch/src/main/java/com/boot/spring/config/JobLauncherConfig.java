package com.boot.spring.config;

import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class JobLauncherConfig implements StepExecutionListener {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	private Map<String,JobParameter> params;
	
	@Bean
	public Job jobLauncherJob() {
		return jobBuilderFactory.get("jobLauncherJob")
		.start(jobLauncherJobStep())
		.build();

	}
	
	@Bean
	public Step jobLauncherJobStep() {
		return stepBuilderFactory.get("jobLauncherJobStep")
				.listener(this)
				.tasklet((stepContribution,chunkContext)->{
					System.out.println(params.get("msg").getValue().toString());
					System.out.println(chunkContext.getStepContext().getJobParameters().get("msg").toString());
					return RepeatStatus.FINISHED;
				})
				.build();
		
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		params = stepExecution.getJobParameters().getParameters();
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

}
