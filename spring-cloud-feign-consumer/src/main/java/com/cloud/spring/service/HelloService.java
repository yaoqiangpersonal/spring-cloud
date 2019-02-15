package com.cloud.spring.service;

import org.springframework.cloud.openfeign.FeignClient;

import com.cloud.spring.api.BaseService;

@FeignClient(value="hello-service",fallback=HelloServiceFallBack.class)
public interface HelloService extends BaseService{

}
