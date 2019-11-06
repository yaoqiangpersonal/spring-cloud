package com.cloud.spring.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;

import com.cloud.spring.po.User;
import com.cloud.spring.service.BasicService;

import reactor.core.publisher.Mono;

@RestController
public class BasicController {
	
	@Autowired
	private BasicService basicService;
	
	@Autowired
	private WebSocketSession session;

	
    @GetMapping("/hello_world")
    public Mono<Void> sayHelloWorld() {
		session.send(Mono.just(session.textMessage("您好")));
   return Mono.empty();
    }
}

