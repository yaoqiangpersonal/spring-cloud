package com.cloud.spring.websocket.tomcat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.spring.websocket.tomcat.po.Shout;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MaroController {

    @Autowired
    private SimpMessagingTemplate wsTemplate;
	
    @ResponseBody
	@MessageMapping("marco")
	@SendTo("/topic/shout")
	public Shout handleShout(@RequestBody Shout incoming) {
		log.info("received message: " + incoming.getMessage());
		Shout outgoing = new Shout();
		outgoing.setMessage("Polo");
		return outgoing;
	}	 

}
