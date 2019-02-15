package com.cloud.spring.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthorizationController {
	
	@GetMapping("demo")
	public ModelAndView authorization() {
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}

}
