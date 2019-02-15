package com.cloud.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableOAuth2Sso
public class SpringCloudFeignConsumerApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(SpringCloudFeignConsumerApplication.class, args);
	}

	

}

