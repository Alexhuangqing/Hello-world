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
	 * c3p0数据库连接池,配置文件 xml 见参考帮助文档中 Appendix B: Configuation Files 的内容
	 * @throws Exception 
	 */
	@Test
	public void testC3P0WithXML() throws Exception{
		DataSource dataSource = new ComboPooledDataSource("helloc3p0");
		Connection connection =  dataSource.getConnection();
		System.out.println(connection);
	}
	
	
	/**c3p0数据库连接池
	 * 导入c3p0-0.9.2.1.jar的同时，还应该把mchange-commons-java-0.2.3.4.jar放在相同的目录下
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
	 * BasicDataSourceFactory是一个工厂类 ，用于创建实现该DataSource接口的对象
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
	 * 使用DBCP数据库链接池 DataSource是一个接口，BasicDataSource是该接口的实现类
	 * DataSource通常称之为数据源，它包含连接池与连接池管理两个部分， 习惯上把DataSource称之为连接池：
	 * 1.加入两个jar包，dbcpjar包 依赖于commons-Pool 2.创建数据库连接池 3.为数据源实例指定必须的属性 4.从数据源中
	 * 获取数据库链接
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testDBCP() throws SQLException {
		// 创建连接池
		BasicDataSource dataSource = new BasicDataSource();
		// 为数据源指定必须的属性setter方法
		dataSource.setUsername("scott");
		dataSource.setPassword("tiger");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		// 为数据源指定一些可选属性
		// 1). 指定数据库连接池中初始化连接数的个数
		dataSource.setInitialSize(5);
		// 2). 指定最大的连接数: 同一时刻可以同时向数据库申请的连接数
		dataSource.setMaxActive(5);
		// 3). 指定小连接数: 在数据库连接池中保存的最少的空闲连接的数量
		dataSource.setMinIdle(5);
		// 4).等待数据库连接池分配连接的最长时间. 单位为毫秒. 超出该时间将抛出异常.
		dataSource.setMaxWait(5000);
		// 从数据源中获取数据库链接
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
		// 实现的方式创建一个多线程,尝试获取一个链接
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
