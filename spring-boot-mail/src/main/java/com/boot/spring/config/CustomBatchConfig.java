package com.boot.spring.config;

import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.NamedBeanHolder;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CustomBatchConfig extends DefaultBatchConfigurer implements ApplicationContextAware{

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	private ApplicationContext applicationContext;
	

	@Bean
	public JobRegistryBeanPostProcessor jobRegistrarPostProcessor(){
		JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
		jobRegistryBeanPostProcessor.setBeanFactory(applicationContext.getAutowireCapableBeanFactory());
		jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
		return jobRegistryBeanPostProcessor;
	}
	
	@Bean
	public JobOperator jobOperator(){
		SimpleJobOperator jobOperator = new SimpleJobOperator();
		jobOperator.setJobLauncher(getJobLauncher());
		jobOperator.setJobExplorer(getJobExplorer());
		jobOperator.setJobRepository(getJobRepository());
		jobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
		jobOperator.setJobRegistry(jobRegistry);
		return jobOperator;
	}
	
	

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
}
