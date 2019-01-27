package com.cloud.spring;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class DemoTest {
	
	@Test
	public void run() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		
		System.out.println(StringUtils.join(list));
		
	}

}
