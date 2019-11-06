package com.boot.demoT;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.boot.spring.po.Person;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomBeanFactoryPostProcessor2 implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
		//注册Bean定义，容器根据定义返回bean
		log.info("register personManager1>>>>>>>>>>>>>>>>");
		BeanDefinitionBuilder beanDefinitionBuilder =
		        BeanDefinitionBuilder.genericBeanDefinition(Person.class);
		beanDefinitionBuilder.addPropertyValue("name", "123");
		BeanDefinition personManagerBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
		defaultListableBeanFactory.registerBeanDefinition("person", personManagerBeanDefinition);
		
	}

}
