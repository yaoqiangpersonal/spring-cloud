package com.cloud.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.spring.service.SendService;

@RestController
public class ProducerController {
    
    @Autowired
    private SendService service;
    
    @GetMapping("/send/{msg}")
    public void send(@PathVariable("msg") String msg){
        //service.sendMessage(msg);
    }
	

}
