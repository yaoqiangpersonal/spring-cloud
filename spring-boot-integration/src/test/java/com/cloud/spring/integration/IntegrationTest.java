package com.cloud.spring.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.junit.Test;
import org.springframework.batch.integration.chunk.ChunkRequest;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.util.SerializationUtils;


public class IntegrationTest {
	
	@Test
	public void run() {
		ChunkRequest<String> request = new ChunkRequest<>(0, Arrays.asList("foo", "bar"),
				111L, MetaDataInstanceFactory.createStepExecution().createStepContribution());
		ChunkRequest<String> result = (ChunkRequest<String>) SerializationUtils.deserialize(SerializationUtils
				.serialize(request));
		ByteArraySerializer serializer = new ByteArraySerializer();
		assertNotNull(result.getStepContribution());
		assertEquals(111L, result.getJobId());
		assertEquals(2, result.getItems().size());
	}

}
