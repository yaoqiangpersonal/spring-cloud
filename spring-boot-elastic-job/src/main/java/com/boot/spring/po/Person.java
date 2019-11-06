package com.boot.spring.po;

import com.boot.spring.annotation.AnimalUtil;

import lombok.Data;

@Data
@AnimalUtil
public class Person implements Animal{
	
	private String id;
	
	private String name;

}
