package com.boot.spring;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing(modular = true)
public class SpringBootMailApplication {

	
	@Autowired
	private JobOperator jobOperator;
	
	@Scheduled(fixedDelay = 1000)
	public void run() {

		try {
			try {
				jobOperator.startNextInstance("transactionJob");
			} catch (JobParametersNotFoundException | JobRestartException | JobExecutionAlreadyRunningException
					| JobInstanceAlreadyCompleteException | UnexpectedJobExecutionException e) {
				e.printStackTrace();
			}
			
		} catch (NoSuchJobException  | JobParametersInvalidException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootMailApplication.class, args);
	}

}
