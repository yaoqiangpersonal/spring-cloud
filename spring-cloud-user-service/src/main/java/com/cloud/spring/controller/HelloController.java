package com.cloud.spring.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.spring.po.User;

@RestController
public class HelloController {
	
	private final static Map<Long,User> map = new HashMap<>();
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	static {
		map.put(1l, new User(1,"123"));
		map.put(2l, new User(2,"456"));
		map.put(3l, new User(3,"789"));
	}
	
	@RequestMapping(value="/user/{id}",method=RequestMethod.GET)
	public User getUserById(@PathVariable("id")Long l) {
		logger.info(String.valueOf(l));
		return map.get(l);
	}
	
	@RequestMapping(value="/users",method=RequestMethod.GET)
	public List<User> getUserByIds(String ids) {
		List<Long> idss = handle(ids);
		logger.info("获取到ids:{}",idss);
		List<User> list = new ArrayList<>();
		idss.forEach(l->{
			list.add(getUserById(Long.valueOf(l)));
		});
		return list;
	}
	
	private List<Long> handle(String ids){
		List<String> ss = Arrays.asList(ids.replaceAll("[\\[\\]]", "").split(","));
		return ss.stream().map(s->Long.valueOf(s.trim())).collect(Collectors.toList());
		
	}


}
