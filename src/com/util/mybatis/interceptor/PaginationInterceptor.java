/**
 * Create by fuwei
 * Since 2013-4-24上午10:48:38
 */
package com.util.mybatis.interceptor;

import java.sql.Connection;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;

import com.util.mybatis.dialect.Dialect;
import com.util.mybatis.dialect.impl.MySQLDialect;
import com.util.mybatis.dialect.impl.OracleDialect;

/**
 * @author fuwei
 * @date 2013-4-24 上午10:48:38
 * @modifyNote
 * @version 1.0
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class PaginationInterceptor implements Interceptor {

	private String databaseType;
	
	/**
	 * @author fuwei
	 * @date 2013-4-24 上午10:49:11
	 * @modifyNote
	 * @param arg0
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler);
		RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
		if(rowBounds ==null|| rowBounds == RowBounds.DEFAULT){
	        return invocation.proceed();
	    }
		Dialect dialect = null;
		if (Dialect.MYSQL.equalsIgnoreCase(databaseType)) {
			dialect = new MySQLDialect();
		} if (Dialect.ORACLE.equalsIgnoreCase(databaseType)) {
			dialect = new OracleDialect();
		} else {
			dialect = new MySQLDialect();
		}
		String sql = (String)metaStatementHandler.getValue("delegate.boundSql.sql");
		metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getPageSql(sql, rowBounds.getOffset(), rowBounds.getLimit()));
		metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET );
		metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT );
		return invocation.proceed();
	}

	/**
	 * @author fuwei
	 * @date 2013-4-24 上午10:49:11
	 * @modifyNote
	 * @param arg0
	 * @return
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * @author fuwei
	 * @date 2013-4-24 上午10:49:50
	 * @modifyNote
	 * @param arg0
	 */
	@Override
	public void setProperties(Properties properties) {
		databaseType = properties.getProperty("dialect");
	}

	
}
