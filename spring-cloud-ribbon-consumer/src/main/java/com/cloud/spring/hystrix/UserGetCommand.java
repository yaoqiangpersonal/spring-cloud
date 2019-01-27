package com.cloud.spring.hystrix;

import org.springframework.web.client.RestTemplate;

import com.cloud.spring.po.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

public class UserGetCommand  extends HystrixCommand<User>{

	private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("Command");
	
	private RestTemplate restTemplate;

	private long id;
	
	public UserGetCommand(RestTemplate restTemplate,long id) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetSelect")).andCommandKey(GETTER_KEY));
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
	
	public static void flushCache(long id) {
		HystrixRequestCache
		.getInstance(GETTER_KEY, HystrixConcurrencyStrategyDefault.getInstance())
		.clear(String.valueOf(id));
	}
}
