package com.cloud.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
public class WebMvcConfig  {
	
	
	/*
	 * @Bean public InternalResourceViewResolver viewResolver() {
	 * InternalResourceViewResolver viewResolver = new
	 * InternalResourceViewResolver(); viewResolver.setPrefix("/static/");
	 * viewResolver.setSuffix(".html");
	 * viewResolver.setContentType("text/html;charset=UTF-8"); return viewResolver;
	 * }
	 */
	
	@Bean
	@Order(0)
	public RequestContextListener requestContextListener() {
	    return new RequestContextListener();
	}
}
