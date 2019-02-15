package com.cloud.spring.hystrix;

import org.springframework.web.client.RestTemplate;

import com.cloud.spring.po.User;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;

public class UserObservableCommand extends HystrixObservableCommand<User>{

	private RestTemplate restTemplate;
	private long id;
	
	public UserObservableCommand(Setter setter,RestTemplate restTemplate,Long id) {
		super(setter);
		this.restTemplate=restTemplate;
		this.id=id;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Observable<User> construct() {
		return 
				Observable.create(s->{
			try {
				if (s.isUnsubscribed()) {
					User user = restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
					System.out.println(user);
					s.onNext(user);
					s.onCompleted();
				} 
			} catch (Exception e) {
				s.onError(e);
			}
		});
	}
	
	
}
