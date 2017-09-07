package com.atguigu.jdbcDao.java;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
/**
 * 
 * 访问数据的Dao接口
 * 里面定义了访问数据库的各种方法
 *该接口有不同的实现路径，如jdbc实现
 * @param <T> 处理的实体类型
 */

public interface Dao<T> {
	/**
	 * 执行update insert delete 语句 ，更新数据库
	 * @param connection 数据库链接
	 * @param sql 要执行的sql语句
	 * @param args 填充sql语句中的占位符
	 */
	@Test
	public void update(Connection connection,String sql,Object...args);
	/**
	 * 得到一个T的实例
	 * @param connection 
	 * @param sql 
	 * @param args 
	 * @return 
	 * @throws SQLException 
	 */
	@Test
	public  T get(Connection connection,String sql,Object...args) throws SQLException;
	/**
	 * 得到实例对象的集合
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 */
	@Test
	public  List<T> getForList(Connection connection,String sql,Object...args);
	/**
	 * 返回一个属性值，如查询平均工资，某员工的id
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 */
	@Test
	public <E> E getForValue(Connection connection,String sql,Object...args);//注意此处为泛型方法，要自己声明泛型
	
	/**
	 * 批量执行sql语句
	 * @param connection
	 * @param sql
	 * @param args 为数组参数，一个数组填充一条sql语句的占位符
	 */
	@Test
	public void batch(Connection connection,String sql,Object[]...args);

}
