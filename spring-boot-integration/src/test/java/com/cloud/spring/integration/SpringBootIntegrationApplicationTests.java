package com.cloud.spring.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.integration.chunk.ChunkRequest;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SerializationUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootIntegrationApplicationTests {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	public void contextLoads() {

		ChunkRequest<String> request = new ChunkRequest<>(0, Arrays.asList("foo", "bar"),
				111L, MetaDataInstanceFactory.createStepExecution().createStepContribution());
		ChunkRequest<String> result = (ChunkRequest<String>) SerializationUtils.deserialize(SerializationUtils
				.serialize(request));
		
		assertNotNull(result.getStepContribution());
		assertEquals(111L, result.getJobId());
		assertEquals(2, result.getItems().size());
		
	}

}
