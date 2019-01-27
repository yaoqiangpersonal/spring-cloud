package com.cloud.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.spring.po.User;
import com.cloud.spring.service.HelloService;

@RestController
@RequestMapping("/feign")
public class ConsumerController {
	
	@Autowired
	private HelloService helloService;
	
	@GetMapping("/hello")
	public String hello() {
		StringBuilder sb = new StringBuilder();
		sb.append(helloService.sendParam("tom")).append("\n");
		sb.append(helloService.sendHeader("tom", 23)).append("\n");
		sb.append(helloService.sendJson(new User(23,"tom"))).append("\n");
		return sb.toString();
	}

}
