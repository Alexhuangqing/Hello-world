package com.atguigu.jdbc.java;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 操作 JDBC 的工具类. 其中封装了一些工具方法 Version 1
 */

public class JDBCTools {
	/**
	 * 动态获取不同数据表的select查询的语句，并将查询结果封装在对应的对象中
	 * 
	 * @param clazz动态创建不同对象，给不同对象属性赋值
	 * @param sql
	 *            要执行的select语句
	 * @param args
	 *            sql语句中占位符对应的形参
	 * @return
	 * @throws Exception
	 */

	public static <T> T getSelectObject(Class<T> clazz, String sql, Object... args) throws Exception {
		// 1.开启资源Connection PreparedStatement ResultSet
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		T t = null;
		try {
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			// 2.完成sql语句的执行
			// 2.1确定sql语句
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// 2.2执行语句，返回一个Resultset 结果集
			rs = ps.executeQuery();

			// 3.处理结果集(这里处理的是返回一条记录)
			// 3.1利用Resultset 的方法得到其元数据对象
			// --》ResultsetMetaData对象(可知结果集中的列的别名，与个数)，
			// 封装成Map(列的别名,value)；
			// 3.2遍历map中key-value，利用反射给T的例对象的属性（列的别名）赋值
			Map<String, Object> values = new HashMap<>();

			if (rs.next()) {
				// 调式代码 System.out.println("xxxx");
				t = clazz.newInstance();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					Object value = rs.getObject(i + 1);
					String label = rsmd.getColumnLabel(i + 1);
					System.out.println(rsmd.getColumnTypeName(i + 1));
					// 如何将BigDecimal 这里要转成int数据
					if (value instanceof BigDecimal) {
						value = ((BigDecimal) value).intValue();
					}
					values.put(label, value);
				}
				System.out.println(values);
				for (Map.Entry<String, Object> entry : values.entrySet()) {
					System.out.println("xxxx");
					ReflectionUtils.setFieldValue(t, entry.getKey(), entry.getValue());

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTools.release(rs, ps, connection);
		}

		return t;
	}

	/**
	 * 
	 * @param sql
	 *            预编译的要用的sql语句，
	 * @param args
	 *            填充占位符要用的形参列表
	 * @throws Exception
	 */

	public static void upDateData(String sql, Object... args) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			// 有多少占位符？，就形参列表就传递多少形参。
			// 占位符是从1开始计数
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// PrepareStatement是Statement的子接口，
			JDBCTools.release(ps, connection);
		}

	}

	/**
	 * 用Statement类 实现数据库的更新：增（insert），删除（delete），修改（update）
	 * 
	 * @param sql
	 * @throws Exception
	 */

	public static void updateData(String sql) throws Exception {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = JDBCTools.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			JDBCTools.release(statement, connection);
		}
	}

	/**
	 * 建立链接Connection,通过读取配置文件从数据库服务器获取一个连接.
	 * 使用静态的数据库连接池，表明只需要维护一个数据库连接池就够了
	 */
	private static DataSource dataSource = null;
	static {
		dataSource = new ComboPooledDataSource("helloc3p0");

	}

	public static Connection getConnection() throws SQLException {

		return dataSource.getConnection();
	}

	/**
	 * 关闭 Statement 和 Connection
	 * 
	 * @param statement
	 * @param conn
	 */
	public static void release(Statement statement, Connection conn) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭 ResultSet,Statement 和 Connection
	 * 
	 * @param statement
	 * @param conn
	 * @param rs
	 * 
	 */
	public static void release(ResultSet rs, Statement statement, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
