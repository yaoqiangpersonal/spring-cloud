package com.cloud.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@GetMapping("demo")
	public void send() {
		System.out.println(123);
	}

}
