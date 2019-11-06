package com.boot.spring.config;

import java.time.Duration;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
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
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.messaging.MessageHandler;

import com.boot.spring.header.CustomHeaderMapper;

@Configuration
public class KafkaConfig {
	
	@Autowired
	private QueueChannel replies;
	
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
	
	@Bean
	public KafkaTemplate<?, ?> kafkaTemplate(KafkaMessageListenerContainer<Object, Object> container) {
		ReplyingKafkaTemplate<Object, Object, Object> template = new ReplyingKafkaTemplate<Object, Object, Object>(kafkaProducerFactory(null), container);
		template.setDefaultReplyTimeout(Duration.ofSeconds(30));
		template.afterPropertiesSet();
		return template;
	}
	

	@ServiceActivator(inputChannel = "requests")
	@Bean
	public MessageHandler handler(KafkaTemplate<Object, Object> kafkaTemplate,KafkaAppProperties properties) {
		KafkaProducerMessageHandler<Object, Object> handler = new KafkaProducerMessageHandler<>(kafkaTemplate);
		handler.setOutputChannel(replies);
		handler.setHeaderMapper(new CustomHeaderMapper());
		handler.setTopicExpression(new LiteralExpression(properties.getSendTopic()));
		handler.setMessageKeyExpression(new LiteralExpression(properties.getMessageKey()));
		handler.setRequiresReply(true);
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
	public KafkaMessageListenerContainer<?, ?> container(
			ConsumerFactory<Object, Object> kafkaConsumerFactory,KafkaAppProperties properties) {
		ContainerProperties containerProperties = new ContainerProperties(new TopicPartitionOffset(properties.getReturnTopic(), 0));
		containerProperties.setMissingTopicsFatal(false);
		return new KafkaMessageListenerContainer<Object, Object>(kafkaConsumerFactory,containerProperties);
	}

	//@Bean
	public KafkaMessageDrivenChannelAdapter<String, String>
				adapter(KafkaMessageListenerContainer<String, String> container) {
		KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
				new KafkaMessageDrivenChannelAdapter<>(container);
		kafkaMessageDrivenChannelAdapter.setOutputChannel(replies);
		return kafkaMessageDrivenChannelAdapter;
	}

}
