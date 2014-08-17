package com.util.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.util.date.DateUtils;

public class RunTimeTest {

	private Long startTime;

	private Long endTime;

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void endAndPrintMilliSeconds() {
		endTime = System.currentTimeMillis();
		LOG.info(endTime - startTime + "ms");
	}
	
	public void endAndPrintSeconds() {
		endTime = System.currentTimeMillis();
		LOG.info(DateUtils.formatLongToTime(endTime - startTime));
	}
	
	public Long end() {
		endTime = System.currentTimeMillis();
		return (endTime - startTime);
	}
	
	private final static Log LOG = LogFactory.getLog(RunTimeTest.class);
}
