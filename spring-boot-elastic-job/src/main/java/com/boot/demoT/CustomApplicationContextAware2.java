package com.boot.demoT;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.boot.spring.po.Person;

@Component
public class CustomApplicationContextAware2 implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(applicationContext instanceof ConfigurableApplicationContext) {
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)((ConfigurableApplicationContext) applicationContext)
					.getBeanFactory();
			BeanDefinitionBuilder beanDefinitionBuilder =
	        BeanDefinitionBuilder.genericBeanDefinition(Person.class);
			beanDefinitionBuilder.addPropertyValue("name", "123");
			BeanDefinition personManagerBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
			beanFactory.registerBeanDefinition("person", personManagerBeanDefinition);
		}
		
	}

}
