package com.boot.spring.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.boot.spring.listener.impl.NotificationExecutionListenerImpl;

//@Configuration
public class BatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job jobFlowBuilder() {
		return jobBuilderFactory.get("job")
		.start(step1())
		.build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("test")
		.listener(new NotificationExecutionListenerImpl())
		.<String,String>chunk(1)
		.reader(itemReader())
		.writer(l->{
			l.forEach(System.out::println);
		}).build();
		
	}
	
	public void print(List<String> list) {
		list.forEach(System.out::println);
	}
	

	
	public ItemReader<String> itemReader(){
		List<String> list = new ArrayList<String>(Arrays.asList("one","two","three","four"));
		ItemReader<String> reader = new IteratorItemReader<String>(list.iterator());
		return reader;
		
	}
	
	
}
