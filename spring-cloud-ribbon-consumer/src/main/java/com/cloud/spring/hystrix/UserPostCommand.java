package com.cloud.spring.hystrix;

import org.springframework.web.client.RestTemplate;

import com.cloud.spring.po.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class UserPostCommand extends HystrixCommand<User>{

	private RestTemplate restTemplate;

	private User user;
	
	public UserPostCommand(RestTemplate restTemplate,User user) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetSelect")));
		this.restTemplate = restTemplate;
		this.user = user;
	}

	@Override
	protected User run() throws Exception {
		User u = restTemplate.postForObject("http://USER-SERVICE/users",user, User.class);
		//刷新缓存
		UserGetCommand.flushCache(u.getId());
		return u;
	}
	

}
