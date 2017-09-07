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
	 * insert update delete �Ȳ�����ķ���������ڴ˷�����
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

	// select��ѯ��һ����¼ ����һ�����Ӧ�Ķ���
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
			rs = ps.executeQuery();//��ѯ���Զ����ؽ����

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
	 * select��ѯ�ö�����¼ ����һ�����Ӧ�Ķ��󼯺�
	 * 
	 * @param clazz
	 *            ���������
	 * @param sql
	 *            ���У��򲻴��У�ռλ������sql���
	 * @param args
	 *            �β��б� �����ռλ��
	 * @return ���󼯺�
	 */
	public static <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
		// 1.��ѯ֮ǰ������Դ,��ԴҪ��try-catch����ʹ�ã�����Ҫ������
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// ���Ҫ����������ϣ����Բ��ܷ���try-catch����
		List<T> list = null;
		try {
			// 2.�������ӣ�ͨ�������β�ȷ�������sql��䣬�õ������
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();

			// 3.�ӽ������һ���б���
			// ��һ�м�¼�����ɸ���ֵ�Է�װ��һ��Map<String,Object>��
			// �����м�¼��װ��Ϊһ��List<Map>
			List<Map<String, Object>> values = handleResultSetToMapList(rs);
			// 4.���List<Map>��Ϊ�� ����List<Map>�����õ���ÿһ��Mapֵӳ�䵽һ��������

			list = transferMapListToBeanList(clazz, list, values);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// 6.�ر���Դ
			JDBCTools.release(rs, ps, connection);

		}

		return list;
	}

	private static <T> List<T> transferMapListToBeanList(Class<T> clazz, List<T> list, List<Map<String, Object>> values)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		if (values.size() > 0) {
			list = new ArrayList<T>();
			T entity = null;// ��������������
			for (Map<String, Object> value : values) {
				entity = clazz.newInstance();
				for (Entry<String, Object> entry : value.entrySet()) {
					// ReflectionUtils.setFieldValue(entity, entry.getKey(),
					// entry.getValue());
					BeanUtils.setProperty(entity, entry.getKey(), entry.getValue());
				}
				// 5.��ÿ��������4�����м���List<student>
				list.add(entity);

			}

		}
		return list;
	}

	/**
	 * �������� ��һ�м�¼�����ɸ���ֵ�Է�װ��һ��Map<String,Object>�� �����м�¼��װ��Ϊһ��List<Map>
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
	 * select��ѯ��һ����¼��һ������ֵ
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getForValue(String sql, Object... args) {
		// 1.������Դ
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 2.����sql��䣬����������ռλ��
			connection = JDBCTools.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// 3.���ؽ����
			rs = ps.executeQuery();
			// 4.��������
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
