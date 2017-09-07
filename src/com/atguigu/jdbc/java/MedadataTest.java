package com.atguigu.jdbc.java;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

public class MedadataTest {
	/**ResultSetMetaData 
	 * �����������Ԫ���ݶ��󣬿��Եõ�������Ļ�����Ϣ
	 * 
	 * ������columnName,�еı���columnLabel,��������еĸ���columnCount����������е�����columnTypeName
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
//			���棺���ﲻ������ռλ����ռλ����ҪӦ����ɸѡ��������Ӧ���ݿ���value
//			ps.setString(1,"EMPLOYEE_ID  id");
//			ps.setString(2,"SALARY  sal");
//			ps.setString(3,"EMAIL em");
			
			 rs = ps.executeQuery();
//			���ﲻ��Ҫȡ��������е�ֵ������Ҫ����rs.next()
			
			ResultSetMetaData rsmd = rs.getMetaData();
//			ͨ���������Ԫ���ݶ��� ��ȡ�еĵĸ���
			int columnCount = rsmd.getColumnCount();
			System.out.println("������Ĺ�"+columnCount+"����Ϣ");
			for(int i = 0; i < columnCount; i++ ){
				System.out.println("������ĵ�"+(i+1)+"����Ϣ");
//			ͨ���������Ԫ���ݶ��� ��ȡ�е�����
				String columnName = rsmd.getColumnName(i+1);
				System.out.println(columnName);
//			ͨ���������Ԫ���ݶ��� ��ȡ�еı���
				String columnLabel = rsmd.getColumnLabel(i+1);
				System.out.println(columnLabel);
//			ͨ���������Ԫ���ݶ��� ��ȡ�е���������
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
	 * DatabaseMedaData�������ݿ�Ԫ���ݶ��� ���Եõ������ݿ�Ļ�����Ϣ��
	 * �磺���ݿ�汾�ţ��û��������ر���
	 * 
	 */
	@Test
	public void testDatabaseMedaData() {
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = JDBCTools.getConnection();
			DatabaseMetaData data = connection.getMetaData();
			// �õ����ݿⱾ���һЩ������Ϣ����汾��version:int
			int version = data.getDatabaseMajorVersion();
			System.out.println(version);
			// �õ����ݿ��û���
			String user = data.getUserName();
			System.out.println(user);
			// �õ�����Щ���ݿ�,����һ�����������һ�����ݿ�����ĵ��м��ϣ�
			//�˴���oracle��δ��ʵ�֣�undo����������
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
