package com.atguigu.jdbc.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

public class TransactionTest {
	/**
	 * ��������ĸ��뼶�� �� JDBC �����п���ͨ�� Connection �� setTransactionIsolation ����������ĸ��뼶��.
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Tom �� Jerry ��� 500 Ԫ.
	 * 
	 * ��������: 1. ����������, ÿ������ʹ�õ����Լ��ĵ���������, ���޷���֤����. 2. ���岽��: 1). ���������ʼǰ, ��ʼ����:
	 * ȡ�� Connection ��Ĭ���ύ��Ϊ. connection.setAutoCommit(false); 2). �������Ĳ������ɹ�,
	 * ���ύ����: connection.commit(); 3). �ع�����: �������쳣, ���� catch ���лع�����:
	 */
	/**
	 * 
	 */
	@Test
	public void TestTransaction() {
		Connection connection = null;
		PreparedStatement ps = null;

		try {

			connection = JDBCTools.getConnection();
			// ��ʼ����: ȡ��Ĭ���ύ.
			connection.setAutoCommit(false);
			String sql1 = "update emp1 set salary = salary +100 where employee_id = 1001";
			ps = connection.prepareStatement(sql1);
			ps.executeUpdate();

//			int i = 10 / 0;
			String sql2 = "update emp1 set salary = salary-100 where employee_id = 1002";
			ps = connection.prepareStatement(sql2);
			ps.executeUpdate();
			// �ύ����
			connection.commit();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// �ع�����
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			JDBCTools.release(null, ps, connection);
		}

	}

}
