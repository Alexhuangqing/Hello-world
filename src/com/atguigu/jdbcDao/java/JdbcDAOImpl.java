package com.atguigu.jdbcDao.java;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanMapHandler;

/**
 * 
 * 
 *
 * @param <T>
 *            JdbcDAOImpl<T>���ഫ��ľ���ʵ��
 */
public class JdbcDAOImpl<T> implements Dao<T> {
	QueryRunner queryRunner = null;
	// ������ͨ��һ����ʶ����ʾ��������ĳ�����Ե�����
	Class<T> type;// ��ʵ����ķ����ǴӸ���ķ��ʹ������ģ�������������ʼ��

	public JdbcDAOImpl() {
		queryRunner = new QueryRunner(true);
		type = ReflectionUtils.getSuperGenericType(getClass());
	}

	@Override
	public void update(Connection connection, String sql, Object... args) {
		// TODO Auto-generated method stub

	}

	@Override
	public T get(Connection connection, String sql, Object... args) throws SQLException {

		return (T) queryRunner.query(connection, sql, new BeanHandler<Object>(type), args);
	}

	@Override
	public List<T> getForList(Connection connection, String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> E getForValue(Connection connection, String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void batch(Connection connection, String sql, Object[]... args) {
		// TODO Auto-generated method stub

	}

}
