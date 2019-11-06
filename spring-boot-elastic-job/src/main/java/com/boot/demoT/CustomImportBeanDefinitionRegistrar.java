package com.boot.demoT;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.boot.spring.annotation.AnimalUtil;
import com.boot.spring.po.People;
import com.boot.spring.po.Person;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Setter
@Slf4j
public class CustomImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar
,ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware, BeanFactoryAware{

	private BeanFactory beanFactory;
	
	private Environment environment;
	
	private ClassLoader classLoader;

	private ResourceLoader resourceLoader;
	
	

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		registerHttpRequest(registry);
		
	}
	
	   /**
	    * 注册动态bean的主要方法
	    *
	    * @param beanDefinitionRegistry
	    */
	   private void registerHttpRequest(BeanDefinitionRegistry beanDefinitionRegistry) {
	       ClassPathScanningCandidateComponentProvider classScanner = getClassScanner();
	       classScanner.setResourceLoader(this.resourceLoader);
	       //指定只关注标注了@HTTPUtil注解的接口
	       AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(AnimalUtil.class);
	       classScanner.addIncludeFilter(annotationTypeFilter);
	       //指定扫描的基础包
	       String basePack = "com.boot.spring.po";
	       Set<BeanDefinition> beanDefinitionSet = classScanner.findCandidateComponents(basePack);
	       for (BeanDefinition beanDefinition : beanDefinitionSet) {
	           if (beanDefinition instanceof AnnotatedBeanDefinition) {
	               registerBeans(((AnnotatedBeanDefinition) beanDefinition));
	           }
	       }
	   } 
	   
	   /**
	    * 创建动态代理，并动态注册到容器中
	    *
	    * @param annotatedBeanDefinition
	    */
	   private void registerBeans(AnnotatedBeanDefinition annotatedBeanDefinition) {
	       String className = annotatedBeanDefinition.getBeanClassName();
	       ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(className, createProxy(annotatedBeanDefinition));
	   }
	   
	   /**
	    * 构造Class扫描器，设置了只扫描顶级接口，不扫描内部类
	    *
	    * @return
	    */
	   private ClassPathScanningCandidateComponentProvider getClassScanner() {
	       return new ClassPathScanningCandidateComponentProvider(false, this.environment);
	   }

	   /**
	    * 创建动态代理
	    *
	    * @param annotatedBeanDefinition
	    * @return
	    */
	   private Object createProxy(AnnotatedBeanDefinition annotatedBeanDefinition) {
	       try {
	           AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
	           Class<?> target = Class.forName(annotationMetadata.getClassName());
	           InvocationHandler invocationHandler = createInvocationHandler();
	           Object proxy = Proxy.newProxyInstance(Person.class.getClassLoader(), target.getInterfaces(), invocationHandler);
	           return proxy;
	       } catch (ClassNotFoundException e) {
	           log.error(e.getMessage());
	       }
	       return null;
	   }

	   /**
	    * 创建InvocationHandler，将方法调用全部代理给DemoHttpHandler
	    *
	    * @return
	    */
	   private InvocationHandler createInvocationHandler() {
	       return new InvocationHandler() {
		
		           @Override
		           public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						People p = new People();
		               return method.invoke(p, args);
		           }
			
		};
	   }

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		
	}




}
