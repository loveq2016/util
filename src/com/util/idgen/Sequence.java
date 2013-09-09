package com.util.idgen;

import java.text.SimpleDateFormat;
import java.util.*;

public class Sequence {
	/**
	* 取得当前时间精确到毫秒 格式："yyyyMMddHHmmssSSS"，并联合5位随机数
	*/
	public synchronized static String createByTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String timeIndex = formatter.format(new Date());
		int t = new Random().nextInt(999999);  
		timeIndex += Integer.valueOf(t).toString().substring(1);
		return timeIndex;
	}	
	
	/**
	* 取得系统UUID
	*/
	public synchronized static String createByUuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}		
	
	/**
	* 创建15位系统id
	*/
	public synchronized static String createId() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		String id = formatter.format(new Date());
		char[] idList = id.substring(1).toCharArray();		
		Random r = new Random();
		id= r.nextInt(10)
			+String.valueOf(idList[0])
			+String.valueOf(idList[13])
			+String.valueOf(idList[1])
			+String.valueOf(idList[12])
			+String.valueOf(idList[2])
			+String.valueOf(idList[11])
			+String.valueOf(idList[3])
			+String.valueOf(idList[10])
			+String.valueOf(idList[4])
			+String.valueOf(idList[9])
			+String.valueOf(idList[5])
			+String.valueOf(idList[8])
			+String.valueOf(idList[6])
			+String.valueOf(idList[7]);
		return id;
	}	
	public static void main(String[] args) throws Exception {
		System.out.println("Sequence.createByUuid()："+ Sequence.createByUuid());
		System.out.println("Sequence.createByTime()："+ Sequence.createByTime());
		System.out.println("Sequence.createId()："+ Sequence.createId());
	}
}
