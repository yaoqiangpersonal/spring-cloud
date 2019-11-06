package com.cloud.spring.integration.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.integration.partition.StepExecutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.kafka.support.SimpleKafkaHeaderMapper;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.MessageHandler;

import com.cloud.spring.integration.deserializer.CustomDeserializer;
import com.cloud.spring.integration.header.CustomHeaderMapper;

@Configuration
public class KafkaConfig {
	
	@Autowired
	private QueueChannel receiveChannel;


	@Bean
	public KafkaHeaderMapper kafkaHeaderMapper() {
		SimpleKafkaHeaderMapper kafkaHeaderMapper = new SimpleKafkaHeaderMapper();
		return kafkaHeaderMapper;
	}
	
	
	
	@Bean
	public ProducerFactory<Object, Object> kafkaProducerFactory(KafkaProperties properties) {
		Map<String, Object> producerProperties = properties.buildProducerProperties();
		producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		/*
		 * JsonSerializer<Object> key = new JsonSerializer<>(); JsonSerializer<Object>
		 * value = new JsonSerializer<>(); Map<String,Object> configs = new
		 * HashMap<String, Object>(); configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
		 * StepExecutionRequest.class); value.configure(configs, false);
		 */
		return new DefaultKafkaProducerFactory<>(producerProperties);
	}
	
	
	/**
	 * 
	 *  接收处理后的请求，出栈适配通道
	 *  
	 * @param kafkaTemplate
	 * @param properties
	 * @return
	 */
	@ServiceActivator(inputChannel = "returnChannel")
	@Bean
	public MessageHandler handler(KafkaTemplate<String, String> kafkaTemplate,KafkaAppProperties properties) {
		KafkaProducerMessageHandler<String, String> handler = new KafkaProducerMessageHandler<>(kafkaTemplate);
		//发送到出栈通道
		handler.setTopicExpression(new LiteralExpression(properties.getReturnTopic()));
		handler.setMessageKeyExpression(new LiteralExpression(properties.getMessageKey()));
		handler.setHeaderMapper(new CustomHeaderMapper());
		return handler;
	}

	@Bean
	public ConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties properties) {
		Map<String, Object> consumerProperties = properties
				.buildConsumerProperties();
		consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
		/*
		 * JsonDeserializer<Object> key = new JsonDeserializer<Object>();
		 * Map<String,Object> configs = new HashMap<String, Object>();
		 * configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE, StepExecutionRequest.class);
		 * JsonDeserializer<Object> value = new JsonDeserializer<Object>();
		 * value.configure(configs, false);
		 */
		return new DefaultKafkaConsumerFactory<>(consumerProperties);
	}

	@Bean
	public KafkaMessageListenerContainer<?,?> container(
			ConsumerFactory<Object, Object> kafkaConsumerFactory,KafkaAppProperties properties) {
		//监听 接收通道
		ContainerProperties containerProperties = new ContainerProperties(new TopicPartitionOffset(properties.getReceiveTopic(), 0));
		containerProperties.setMissingTopicsFatal(false);
		return new KafkaMessageListenerContainer<>(kafkaConsumerFactory,containerProperties);
	}

	@Bean
	public KafkaMessageDrivenChannelAdapter<?, ?>
				adapter(KafkaMessageListenerContainer<String, String> container) {
		KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
				new KafkaMessageDrivenChannelAdapter<>(container);
		//发送到 接收通道 适配通道
		kafkaMessageDrivenChannelAdapter.setOutputChannel(receiveChannel);
		return kafkaMessageDrivenChannelAdapter;
	}

}
