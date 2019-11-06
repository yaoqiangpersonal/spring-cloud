package com.cloud.spring;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class ObserverImpl extends Observable {

	private final Observable observable;
	
	private final long time;
	
	private final String msg;
	
	public ObserverImpl(Observable observable,long time,String msg) {
		this.observable = observable;
		this.time = time;
		this.msg = msg;
	}

	
	
	@Override
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
		setChanged();
		notifyObservers(msg);
	}



	@Override
	public void notifyObservers() {
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		observable.notifyObservers();
	}

	@Override
	public void notifyObservers(Object arg) {
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.notifyObservers(arg);
	}
	
	
	
	
	
}
