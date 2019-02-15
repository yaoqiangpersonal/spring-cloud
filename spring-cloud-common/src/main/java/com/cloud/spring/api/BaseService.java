package com.cloud.spring.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloud.spring.po.User;


public interface BaseService {
	
	@GetMapping("/sendParam")
	String sendParam(@RequestParam("name") String name);
	
	@GetMapping("/sendHeader")
	User sendHeader(@RequestHeader("name") String name,@RequestHeader("age") Integer age);
	
	@PostMapping("/sendJson")
	String sendJson(@RequestBody User user);
	

}
