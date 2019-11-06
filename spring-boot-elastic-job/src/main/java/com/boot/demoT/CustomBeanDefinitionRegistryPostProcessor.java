package com.boot.demoT;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import com.boot.spring.po.Person;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		//注册Bean定义，容器根据定义返回bean
		log.info("register personManager1>>>>>>>>>>>>>>>>");
		BeanDefinitionBuilder beanDefinitionBuilder =
		        BeanDefinitionBuilder.genericBeanDefinition(Person.class);
		beanDefinitionBuilder.addPropertyValue("name", "123");
		BeanDefinition personManagerBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
		registry.registerBeanDefinition("person", personManagerBeanDefinition);
		
	}

}
