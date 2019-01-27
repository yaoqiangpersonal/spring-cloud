package com.cloud.spring.service;

import org.springframework.stereotype.Component;

import com.cloud.spring.po.User;

@Component
public class HelloServiceFallBack implements HelloService {

	@Override
	public String sendParam(String name) {
		return "error";
	}

	@Override
	public User sendHeader(String name, Integer age) {
		return null;
	}

	@Override
	public String sendJson(User user) {
		return "error";
	}

}
