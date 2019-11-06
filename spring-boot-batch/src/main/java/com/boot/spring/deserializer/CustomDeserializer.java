package com.boot.spring.deserializer;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.util.SerializationUtils;

public class CustomDeserializer implements Deserializer<Object> {

	@Override
	public Object deserialize(String topic, byte[] data) {
		return SerializationUtils
		.deserialize(data);

	}
	

}
