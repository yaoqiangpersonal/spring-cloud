package com.cloud.spring.service;

import java.util.Date;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;


import org.springframework.integration.annotation.*;


@EnableBinding({Source.class,Sink.class})
public class SendService {
	

	
	@Bean
	@InboundChannelAdapter(value = Source.OUTPUT,poller=@Poller(fixedDelay="2000"))
	public MessageSource<Date> send(){
		return ()->new GenericMessage<>(new Date());
	}
    
	@StreamListener(Sink.INPUT)
	public void run(Object payload) {
		System.out.println("return: " + payload);
	}

    

}

