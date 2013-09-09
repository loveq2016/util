package com.util.test;

public class MethodTest {

	private Long startTime;
	
	private Long endTime;
	
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void end() {
		endTime = System.currentTimeMillis();
		System.out.println(endTime-startTime + "ms"); 
	}

}
