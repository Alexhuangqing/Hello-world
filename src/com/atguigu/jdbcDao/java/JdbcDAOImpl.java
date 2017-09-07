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
 *            JdbcDAOImpl<T>子类传入的具体实现
 */
public class JdbcDAOImpl<T> implements Dao<T> {
	QueryRunner queryRunner = null;
	// 泛型是通过一个标识来表示声明类中某个属性的类型
	Class<T> type;// 该实现类的泛型是从父类的泛型传下来的，所以这样给初始化

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
