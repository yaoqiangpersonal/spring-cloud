package com.cloud.spring.observable;

public class Demo2 {
	
	private final static int _1MB = 1024 * 1024;
	
	public static void main(String[] args) {
		byte[] allocation4 = new byte[5 * _1MB];
		System.out.println(allocation4);
	}

}
