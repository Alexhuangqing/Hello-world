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
//	1.通过Connection对象的prepareCall（）方法创建一个CallableStatement对象的实例
//	使用prepareCall（）方法时，需要传入String类型的字符串，该字符串用于指明如何调用存储过程
//	2通过CallableStatement对象的reisterOutParameter（）方法，注册out参数
			connection = JDBCTools.getConnection();
			String sql ="{?= call sum_emp1 (?)}";
			callableStatement = connection.prepareCall(sql);
			callableStatement.registerOutParameter(1, Types.NUMERIC);//返回值
			callableStatement.registerOutParameter(2, Types.NUMERIC);//返回参数
//	3通过CallableStatement对象的setXxx（）方法，设定IN或IN OUT参数
//	若想将参数默认值设为null，可用setNull（）方法,(指定形参)
			
//	4通过CallableStatement对象的execute（）方法执行存储过程
			callableStatement.execute();
			
//	5.如果所调用的是带有返回参数的存储过程，，还需要通过CallableStatement对象的
//	getXxx方法 获取其返回值
			
			double result = callableStatement.getDouble(1);//返回两个参数的结果集
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
