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
 * ���� JDBC �Ĺ�����. ���з�װ��һЩ���߷��� Version 1
 */

public class JDBCTools {
	/**
	 * ��̬��ȡ��ͬ���ݱ��select��ѯ����䣬������ѯ�����װ�ڶ�Ӧ�Ķ�����
	 * 
	 * @param clazz��̬������ͬ���󣬸���ͬ�������Ը�ֵ
	 * @param sql
	 *            Ҫִ�е�select���
	 * @param args
	 *            sql�����ռλ����Ӧ���β�
	 * @return
	 * @throws Exception
	 */

	public static <T> T getSelectObject(Class<T> clazz, String sql, Object... args) throws Exception {
		// 1.������ԴConnection PreparedStatement ResultSet
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		T t = null;
		try {
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			// 2.���sql����ִ��
			// 2.1ȷ��sql���
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// 2.2ִ����䣬����һ��Resultset �����
			rs = ps.executeQuery();

			// 3.��������(���ﴦ����Ƿ���һ����¼)
			// 3.1����Resultset �ķ����õ���Ԫ���ݶ���
			// --��ResultsetMetaData����(��֪������е��еı����������)��
			// ��װ��Map(�еı���,value)��
			// 3.2����map��key-value�����÷����T������������ԣ��еı�������ֵ
			Map<String, Object> values = new HashMap<>();

			if (rs.next()) {
				// ��ʽ���� System.out.println("xxxx");
				t = clazz.newInstance();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					Object value = rs.getObject(i + 1);
					String label = rsmd.getColumnLabel(i + 1);
					System.out.println(rsmd.getColumnTypeName(i + 1));
					// ��ν�BigDecimal ����Ҫת��int����
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
	 *            Ԥ�����Ҫ�õ�sql��䣬
	 * @param args
	 *            ���ռλ��Ҫ�õ��β��б�
	 * @throws Exception
	 */

	public static void upDateData(String sql, Object... args) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			// �ж���ռλ���������β��б�ʹ��ݶ����βΡ�
			// ռλ���Ǵ�1��ʼ����
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// PrepareStatement��Statement���ӽӿڣ�
			JDBCTools.release(ps, connection);
		}

	}

	/**
	 * ��Statement�� ʵ�����ݿ�ĸ��£�����insert����ɾ����delete�����޸ģ�update��
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
	 * ��������Connection,ͨ����ȡ�����ļ������ݿ��������ȡһ������.
	 * ʹ�þ�̬�����ݿ����ӳأ�����ֻ��Ҫά��һ�����ݿ����ӳؾ͹���
	 */
	private static DataSource dataSource = null;
	static {
		dataSource = new ComboPooledDataSource("helloc3p0");

	}

	public static Connection getConnection() throws SQLException {

		return dataSource.getConnection();
	}

	/**
	 * �ر� Statement �� Connection
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
	 * �ر� ResultSet,Statement �� Connection
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
