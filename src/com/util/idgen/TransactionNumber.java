package com.util.idgen;

import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

/**
 * * 交易流水号 * * *
 * 
 * @author Song Qing *
 */
public class TransactionNumber {
	/** * 生成日期 */
	private static Date BUILD_DATE = new Date();

	/** * 当前序列号 */
	private static long CURRENT_NUMBER = 0;

	/**
	 * 生成序列号
	 * @return 序列号；按天生成、从1开始、隔天重置
	 */

	private static long generate() {
		Date current = new Date();
		if (DateUtils.isSameDay(current, BUILD_DATE)) {// 同一天，则序列号递增1
			CURRENT_NUMBER++;
		} else {
			// 非同天，则序列号重置，生成日期重置
			CURRENT_NUMBER=1;                         
			BUILD_DATE=current;                  
			}                  
		return CURRENT_NUMBER;        
		}      
	
	/**
	 * 格式化序列号 
	 * @param serialNumber 
	 * @return 8位序列号；低位数前面用0补齐
	 */         
	private static String format(long serialNumber,Integer serial) {   
		String format = "%0"+serial+"d";
		return String.format(format, serialNumber);          
    }     
	
	/**           
	 * 生成交易流水号
     * @param apId 应用系统标识           
     * @param timeStamp     时间戳                
     * @return 交易流水号；应用平台ID(8位)+yyyymmdd(8位)+自增序列号(8位)           
     */          
	public static String generateDefault(){                  
		return format(generate(),12);          
	}
	
	/**           
	 * 生成交易流水号
     * @param apId 应用系统标识           
     * @param timeStamp     时间戳                
     * @return 交易流水号；应用平台ID(8位)+yyyymmdd(8位)+自增序列号(8位)           
     */          
	public static String generate(Integer serial){                  
		return format(generate(),serial);          
	}
		
	public static void main(String[] args) {
		System.out.println(generate(14));
	}
}
