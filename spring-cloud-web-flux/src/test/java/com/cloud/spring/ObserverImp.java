package com.cloud.spring;

import java.util.Observable;
import java.util.Observer;

public class ObserverImp implements Observer{

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("update:" + arg);
	}
	
	

}
