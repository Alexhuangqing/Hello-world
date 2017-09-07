package com.atguigu.jdbc.java;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class AddBatchTest {

	/**
	 * �� Oracle �� customers ���ݱ��в��� 10 ������¼ ������β���, ��ʱ���. 2. ʹ�� Statement.
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
				// �����ܡ� sql���
				ps.addBatch();
				// ���ܵ�һ���̶� ͳһִ�У�ִ�����Ժ��������
				if ((i + 1) % 300 == 0) {// ͨ��ѭ���Ĵ�������֪������sql��������
					ps.executeBatch();//ͳһ����
					ps.clearBatch();//һ��Ҫ���
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
	 * �� Oracle �� customers ���ݱ��в��� 10 ������¼ ������β���, ��ʱ���. 2. ʹ�� Statement.
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
	 * �� Oracle �� customers ���ݱ��в��� 10 ������¼ ������β���, ��ʱ���. 1. ʹ�� Statement.
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
				String sql = "insert into  student  values(" + (i + 1) + ")";// ����ģ�i+1����Ҫ�ı�
																				// ����д�����ŵ������������

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
