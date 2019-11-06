package com.cloud.spring.integration.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

import com.rometools.rome.feed.synd.SyndEntry;

//@Configuration
public class BeanConfig {

	@Bean
	public IntegrationFlow demoFlow() {
		return IntegrationFlows.from("input") // 从Channel input获取消息
				.<String, Integer>transform(Integer::parseInt) // 将消息转换成整数
				.get(); // 获得集成流程并注册为Bean
	}

	@Bean
	public IntegrationFlow releasesFlow() {
		return IntegrationFlows.from("releasesChannel") // 从消息通道releasesChannel开始获取数据。
				.<SyndEntry, String>transform(payload -> "《" + payload.getTitle() + "》 " + payload.getLink())// 使用transform方法进行数据转换。payload类型为SyndEntry，将其转换为字符串类型，并自定义数据的格式。
				.handle(
						Files.outboundAdapter(new File("e:/springblog")) // 用handle方法处理file的出站适配器。Files类是由Spring
																			// Integration Java DSL提供的 Fluent
																			// API用来构造文件输出的适配器。
						.fileExistsMode(FileExistsMode.APPEND).charset("UTF-8")
						.fileNameGenerator(message -> "releases.txt"))
				.get();

	}

	@Bean
	public IntegrationFlow engineeringFlow() {
		return IntegrationFlows.from("engineeringChannel")
				.<SyndEntry, String>transform(payload -> "《" + payload.getTitle() + "》" + payload.getLink())
				.handle(Files
						.outboundAdapter(new File("e:/springblog"))
						.fileExistsMode(FileExistsMode.APPEND)
						.charset("UTF-8")
						.fileNameGenerator(message -> "engineering.txt")
						.get())
				.get();

	}

	@Bean
	public IntegrationFlow newsFlow() {
		return IntegrationFlows.from("newsChannel")
				
				.<SyndEntry, String>transform(
						payload -> "《" + payload.getTitle() + "》" + payload.getLink()
						)
				.enrichHeaders(
						// 通过enricherHeader来增加消息头的信息
						Mail.headers()
						.subject("来自Spring的新闻")
						.to("562456140@qq.com")
						.from("562456140@qq.com"))
				.handle(Mail
						.outboundAdapter("smtp.qq.com")
						.credentials("562456140@qq.com", "tpatdvybmfdubbcd")
						.port(465)
						.protocol("smtp")
						.defaultEncoding("UTF-8")
						.javaMailProperties(p->{
							p.put("mail.debug", true);
							p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
						}))
				.get();
	}

}
