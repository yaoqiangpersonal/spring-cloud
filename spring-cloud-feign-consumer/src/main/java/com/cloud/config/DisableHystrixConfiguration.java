package com.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import feign.Feign;


/**
 * feign配置类
 * 
 * @author yaoqiang
 *
 */
@Configuration
public class DisableHystrixConfiguration {
	
	

	@Bean
	@Scope("property")
	public Feign.Builder feignBuilder(){
		return Feign.builder();
	}

}
