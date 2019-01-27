package com.cloud.spring.config;

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

public class DisableHystrixConfiguration {
	
	

	public Feign.Builder feignBuilder(){
		return Feign.builder();
	}

}
