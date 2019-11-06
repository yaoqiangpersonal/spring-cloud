package com.boot.spring.config;

import java.util.Map;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.boot.spring.po.User;

//@Configuration
public class ErrorBatch {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job errorJob() {
		return jobBuilderFactory.get("errorJob")
		.start(errorStep1())
		.next(errorStep2())
		.build();

	}
	
	@Bean
	public Step errorStep1() {
		return stepBuilderFactory.get("errorStep1")
				.tasklet(errorHandling())
				.build();
		
	}
	
	@Bean
	public Step errorStep2() {
		return stepBuilderFactory.get("errorStep2")
				.tasklet(errorHandling())
				.build();
	}
	
	@Bean
	@StepScope
	public Tasklet errorHandling() {
		return (stepContribution,chunkContext)->{
			//step独有
			ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
			System.out.println(stepExecutionContext.get("error"));
			if(stepExecutionContext.containsKey("error")) {
				System.out.println("二次通过");
				return RepeatStatus.FINISHED;
			}else {
				System.out.println("初次错误");
				stepExecutionContext.put("error", true);
				throw new RuntimeException("发生错误");
			}
		};
		
	}

}
