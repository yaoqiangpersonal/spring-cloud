package com.cloud.spring.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloud.spring.api.BaseService;
import com.cloud.spring.po.User;

@FeignClient(value="hello-service",fallback=HelloServiceFallBack.class)
public interface HelloService extends BaseService{

}
