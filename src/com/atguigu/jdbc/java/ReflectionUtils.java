package com.atguigu.jdbc.java;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//���乤�߰�
public class ReflectionUtils {
	/**
	 * 
	 * @param obj ���䴴����ʵ������
	 * @param fieldName  �ֶ��� ������Oracle��Ĭ��ȫ��д���� ע�ⷵ��ֵ�ô�Сд
	 * @param fieldValue  
	 * 						
	 * @throws Exception
	 */

	public static void setFieldValue(Object obj,String fieldName,Object fieldValue ) throws Exception {
		Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj,fieldValue);

	}

	/**
	 * ͨ������, ��ö��� Class ʱ�����ĸ���ķ��Ͳ���������
	 * ��: public EmployeeDao extends BaseDao<Employee, String>
	 * @param clazz
	 * @param index
	 * @return
	 */
	
	public static Class getSuperClassGenricType(Class clazz, int index){
		Type genType = clazz.getGenericSuperclass();
		
		if(!(genType instanceof ParameterizedType)){
			return Object.class;
		}
		
		Type [] params = ((ParameterizedType)genType).getActualTypeArguments();
		
		if(index >= params.length || index < 0){
			return Object.class;
		}
		
		if(!(params[index] instanceof Class)){
			return Object.class;
		}
		
		return (Class) params[index];
	}
	
	/**
	 * ͨ������, ��� Class �����������ĸ���ķ��Ͳ�������
	 * ��: public EmployeeDao extends BaseDao<Employee, String>
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> Class<T> getSuperGenericType(Class clazz){
		return getSuperClassGenricType(clazz, 0);
	}
	
	
}
