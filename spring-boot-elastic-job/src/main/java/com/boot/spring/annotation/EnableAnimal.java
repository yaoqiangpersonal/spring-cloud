package com.boot.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.boot.demoT.CustomApplicationContextAware2;
import com.boot.demoT.CustomBeanDefinitionRegistryPostProcessor;
import com.boot.demoT.CustomImportBeanDefinitionRegistrar;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CustomApplicationContextAware2.class)
public @interface EnableAnimal {

}
