package com.cloud.spring.integration.serializer;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

public class CustomSerializer implements Serializer<Object> {

	@Override
	public byte[] serialize(String topic, Object data) {
		return SerializationUtils
		.serialize(data);
	}

}
