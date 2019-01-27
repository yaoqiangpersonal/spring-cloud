package com.cloud.spring.observable;

import rx.Observable;
import rx.Subscriber;

public class Demo {
	public static void main(String[] args) {
		Observable<String> observable = Observable.create(Demo::demo);
		Subscriber<String> subscriber = new Subscriber<String>() {
			@Override
			public void onNext(String t) {
				System.out.println("Subscriber:" + t);
			}
			
			@Override
			public void onCompleted() {
				
			}
			
			@Override
			public void onError(Throwable e) {
				
			}
		};
		observable.subscribe(subscriber);
	}
	
	public static void demo(Subscriber<? super String> subscriber) {
		subscriber.onNext("hello");
		subscriber.onNext("nothing");
		subscriber.onCompleted();
	}

}
