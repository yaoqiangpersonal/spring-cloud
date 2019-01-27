package com.cloud.spring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.spring.po.User;
import com.cloud.spring.service.HelloService;


@RestController
public class ConsumerController {
	
	@Autowired
	private HelloService helloService;
	
	@RequestMapping(value="/ribbon-consumer",method=RequestMethod.GET)
	public User helloConsumer() throws Exception{
	    User demo1 = helloService.find(1l);
	    System.out.println(demo1);
		return demo1;
	}
	
	@GetMapping(value="/turbine-consumer")
	public String turbineConsumer() throws Exception{
	    String demo1 = helloService.turbineConsumer();
	    System.out.println(demo1);
		return demo1;
	}

}
