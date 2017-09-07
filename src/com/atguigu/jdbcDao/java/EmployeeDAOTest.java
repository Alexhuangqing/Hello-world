package com.atguigu.jdbcDao.java;

import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.Test;

import com.atguigu.jdbc.java.JDBCTools;

public class EmployeeDAOTest {
	EmployeeDAO dao = new EmployeeDAO();

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet() {
		Connection connection = null;
		String sql = "select EMPLOYEE_ID \"id\" , SALARY \"sal\" , EMAIL \"email\"  from  emp1 ";
		try {
			connection = JDBCTools.getConnection();
			Employee e = dao.get(connection, sql);
			System.out.println(e);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(null, connection);
		}
	}

	@Test
	public void testGetForList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetForValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testBatch() {
		fail("Not yet implemented");
	}

}
