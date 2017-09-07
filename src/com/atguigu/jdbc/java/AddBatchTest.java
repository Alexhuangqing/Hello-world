package com.atguigu.jdbc.java;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class AddBatchTest {

	/**
	 * 向 Oracle 的 customers 数据表中插入 10 万条记录 测试如何插入, 用时最短. 2. 使用 Statement.
	 */
	@Test
	public void testAddBatch() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JDBCTools.getConnection();
			connection.setAutoCommit(false);
			String sql = "insert into  student  values(?)";
			ps = connection.prepareStatement(sql);
			long start = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {

				ps.setBigDecimal(1, new BigDecimal(i + 1));
				// “积攒” sql语句
				ps.addBatch();
				// 积攒到一定程度 统一执行，执行完以后，容器清空
				if ((i + 1) % 300 == 0) {// 通过循环的次数，来知道积攒sql语句的条数
					ps.executeBatch();//统一处理
					ps.clearBatch();//一定要清空
				}
			}
			if (100000 % 300 != 0) {
				ps.executeBatch();
				ps.clearBatch();
			}
			long end = System.currentTimeMillis();
			System.out.println("time:" + (end - start));// time:358
			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			JDBCTools.release(ps, connection);

		}

	}

	/**
	 * 向 Oracle 的 customers 数据表中插入 10 万条记录 测试如何插入, 用时最短. 2. 使用 Statement.
	 */
	@Test
	public void testInsertPreparedStatement() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JDBCTools.getConnection();
			connection.setAutoCommit(false);
			String sql = "insert into  student  values(?)";
			ps = connection.prepareStatement(sql);
			long start = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {

				ps.setBigDecimal(1, new BigDecimal(i + 1));
				ps.executeQuery();
			}
			long end = System.currentTimeMillis();
			System.out.println("time:" + (end - start));// time:22263
			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			JDBCTools.release(ps, connection);

		}

	}

	/**
	 * 向 Oracle 的 customers 数据表中插入 10 万条记录 测试如何插入, 用时最短. 1. 使用 Statement.
	 */
	@Test
	public void testInsertStatement() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = JDBCTools.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			long start = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				String sql = "insert into  student  values(" + (i + 1) + ")";// 这里的（i+1）是要改变
																				// 不能写死，放到语句引号外面

				statement.executeUpdate(sql);
			}
			long end = System.currentTimeMillis();
			System.out.println("time:" + (end - start));// time:72045
			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			JDBCTools.release(statement, connection);

		}

	}

}
