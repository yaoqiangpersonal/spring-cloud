package com.cloud.spring.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.expression.ValueExpression;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.redis.inbound.RedisStoreMessageSource;
import org.springframework.integration.scheduling.PollerMetadata;
import com.cloud.spring.integration.config.KafkaAppProperties;
import com.rometools.rome.feed.synd.SyndEntry;

@SpringBootApplication
@EnableConfigurationProperties(KafkaAppProperties.class)
@EnableBatchProcessing(modular = true)
@IntegrationComponentScan
@EnableBatchIntegration
public class SpringBootIntegrationApplication {

	@Value("https://spring.io/blog.atom")  //通过@Value注解自动获得https://spring.id/blog.atom的资源
	Resource resource;
	
	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {  
		//使用Fluent API和Pollers配置默认的轮询方式
		return Pollers.cron("0/5 * * ? * *").get();
	}
	
	@Bean
	public PublishSubscribeChannel publishSubscribeChannel() {
		PublishSubscribeChannel channel = new PublishSubscribeChannel();
		return channel;
	}

	
	@Bean
	public FeedEntryMessageSource feedEntryMessageSource()throws IOException{ 
		//FeedEntryMessageSource实际为feed:inbound-channel-adapter,此处即构造feed的入站通道适配器作为数据输入
		FeedEntryMessageSource messageSource = new FeedEntryMessageSource(resource.getURL(), "news");
		return messageSource;
	}
	
	@Bean
	public RedisStoreMessageSource redisStoreMessageSource(RedisTemplate<String, Object> redisTemplate)throws IOException{ 
		//FeedEntryMessageSource实际为feed:inbound-channel-adapter,此处即构造feed的入站通道适配器作为数据输入
		RedisStoreMessageSource messageSource = new RedisStoreMessageSource(redisTemplate,new ValueExpression<String>("test"));
		return messageSource;
	}
	
	//@Bean
	public IntegrationFlow  sou(RedisStoreMessageSource redisStoreMessageSource) {
		return IntegrationFlows.from(redisStoreMessageSource)
				.<DefaultRedisList<Integer>>filter(rs-> rs.size() != 0)
				.<DefaultRedisList<Integer>,List<Integer>>transform(rs-> {
					 
					 List<Integer> list = new ArrayList<>();
					for(int i = 0;i< 200 || i == rs.size();i++) {
						list.add(rs.pop());
					}
					return list;
					
				}
				)
				.channel("output")
				.handle(l->{
					List<Integer> list =(List<Integer>)l.getPayload();	
					list.forEach(System.out::println);
				})
			     .get();
	}
	
	/*
	 * @Bean public IntegrationFlow testFlow() { return
	 * IntegrationFlows.from("replies") .handle(System.out::println) .get(); }
	 */
	//@Bean
	public IntegrationFlow myFlow() throws IOException {
	
		return IntegrationFlows.from(feedEntryMessageSource())  //流程从from方法开始
				.log()
				.<SyndEntry,String>route(
						payload ->
							payload.getCategories().get(0).getName(),  
							//通过路由方法route来选择路由，消息体（payload）的类型为SyndEntry，作为判断条件的类型为String，判断的值是通过payload获得的分类（Categroy）;
							mapping -> mapping
							.channelMapping("releases","releasesChannel")  
							//通过不同分类的值转向不同的消息通道，若分类为releases，则转向releasesChannel;
							.channelMapping("engineering","engineeringChannel")
							.channelMapping("engineering","newsChannel")
						)
				.get();   //通过get方法获得IntegrationFlow实体，配置为Spring的Bean。
	}

	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootIntegrationApplication.class, args);
	}

}
