package com.cloud.spring.integration.header;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.messaging.MessageHeaders;

public class CustomHeaderMapper extends DefaultKafkaHeaderMapper {

	@Override
	protected Object headerValueToAddOut(String key, Object value) {
		if(value instanceof String && value.toString().contains(":") ) 
			value = "\"" + value + "\"";
		
		return super.headerValueToAddOut(key, value);
	}



}
