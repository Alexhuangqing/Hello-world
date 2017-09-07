package com.atguigu.jdbc.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

public class TransactionTest {
	/**
	 * 测试事务的隔离级别 在 JDBC 程序中可以通过 Connection 的 setTransactionIsolation 来设置事务的隔离级别.
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Tom 给 Jerry 汇款 500 元.
	 * 
	 * 关于事务: 1. 如果多个操作, 每个操作使用的是自己的单独的连接, 则无法保证事务. 2. 具体步骤: 1). 事务操作开始前, 开始事务:
	 * 取消 Connection 的默认提交行为. connection.setAutoCommit(false); 2). 如果事务的操作都成功,
	 * 则提交事务: connection.commit(); 3). 回滚事务: 若出现异常, 则在 catch 块中回滚事务:
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
			// 开始事务: 取消默认提交.
			connection.setAutoCommit(false);
			String sql1 = "update emp1 set salary = salary +100 where employee_id = 1001";
			ps = connection.prepareStatement(sql1);
			ps.executeUpdate();

//			int i = 10 / 0;
			String sql2 = "update emp1 set salary = salary-100 where employee_id = 1002";
			ps = connection.prepareStatement(sql2);
			ps.executeUpdate();
			// 提交事务
			connection.commit();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 回滚事务
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
