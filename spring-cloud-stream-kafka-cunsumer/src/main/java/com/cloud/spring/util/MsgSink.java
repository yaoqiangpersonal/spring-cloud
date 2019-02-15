
package com.cloud.spring.util;


import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.Transformer;

import reactor.core.publisher.Flux;


@EnableBinding({Sink.class,Source.class})
public class MsgSink {
	

	@StreamListener
	@Output(Source.OUTPUT)
	public Flux<String> receive(@Input(Processor.INPUT) Flux<String> input) {
		return input.map(data->{
			System.out.println("received: " + data);
			return data;
		}).buffer(5).map(data-> String.valueOf("from input channel return -" + data));
		
		//System.out.println("receive consumer" + payload);
		//return "finish";
	}
	
	@Transformer(inputChannel=Processor.INPUT,outputChannel=Processor.INPUT)
	public Object transform(Object message) {
		System.out.println("......");
		return message;
	}
	

	

}
