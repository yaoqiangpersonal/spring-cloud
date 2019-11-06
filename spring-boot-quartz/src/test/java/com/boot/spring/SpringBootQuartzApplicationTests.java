package com.boot.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.boot.spring.elastic.MyElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootQuartzApplicationTests {

	@Test
	public void contextLoads() {

	}

}
