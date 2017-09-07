package com.atguigu.jdbc.java;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.junit.Test;

public class CallableStatementTest {
	
	@Test
	public void testCallableStatement(){
		
		Connection connection = null;
		CallableStatement callableStatement = null;
		try {
//	1.ͨ��Connection�����prepareCall������������һ��CallableStatement�����ʵ��
//	ʹ��prepareCall��������ʱ����Ҫ����String���͵��ַ��������ַ�������ָ����ε��ô洢����
//	2ͨ��CallableStatement�����reisterOutParameter����������ע��out����
			connection = JDBCTools.getConnection();
			String sql ="{?= call sum_emp1 (?)}";
			callableStatement = connection.prepareCall(sql);
			callableStatement.registerOutParameter(1, Types.NUMERIC);//����ֵ
			callableStatement.registerOutParameter(2, Types.NUMERIC);//���ز���
//	3ͨ��CallableStatement�����setXxx�����������趨IN��IN OUT����
//	���뽫����Ĭ��ֵ��Ϊnull������setNull��������,(ָ���β�)
			
//	4ͨ��CallableStatement�����execute��������ִ�д洢����
			callableStatement.execute();
			
//	5.��������õ��Ǵ��з��ز����Ĵ洢���̣�������Ҫͨ��CallableStatement�����
//	getXxx���� ��ȡ�䷵��ֵ
			
			double result = callableStatement.getDouble(1);//�������������Ľ����
			double out = callableStatement.getDouble(2);
			System.out.println(result);
			System.out.println(out);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCTools.release(callableStatement,connection);
		}
		
		
		
	}

}
