package com.util.test;

import com.util.date.DateUtil;

public class RunTimeTest {

	private Long startTime;

	private Long endTime;

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void endAndPrintMilliSeconds() {
		endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime + "ms");
	}
	
	public void endAndPrintSeconds() {
		endTime = System.currentTimeMillis();
		System.out.println(DateUtil.formatLongToTime(endTime - startTime));
	}
	
	public Long end() {
		endTime = System.currentTimeMillis();
		return (endTime - startTime);
	}
}
