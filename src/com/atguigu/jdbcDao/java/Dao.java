package com.atguigu.jdbcDao.java;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
/**
 * 
 * �������ݵ�Dao�ӿ�
 * ���涨���˷������ݿ�ĸ��ַ���
 *�ýӿ��в�ͬ��ʵ��·������jdbcʵ��
 * @param <T> �����ʵ������
 */

public interface Dao<T> {
	/**
	 * ִ��update insert delete ��� ���������ݿ�
	 * @param connection ���ݿ�����
	 * @param sql Ҫִ�е�sql���
	 * @param args ���sql����е�ռλ��
	 */
	@Test
	public void update(Connection connection,String sql,Object...args);
	/**
	 * �õ�һ��T��ʵ��
	 * @param connection 
	 * @param sql 
	 * @param args 
	 * @return 
	 * @throws SQLException 
	 */
	@Test
	public  T get(Connection connection,String sql,Object...args) throws SQLException;
	/**
	 * �õ�ʵ������ļ���
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 */
	@Test
	public  List<T> getForList(Connection connection,String sql,Object...args);
	/**
	 * ����һ������ֵ�����ѯƽ�����ʣ�ĳԱ����id
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 */
	@Test
	public <E> E getForValue(Connection connection,String sql,Object...args);//ע��˴�Ϊ���ͷ�����Ҫ�Լ���������
	
	/**
	 * ����ִ��sql���
	 * @param connection
	 * @param sql
	 * @param args Ϊ���������һ���������һ��sql����ռλ��
	 */
	@Test
	public void batch(Connection connection,String sql,Object[]...args);

}
