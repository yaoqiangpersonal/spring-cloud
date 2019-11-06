package com.boot.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.boot.spring.po.Animal;
import com.boot.spring.po.Person;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootElasticJobApplicationTests {

	@Autowired
	private Animal person;
	
	@Test
	public void contextLoads() {
		System.out.println(person.getName());
	}

}
