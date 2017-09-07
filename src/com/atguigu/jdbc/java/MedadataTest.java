package com.atguigu.jdbc.java;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

public class MedadataTest {
	/**ResultSetMetaData 
	 * 描述结果集的元数据对象，可以得到结果集的基本信息
	 * 
	 * 如列名columnName,列的别名columnLabel,结果集中列的个数columnCount，结果集中列的类型columnTypeName
	 */
	@Test
	public void TestResultSetMetaData(){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "select EMPLOYEE_ID   ,SALARY  , EMAIL  from emp1";
			ps = connection.prepareStatement(sql);
//			警告：这里不能乱用占位符，占位符主要应用于筛选条件，对应数据库中value
//			ps.setString(1,"EMPLOYEE_ID  id");
//			ps.setString(2,"SALARY  sal");
//			ps.setString(3,"EMAIL em");
			
			 rs = ps.executeQuery();
//			这里不需要取结果集表中的值，不需要调用rs.next()
			
			ResultSetMetaData rsmd = rs.getMetaData();
//			通过结果集的元数据对象 获取列的的个数
			int columnCount = rsmd.getColumnCount();
			System.out.println("结果集的共"+columnCount+"列信息");
			for(int i = 0; i < columnCount; i++ ){
				System.out.println("结果集的第"+(i+1)+"列信息");
//			通过结果集的元数据对象 获取列的名子
				String columnName = rsmd.getColumnName(i+1);
				System.out.println(columnName);
//			通过结果集的元数据对象 获取列的别名
				String columnLabel = rsmd.getColumnLabel(i+1);
				System.out.println(columnLabel);
//			通过结果集的元数据对象 获取列的数据类型
				String typeName = rsmd.getColumnTypeName(i+1);
				System.out.println(typeName);
			}
	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCTools.release(rs,ps,connection);
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * DatabaseMedaData描述数据库元数据对象， 可以得到该数据库的基本信息，
	 * 如：数据库版本号，用户名，返回表集合
	 * 
	 */
	@Test
	public void testDatabaseMedaData() {
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = JDBCTools.getConnection();
			DatabaseMetaData data = connection.getMetaData();
			// 得到数据库本身的一些基本信息，如版本号version:int
			int version = data.getDatabaseMajorVersion();
			System.out.println(version);
			// 得到数据库用户名
			String user = data.getUserName();
			System.out.println(user);
			// 得到有哪些数据库,返回一个结果集（是一个数据库表名的单列集合）
			//此处在oracle上未被实现，undo！！！！！
			rs = data.getCatalogs();
			while (rs.next()) {

				System.out.println(rs.getString(1));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTools.release(rs, null, connection);
		}

	}

}
