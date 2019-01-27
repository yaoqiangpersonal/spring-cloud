package com.boot.learn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.spring.api.BaseService;
import com.cloud.spring.po.User;

@RestController
public class HelloController implements BaseService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DiscoveryClient client;
	
	@RequestMapping(value="/hello",method = RequestMethod.GET)
	public String index() {
		ServiceInstance instance = client.getInstances("hello-service").get(0);
		logger.info("/hello,host:" + instance.getHost() + ",service_id:" + instance.getServiceId());
		return "Hello world";
	}
	
	@Override
	public User sendHeader(String name, Integer age) {
		return new User(age, name);
	}

	@Override
	public String sendParam(String name) {
		
		return "Hello " + name;
	}

	@Override
	public String sendJson(User user) {
		return "Hello" + user.getName() + ", " + user.getId();
	}
	
	

}
