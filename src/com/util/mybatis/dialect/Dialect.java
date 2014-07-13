package com.util.mybatis.dialect;

/**
 * 数据库方音接口
 * @author fuwei
 * @date 2013-4-24 上午10:45:40
 * @modifyNote
 * @version 1.0
 */
public abstract class Dialect {
	
	public final static  String MYSQL = "mysql"; 
	
	public final static  String ORACLE = "oracle"; 
	
	
	public abstract String getPageSql(String sql,int offset,int limit);
}