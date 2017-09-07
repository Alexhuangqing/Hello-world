package com.atguigu.jdbc.java;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class Dao {

	/**
	 * insert update delete 等操作表的方法会包含在此方法中
	 * 
	 * @param sql
	 * @param args
	 */
	public static void update(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			ps.executeQuery();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, ps, connection);
		}

	}

	// select查询得一条记录 返回一个表对应的对象
	public static <T> T get(Class<T> clazz, String sql, Object... args) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		T entity = null;

		try {
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();//查询会自动返回结果集

			Map<String, Object> values = new HashMap<>();
			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String label = rsmd.getColumnLabel(i + 1);
					Object obj = rs.getObject(i + 1);
					if (obj instanceof BigDecimal) {
						obj = ((BigDecimal) obj).intValue();
					}
					values.put(label, obj);

				}
				entity = clazz.newInstance();
				for (Entry<String, Object> entry : values.entrySet()) {
					// ReflectionUtils.setFieldValue(entity, entry.getKey(),
					// entry.getValue());
					BeanUtils.setProperty(entity, entry.getKey(), entry.getValue());
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTools.release(rs, ps, connection);
		}

		return entity;
	}

	/**
	 * select查询得多条记录 返回一个表对应的对象集合
	 * 
	 * @param clazz
	 *            反射类对象
	 * @param sql
	 *            带有（或不带有）占位符？的sql语句
	 * @param args
	 *            形参列表 用来填补占位符
	 * @return 对象集合
	 */
	public static <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
		// 1.查询之前开启资源,资源要在try-catch外面使用，所以要先声明
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// 最后要返回这个集合，所以不能放在try-catch里面
		List<T> list = null;
		try {
			// 2.建立链接，通过传入形参确定编译后sql语句，得到结果集
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();

			// 3.从结果集中一行行遍历
			// 将一行记录的若干个键值对封装成一个Map<String,Object>，
			// 将多行记录封装成为一个List<Map>
			List<Map<String, Object>> values = handleResultSetToMapList(rs);
			// 4.如果List<Map>不为空 遍历List<Map>，将得到的每一个Map值映射到一个对象中

			list = transferMapListToBeanList(clazz, list, values);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// 6.关闭资源
			JDBCTools.release(rs, ps, connection);

		}

		return list;
	}

	private static <T> List<T> transferMapListToBeanList(Class<T> clazz, List<T> list, List<Map<String, Object>> values)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		if (values.size() > 0) {
			list = new ArrayList<T>();
			T entity = null;// 将声明放在外面
			for (Map<String, Object> value : values) {
				entity = clazz.newInstance();
				for (Entry<String, Object> entry : value.entrySet()) {
					// ReflectionUtils.setFieldValue(entity, entry.getKey(),
					// entry.getValue());
					BeanUtils.setProperty(entity, entry.getKey(), entry.getValue());
				}
				// 5.将每个对象在4操作中加入List<student>
				list.add(entity);

			}

		}
		return list;
	}

	/**
	 * 处理结果集 将一行记录的若干个键值对封装成一个Map<String,Object>， 将多行记录封装成为一个List<Map>
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static List<Map<String, Object>> handleResultSetToMapList(ResultSet rs) throws SQLException {
		List<Map<String, Object>> values = new ArrayList<>();
		List<String> labels = getColumnLabels(rs);
		Map<String, Object> map = null;
		while (rs.next()) {
			map = new HashMap<>();

			for (String columnLabel : labels) {

				Object obj = rs.getObject(columnLabel);
				if (obj instanceof BigDecimal) {
					obj = ((BigDecimal) obj).intValue();
				}
				map.put(columnLabel, obj);

			}
			values.add(map);
		}
		return values;
	}

	private static List<String> getColumnLabels(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			list.add(rsmd.getColumnLabel(i + 1));
		}

		return list;
	}

	/**
	 * select查询得一条记录得一个属性值
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getForValue(String sql, Object... args) {
		// 1.开启资源
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 2.编译sql语句，传入参数填充占位符
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// 3.返回结果集
			rs = ps.executeQuery();
			// 4.返回属性
			if (rs.next()) {
				return (E) rs.getObject(1);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTools.release(rs, ps, connection);
		}

		return null;
	}

}
