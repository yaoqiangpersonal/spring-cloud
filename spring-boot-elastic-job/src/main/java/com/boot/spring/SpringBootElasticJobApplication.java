package com.boot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.boot.spring.annotation.EnableAnimal;
import com.boot.spring.po.Person;
import com.cxytiandi.elasticjob.annotation.EnableElasticJob;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

@SpringBootApplication
@EnableElasticJob
//@EnableScheduling
@EnableAnimal
public class SpringBootElasticJobApplication {
	
	@Scheduled(fixedDelay = 10000)
	public void run() {
		System.out.println("test");
	}


	public static void main(String[] args) {
		SpringApplication.run(SpringBootElasticJobApplication.class, args);
	}

}
