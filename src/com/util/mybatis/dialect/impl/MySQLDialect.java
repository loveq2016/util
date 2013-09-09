package com.util.mybatis.dialect.impl;

import com.util.mybatis.dialect.Dialect;

public class MySQLDialect extends Dialect {


	@Override
	public String getPageSql(String sql, int offset, int limit) {
		if (offset > 0) {
			return sql + " limit " + offset + ", " + limit;
		}
		return sql + " limit " + limit;
	}
	
}