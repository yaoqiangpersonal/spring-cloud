package com.cloud.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class HelloService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public Mono<String> sayHello(String message) {
		log.info("params:{}",message);
		return Mono.just("return :" + message);
	}
	
}
