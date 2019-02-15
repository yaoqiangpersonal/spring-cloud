package com.cloud.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.spring.service.HelloService;

import reactor.core.publisher.Mono;

@RestController
public class HelloController {
	
	@Autowired
	private HelloService helloService;
	
	@GetMapping("/demo")
	public Mono<String> sayHello(@PathVariable("msg") String msg){
		return helloService.sayHello(msg);
	}
	

}
