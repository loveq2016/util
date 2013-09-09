package com.util.jdbc;

import static com.util.print.Print.print;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.NamingException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.util.string.StringUtil;
import com.util.xml.Dom4jUtil;

public class JdbcUtil<T> {

	private String url = null;
	private String userName = null;
	private String password = null;
	private String driver = null;
	Connection conn;
	private Properties props = new Properties();

	public JdbcUtil(InputStream inStream) {
		try {
			props.load(inStream);
		} catch (Exception e) {
		}
		url = props.getProperty("jdbc.url");
		userName = props.getProperty("jdbc.userName");
		password = props.getProperty("jdbc.password");
		driver = props.getProperty("jdbc.driver");

		// 注册驱动类
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {

		}
	}

	public JdbcUtil(String url, String userName, String password, String driver) {
		this.url = url;
		this.userName = userName;
		this.password = password;
		try {
			Class.forName(driver);
			this.conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getRemarks(String tableName)
			throws SQLException, NamingException {

		DatabaseMetaData databaseMetaData = getConnection().getMetaData();
		// 获取所有表
		// ResultSet tableSet = databaseMetaData.getTables(null, "%", "%",
		// new String[]{"TABLE"});
		// 获取tableName表列信息

		ResultSet columnSet = databaseMetaData.getColumns(null, "%", tableName,
				"%");

		Map<String, String> map = new HashMap<String, String>();
		if (null != columnSet) {
			while (columnSet.next()) {
				// 列名
				String columnName = columnSet.getString("COLUMN_NAME");
				// 备注
				String columnComment = columnSet.getString("REMARKS");
				// 列类型
				int sqlType = columnSet.getInt("DATA_TYPE");

				String[] array = columnName.split("_");
				String key = "";
				if (array.length == 1) {
					key = array[0];
				} else {
					for (int i = 0; i < array.length; i++) {
						if (i == 0) {
							key += StringUtil.firstLetterToLowerCase(array[i]);
						} else {
							key += StringUtil.firstLetterToUpperCase(array[i]);
						}
					}
				}
				map.put(key, columnComment);
				System.out.println(columnName + columnComment + sqlType);
			}
		}
		return map;

	}

	public void validate(String tableName) throws Exception {
		String sql = "select * from " + tableName;
		PreparedStatement stmt = getConnection().prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		ResultSetMetaData data = rs.getMetaData();
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= data.getColumnCount(); i++) {
			// 获得指定列的列名
			String columnName = data.getColumnName(i);
			System.out.println(columnName + "========="+ data.isNullable(i) + "========="+ data.getColumnDisplaySize(i) + "========="+  data.isAutoIncrement(i));
			list.add(columnName);
		}
	}
	public List<String> getTabelNames() {
		List<String> list = new ArrayList<String>();
		DatabaseMetaData dbMetaData;
		try {
			dbMetaData = conn.getMetaData();
			String catalog = conn.getCatalog(); // catalog 其实也就是数据库名
			ResultSet tablesResultSet = dbMetaData.getTables(catalog, null,
					null, new String[] { "TABLE" });
			while (tablesResultSet.next()) {
				list.add(tablesResultSet.getString("TABLE_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static void main(String[] args) throws Exception, NamingException {
		JdbcUtil ju = new JdbcUtil("jdbc:mysql://127.0.0.1:3306/base", "root",
				"fuwei", "com.mysql.jdbc.Driver");
		/*
		 * List<String> tables = ju.getTabelNames(); for (String string :
		 * tables) { System.out.println(ju.getFKColumnName(string)); }
		 */
		//ju.getRemarks();
		ju.getColumnNames("user");
		
		
	}

	public String getPKColumnName(String tableName) {
		DatabaseMetaData dbMetaData;
		try {
			dbMetaData = conn.getMetaData();
			String catalog = conn.getCatalog(); // catalog 其实也就是数据库名
			ResultSet primaryKeyResultSet = dbMetaData.getPrimaryKeys(catalog,
					null, tableName);
			while (primaryKeyResultSet.next()) {
				return primaryKeyResultSet.getString("COLUMN_NAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String FKCOLUMN_NAME = "fkColumnName";
	public static String PKTABLE_NAME = "pkTablenName";
	public static String PKCOLUMN_NAME = "pkColumnName";

	public List<Map<String, String>> getFKColumnName(String tableName) {
		DatabaseMetaData dbMetaData;
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			dbMetaData = conn.getMetaData();
			String catalog = conn.getCatalog(); // catalog 其实也就是数据库名
			ResultSet foreignKeyResultSet = dbMetaData.getImportedKeys(catalog,
					null, tableName);
			while (foreignKeyResultSet.next()) {
				map = new HashMap<String, String>();
				String fkColumnName = foreignKeyResultSet
						.getString("FKCOLUMN_NAME");
				String pkTablenName = foreignKeyResultSet
						.getString("PKTABLE_NAME");
				String pkColumnName = foreignKeyResultSet
						.getString("PKCOLUMN_NAME");
				map.put(FKCOLUMN_NAME, fkColumnName);
				map.put(PKTABLE_NAME, pkTablenName);
				map.put(PKCOLUMN_NAME, pkColumnName);
				list.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取链接
	 * 
	 * @return JDBC的链接
	 */
	public Connection getConnection() throws NamingException, SQLException {
		return DriverManager.getConnection(url, userName, password);
	}

	/**
	 * 查询sql
	 * 
	 * @param sql
	 * @param prepare
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private ResultSet query(Connection connection, String sql,
			Object... prepare) throws SQLException {

		PreparedStatement stmt = connection.prepareStatement(sql);
		for (int i = 0; i < prepare.length; i++) {
			Object prepareParam = prepare[i];
			stmt.setObject(i + 1, prepareParam, getType(prepareParam));
		}

		return stmt.executeQuery();
	}

	public Map<String, String> getRemarks() throws SQLException,
			NamingException {

		DatabaseMetaData databaseMetaData = getConnection().getMetaData();
		// 获取所有表
		// ResultSet tableSet = databaseMetaData.getTables(null, "%", "%",
		// new String[]{"TABLE"});
		// 获取tableName表列信息

		ResultSet columnSet = databaseMetaData.getColumns(null, "%", "user",
				"%");
		if (null != columnSet) {
			while (columnSet.next()) {
				// 列名
				String columnName = columnSet.getString("COLUMN_NAME");
				// 备注
				String columnComment = columnSet.getString("REMARKS");
				// 列类型
				int sqlType = columnSet.getInt("DATA_TYPE");

				System.out.println(columnName + columnComment);
			}
		}
		return null;

	}

	public List<String> getColumnNames(String table) throws Exception {
		String sql = "select * from " + table;
		PreparedStatement stmt = getConnection().prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		ResultSetMetaData data = rs.getMetaData();
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= data.getColumnCount(); i++) {
			// 获得指定列的列名
			String columnName = data.getColumnName(i);
			list.add(columnName);
		}
		return list;
	}

	public List<TableFieldInfo> getTableFieldInfo(String tableName) throws Exception {
		List<TableFieldInfo> list = new ArrayList<TableFieldInfo>();
		String sql = "select * from " + tableName;
		PreparedStatement stmt = getConnection().prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		ResultSetMetaData data = rs.getMetaData();
		Map<String, String> map = getRemarks(tableName);
		for (int i = 1; i <= data.getColumnCount(); i++) {
			
			String columnName = data.getColumnName(i);
			
			String[] array = columnName.split("_");
			String key = "";
			if (array.length == 1) {
				key = array[0];
			} else {
				for (int j = 0; j < array.length; j++) {
					if (j == 0) {
						key += StringUtil.firstLetterToLowerCase(array[j]);
					} else {
						key += StringUtil.firstLetterToUpperCase(array[j]);
					}
				}
			}
			
			TableFieldInfo tableFieldInfo = new TableFieldInfo();
			tableFieldInfo.setColumnName(key);
			tableFieldInfo.setColumnClassName(data.getColumnClassName(i));
			tableFieldInfo.setColumnDisplaySize(data.getColumnDisplaySize(i));
			tableFieldInfo.setNullable(data.isNullable(i));
			tableFieldInfo.setRemark(map.get(key));
			list.add(tableFieldInfo);
		}
		return list;
	}
	/**
	 * 执行sql
	 * 
	 * @param sql
	 * @param prepare
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private int executeUpdate(Connection connection, String sql,
			Object... prepare) throws SQLException {
		int i = 0;
		PreparedStatement stmt = connection.prepareStatement(sql);
		for (int j = 0; j < prepare.length; j++) {
			Object prepareParam = prepare[i];
			stmt.setObject(j + 1, prepareParam, getType(prepareParam));
		}
		i = stmt.executeUpdate();
		return i;
	}

	/**
	 * 返回参数的sql类型，这段代码很丑，如何改
	 * 
	 * @param prepareParam
	 * @return
	 */
	private int getType(Object prepareParam) {
		if (prepareParam == null)
			return Types.NULL;
		if (prepareParam instanceof String) {
			return Types.VARCHAR;
		} else if (prepareParam instanceof Boolean) {
			return Types.BOOLEAN;
		} else if (prepareParam instanceof Integer) {
			return Types.INTEGER;
		} else if (prepareParam instanceof Long) {
			return Types.BIGINT;
		} else if (prepareParam instanceof Float) {
			return Types.FLOAT;
		} else if (prepareParam instanceof Double) {
			return Types.DOUBLE;
		} else if (prepareParam instanceof BigDecimal) {
			return Types.NUMERIC;
		} else if (prepareParam instanceof Date) {
			return Types.TIMESTAMP;
		} else if (prepareParam instanceof Short) {
			return Types.SMALLINT;
		} else if (prepareParam instanceof InputStream) {
			return Types.BINARY;
		} else {
			throw new RuntimeException("没有此类型");
		}
	}

	/**
	 * 执行update的sql语句
	 * 
	 * @param sql
	 *            修改的update的sql代码,准备参数,连接
	 * @return
	 * @throws SQLException
	 */
	public int update(Connection connection, String sql, Object... prepare)
			throws SQLException {
		return executeUpdate(connection, sql, prepare);
	}

	/**
	 * 执行delete的sql语句
	 * 
	 * @param sql
	 *            修改的delete的sql代码,准备参数,连接
	 * @return
	 * @throws SQLException
	 */
	public int delete(Connection connection, String sql, Object... prepare)
			throws SQLException {
		return executeUpdate(connection, sql, prepare);
	}

	/**
	 * 执行insert的sql语句
	 * 
	 * @param sql
	 *            修改的insert语句,准备参数,连接
	 * @return
	 * @throws SQLException
	 */
	public int insert(Connection connection, String sql, Object... prepare)
			throws SQLException {
		return executeUpdate(connection, sql, prepare);
	}

	/**
	 * 执行select的sql语句
	 * 
	 * @param sql
	 *            修改的select语句
	 * @return
	 * @throws SQLException
	 */
	public ResultSet find(Connection connection, String sql, Object... prepare)
			throws SQLException {
		return query(connection, sql, prepare);
	}

	@SuppressWarnings("unchecked")
	public List<T> toList(ResultSet rs, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			// 获取数据库表结构
			ResultSetMetaData meta = rs.getMetaData();
			Object obj = null;
			while (rs.next()) {
				// 获取formbean实例对象
				obj = Class.forName(cls.getName()).newInstance();
				// 循环获取指定行的每一列的信息
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					// 当前列名
					String colName = meta.getColumnName(i);
					// 将列名第一个字母大写（为什么加+""呢？为了把char类型转换为String类型。replace的参数是String类型。）
					colName = colName.replace(colName.charAt(0) + "",
							new String(colName.charAt(0) + "").toUpperCase());
					// 设置方法名
					String methodName = "set" + colName;
					// 获取当前位置的值，返回Object类型
					Object value = rs.getObject(i);
					// 利用反射机制，生成setXX()方法的Method对象并执行该setXX()方法。
					Method method = obj.getClass().getMethod(methodName,
							value.getClass());
					method.invoke(obj, value);
				}
				list.add((T) obj);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return list;

	}

}
