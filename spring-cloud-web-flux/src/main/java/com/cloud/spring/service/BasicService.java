package com.cloud.spring.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.cloud.spring.po.User;

import reactor.core.publisher.Mono;

@Service
public class BasicService {
	private final Map<String, User> data = new ConcurrentHashMap<>();
	public Mono<User> sayHelloWorld() {
		WebClient
			.builder()
			.baseUrl("https://www.ibm.com/developerworks/cn/java/j-cn-with-reactor-response-encode/index.html")
			.build()
			.get()
			.exchange()
			.subscribe(System.out::println);
		return Mono.justOrEmpty(data.get("key")).switchIfEmpty(Mono.error(new RuntimeException("123")));
	}

}
