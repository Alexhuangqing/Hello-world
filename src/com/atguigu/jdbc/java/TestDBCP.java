package com.atguigu.jdbc.java;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestDBCP {
	
	@Test
	public void testC3p0Connection() throws SQLException{
		Connection connection = JDBCTools.getConnection();
		System.out.println(connection);
	}
	/**
	 * c3p0���ݿ����ӳ�,�����ļ� xml ���ο������ĵ��� Appendix B: Configuation Files ������
	 * @throws Exception 
	 */
	@Test
	public void testC3P0WithXML() throws Exception{
		DataSource dataSource = new ComboPooledDataSource("helloc3p0");
		Connection connection =  dataSource.getConnection();
		System.out.println(connection);
	}
	
	
	/**c3p0���ݿ����ӳ�
	 * ����c3p0-0.9.2.1.jar��ͬʱ����Ӧ�ð�mchange-commons-java-0.2.3.4.jar������ͬ��Ŀ¼��
	 * @throws Exception
	 */
	@Test
	public void testC3P0() throws Exception {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass("oracle.jdbc.driver.OracleDriver"); //loads the jdbc driver            
		cpds.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		cpds.setUser("scott");                                  
		cpds.setPassword("tiger");   
		System.out.println(cpds.getConnection());
	}

	/**
	 * BasicDataSourceFactory��һ�������� �����ڴ���ʵ�ָ�DataSource�ӿڵĶ���
	 * 
	 * @throws Exception
	 */

	@Test
	public void testDBCPWithDataSourceFactory() throws Exception {

		Properties properties = new Properties();
		InputStream is = TestDBCP.class.getClassLoader().getResourceAsStream("dbcp.properties");
		properties.load(is);

		DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);

		Connection connection = dataSource.getConnection();
		System.out.println(connection);

	}

	/**
	 * ʹ��DBCP���ݿ����ӳ� DataSource��һ���ӿڣ�BasicDataSource�Ǹýӿڵ�ʵ����
	 * DataSourceͨ����֮Ϊ����Դ�����������ӳ������ӳع����������֣� ϰ���ϰ�DataSource��֮Ϊ���ӳأ�
	 * 1.��������jar����dbcpjar�� ������commons-Pool 2.�������ݿ����ӳ� 3.Ϊ����Դʵ��ָ����������� 4.������Դ��
	 * ��ȡ���ݿ�����
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testDBCP() throws SQLException {
		// �������ӳ�
		BasicDataSource dataSource = new BasicDataSource();
		// Ϊ����Դָ�����������setter����
		dataSource.setUsername("scott");
		dataSource.setPassword("tiger");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		// Ϊ����Դָ��һЩ��ѡ����
		// 1). ָ�����ݿ����ӳ��г�ʼ���������ĸ���
		dataSource.setInitialSize(5);
		// 2). ָ������������: ͬһʱ�̿���ͬʱ�����ݿ������������
		dataSource.setMaxActive(5);
		// 3). ָ��С������: �����ݿ����ӳ��б�������ٵĿ������ӵ�����
		dataSource.setMinIdle(5);
		// 4).�ȴ����ݿ����ӳط������ӵ��ʱ��. ��λΪ����. ������ʱ�佫�׳��쳣.
		dataSource.setMaxWait(5000);
		// ������Դ�л�ȡ���ݿ�����
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
		connection = dataSource.getConnection();
		System.out.println(connection);
		connection = dataSource.getConnection();
		System.out.println(connection);
		connection = dataSource.getConnection();
		System.out.println(connection);

		Connection connection2 = dataSource.getConnection();
		System.out.println(">" + connection2);
		// ʵ�ֵķ�ʽ����һ�����߳�,���Ի�ȡһ������
		new Thread() {
			public void run() {
				Connection conn = null;
				try {
					conn = dataSource.getConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(conn);

			}
		}.start();
		try {
			Thread.sleep(4500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection2.close();

	}
}
