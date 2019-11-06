package com.boot.spring.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.GenericMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaOperations;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

public class CustomKafkaTemplate<K,V,R> extends ReplyingKafkaTemplate<K,V,R> {

	public CustomKafkaTemplate(
			ProducerFactory<K, V> producerFactory,
			GenericMessageListenerContainer<K, R> replyContainer) {
		super(producerFactory, replyContainer);
	}


	
	

}
