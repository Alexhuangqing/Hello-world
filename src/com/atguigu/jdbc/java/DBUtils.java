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
	 *  ScalarHandler: ���Է���ָ���е�һ��ֵ�򷵻�һ��ͳ�ƺ�����ֵ.
	 *  ������ض�����¼���򷵻ص��ǵ�һ����¼�õ�һ�е�ֵ 
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
	 * ����sql����ѯ��Ӧ�������Ȼ�󽫽����ת��Ϊmap��List
	 * ����sql����ѯ�����������Ǳ����� ֵ����һ����¼��Ӧ����ֵ
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
	 * ����sql����ѯ��Ӧ�ĵ�һ����¼����Ӧ��Map����
	 * ����sql����ѯ�����������Ǳ����� ֵ����һ����¼��Ӧ����ֵ
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
	 * �ѽ��תΪһ��List����list��Ϊnull����û��Ԫ�� ������Ϊ[],
	 * sql��һ����ѯ��¼������list�����У�һ����Ӧ��BeanListHandler�д����class��Ӧ�����ʵ��
	 * ע�⣺Employee��Ҫ�пղι�������handle����������ʵ���������˷��䴴���˸����ʵ����ͬʱ�еı���Ҫ����������Ӧ
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
	 * BeanHandler �ѽ�����ĵ�һ����¼��ת��Ϊ����BeanHandler����ʱ������Class������Ӧ�Ķ���
	 * ע�⣺Employee��Ҫ�пղι�������handle����������ʵ���������˷��䴴���˸����ʵ����ͬʱ�еı���Ҫ����������Ӧ
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
	 * ����QueryRunner���query()���� �˴���query��������ʵ��
	 * ���ǵ��õ�ResultSetHandler��handle��rs����������������rs ����query����������
	 * ����ֱֵ�Ӿ���handle���������ķ���ֵ�� result=rsh.handle��rs����
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
	 * ����QueryRunner���update���������� ����������sql��䣺update��delete��insert
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
