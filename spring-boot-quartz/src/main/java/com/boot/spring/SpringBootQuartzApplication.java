package com.boot.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cxytiandi.elasticjob.annotation.EnableElasticJob;

@SpringBootApplication
@EnableElasticJob
public class SpringBootQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootQuartzApplication.class, args);
	}

}
