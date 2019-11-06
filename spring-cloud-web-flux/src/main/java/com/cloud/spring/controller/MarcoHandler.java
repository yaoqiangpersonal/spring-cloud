package com.cloud.spring.controller;



import org.springframework.web.reactive.HandlerResult;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MarcoHandler implements WebSocketHandler {
	
	private WebSocketSession session;
	 
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		this.session = session;
		return session
                .send(session.receive()
                        .map(msg -> "RECEIVED ON SERVER :: " + msg.getPayloadAsText())
                        .map(session::textMessage)
                    );
	}
	
	public Mono<Void> send(){
		return session.send(Mono.just(session.textMessage("123")));
	}
	
	

	
	
}
