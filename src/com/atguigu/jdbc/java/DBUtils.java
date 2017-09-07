package com.atguigu.jdbc.java;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;



public class DBUtils {
	
	/**
	 *  ScalarHandler: 可以返回指定列的一个值或返回一个统计函数的值.
	 *  如果返回多条记录，则返回的是第一条记录得第一列的值 
	 */
	@Test
	public void testScalarHandler(){
		QueryRunner queryRunner = new QueryRunner(true);
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "select EMPLOYEE_ID \"id\" , SALARY \"sal\" , EMAIL \"email\"  from  emp1 ";
			Object query =  queryRunner.query(connection, sql, new ScalarHandler());
			System.out.println(query);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection);
		}
		
	}
	/**
	 * 返回sql语句查询对应结果集，然后将结果集转化为map的List
	 * 键：sql语句查询得列名（不是别名） 值：第一条记录对应的列值
	 */
	@Test
	public void testMapListHandler(){
		QueryRunner queryRunner = new QueryRunner(true);
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "select EMPLOYEE_ID \"id\" , SALARY \"sal\" , EMAIL \"email\"  from  emp1 ";
			List<Map<String ,Object>> query =  queryRunner.query(connection, sql, new MapListHandler());
			System.out.println(query);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection);
		}
		
	}
	
	/**
	 * 返回sql语句查询对应的第一条记录，对应的Map对象
	 * 键：sql语句查询得列名（不是别名） 值：第一条记录对应的列值
	 */
	@Test
	public void testMapHandler(){
		QueryRunner queryRunner = new QueryRunner(true);
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "select EMPLOYEE_ID \"id\" , SALARY \"sal\" , EMAIL \"email\"  from  emp1 ";
			java.util.Map<String ,Object> query =  queryRunner.query(connection, sql, new MapHandler());
			System.out.println(query);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection);
		}
		
	}
	
	/**BeanListHandler
	 * 把结果转为一个List，该list不为null，如没有元素 ，返回为[],
	 * sql中一条查询记录，就是list集合中，一条对应的BeanListHandler中传入的class对应对象的实例
	 * 注意：Employee类要有空参构造器，handle（）方法的实现中利用了反射创建了该类的实例，同时列的别名要与属性名对应
	 */
	@Test
	public void testBeanListHandler() {
		QueryRunner queryRunner = new QueryRunner(true);
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "select EMPLOYEE_ID \"id\" , SALARY \"sal\" , EMAIL \"email\"  from  emp1 where 1= 2";
			List<Employee> query = (List<Employee>) queryRunner.query(connection, sql, new BeanListHandler(Employee.class));
			System.out.println(query);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection);
		}
		
	}
	
	/**
	 * BeanHandler 把结果集的第一条记录，转化为创建BeanHandler对象时，传入Class参数对应的对象；
	 * 注意：Employee类要有空参构造器，handle（）方法的实现中利用了反射创建了该类的实例，同时列的别名要与属性名对应
	 */
	@Test
	public void testBeanHandler() {
		QueryRunner queryRunner = new QueryRunner(true);
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "select EMPLOYEE_ID \"id\" , SALARY \"sal\" , EMAIL \"email\"  from  emp1";
			Object query = queryRunner.query(connection, sql, new BeanHandler(Employee.class));
			System.out.println(query);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection);
		}

	}

	/**
	 * 测试QueryRunner类的query()方法 此处的query（）方法实际
	 * 就是调用的ResultSetHandler中handle（rs）方法来处理结果集rs 并且query（）方法的
	 * 返回值直接就是handle（）方法的返回值： result=rsh.handle（rs）；
	 */
	@Test
	public void testQueryRunnerQuery() {
		QueryRunner queryRunner = new QueryRunner(true);
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "select EMPLOYEE_ID , SALARY , EMAIL  from emp1";

			ResultSetHandler myResultSetHandler = new ResultSetHandler() {

				@Override
				public Object handle(ResultSet rs) throws SQLException {
					List<Employee> list = new ArrayList<>();

					while (rs.next()) {
						int id = rs.getInt(1);
						double sal = rs.getDouble(2);
						String email = rs.getString(3);
						list.add(new Employee(id, sal, email));
					}

					return list;
				}

			};

			Object query = queryRunner.query(connection, sql, myResultSetHandler);
			System.out.println(query);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection);
		}

	}

	/**
	 * 测试QueryRunner类的update（）方法； 适用于如下sql语句：update，delete，insert
	 */
	@Test
	public void testQueryRunnerUpdate() {
		QueryRunner queryRunner = new QueryRunner(true);
		Connection connection = null;

		try {
			connection = JDBCTools.getConnection();
			String sql = "insert into emp1 values(1005,12323,'@guigu',empty_blob())";

			queryRunner.update(connection, sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection);
		}

	}

}
