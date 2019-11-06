package com.cloud.spring;


import java.util.Arrays;

import java.util.List;

import java.util.concurrent.TimeUnit;

import java.util.stream.Collectors;

import org.jsoup.Jsoup;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;




import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import reactor.core.publisher.Mono;

public class TestDemo {
	private static int  count;
	
	 private static List<String> words = Arrays.asList(
		        "the",
		        "quick",
		        "brown",
		        "fox",
		        "jumped",
		        "over",
		        "the",
		        "lazy",
		        "dog"
		        );
	
	public static void main(String[] args) throws InterruptedException {
WebClient.Builder builder = WebClient.builder().baseUrl("https://camelcamelcamel.com/product/");
		
		for(int i = 0;i < 100;i++) {
			Mono<String> mono = 
			builder
	
			.build()
			
			.method(HttpMethod.GET)

			.uri("/B07D4ZSQ7B")
			
			.retrieve()
			.bodyToMono(String.class);
			
		}

		TimeUnit.SECONDS.sleep(100);
	}
	
	@Test
	public void tt() throws InterruptedException {
WebClient.Builder builder = WebClient.builder().baseUrl("https://camelcamelcamel.com/product/");
		

Disposable dis = builder
	
			.build()
			
			.method(HttpMethod.GET)

			.uri("/B07D4ZSQ7B")

			.retrieve()

			.onStatus(i->i==HttpStatus.MOVED_PERMANENTLY, TestDemo::twoHandle)
			
			.bodyToMono(String.class)

			.subscribe(System.out::println);
			
			TimeUnit.SECONDS.sleep(100);
			


	}
	
	private static Mono<? extends Throwable> twoHandle(ClientResponse response){
		response.bodyToMono(String.class).subscribe(i->{
			String url = Jsoup.parse(i).select("a").get(0).attr("href");
			WebClient
				.create(url)
				.get()
				.retrieve()

				.bodyToMono(String.class)
				.subscribe(TestDemo::handle);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return Mono.empty();
		
	}
	
	
	private static void handle(String html) {
		System.out.println(++count);

		Flux.fromIterable(Jsoup.parse(html).select("#section_amazon .pad .product_pane tbody tr"))
			.take(3)
			.map(e->{
				String price = e.select("td").get(1).text();
				return price;
			}).collect(Collectors.toList()).subscribe(System.out::println);
			
	}
	

	

}
