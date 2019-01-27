package com.cloud.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cloud.spring.po.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;


@Service
public class HelloService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	//@HystrixCollapser(batchMethod="findAll",collapserProperties= {@HystrixProperty(name="timerDelayInMilliseconds",value="100")})
	@HystrixCommand(fallbackMethod = "back")
	@CacheResult
	public User find(@CacheKey Long id){
		logger.info("没合并");
		return restTemplate.getForObject("http://USER-SERVICE/user/{1}", User.class,id);
	}
	
	@HystrixCommand(fallbackMethod="stringBack")
	public String turbineConsumer() {
		return restTemplate.getForEntity("http://TURBINE/demo", String.class).getBody();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<User> findAll(List<Long> ids) throws JsonProcessingException{
	
		logger.info("合并了a,params:{}",ids);
		return restTemplate.getForObject("http://USER-SERVICE/users?ids={1}", List.class,ids.toString());
	}
	
	public List<User> helloFallback(List<Long> ids,Throwable e) {
		e.printStackTrace();
		List<User> list = new ArrayList<User>();
		ids.forEach(l->{
			User u = new User();
			u.setId(l.intValue());
			u.setName("error");
		});
		return list;
	}
	
	public User back(Long id,Throwable e) {
		User u = new User();
		u.setId(id.intValue());
		u.setName("error");
		return u;
	}
	
	public String stringBack() {
		return "error";
	}
}
