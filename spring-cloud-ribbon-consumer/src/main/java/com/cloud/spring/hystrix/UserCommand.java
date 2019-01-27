package com.cloud.spring.hystrix;

import org.springframework.web.client.RestTemplate;

import com.cloud.spring.po.User;
import com.netflix.hystrix.HystrixCommand;

public class UserCommand extends HystrixCommand<User>{

	private RestTemplate restTemplate;

	private long id;
	
	public UserCommand(Setter setter,RestTemplate restTemplate,long id) {
		super(setter);
		this.restTemplate = restTemplate;
		this.id = id;
	}

	@Override
	protected User run() throws Exception {
		return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class,id);
	}
	
	/* (non-Javadoc)
	 * @see com.netflix.hystrix.AbstractCommand#getCacheKey()
	 */
	@Override
	protected String getCacheKey() {
		return String.valueOf(id);
	}
	
	

}
