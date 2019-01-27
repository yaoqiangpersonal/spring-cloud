package com.cloud.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.springframework.stereotype.Component;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

@Component
@WebFilter(filterName = "hystrixRequestContextServletFilter", urlPatterns = "/*", asyncSupported = true)
public class HystrixRequestContextServletFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		chain.doFilter(request, response);
        context.close();
		
	}
	 
}
