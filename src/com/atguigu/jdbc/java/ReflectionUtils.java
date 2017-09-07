package com.atguigu.jdbc.java;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//反射工具包
public class ReflectionUtils {
	/**
	 * 
	 * @param obj 反射创建的实例对象
	 * @param fieldName  字段名 这里在Oracle（默认全大写）中 注意返回值得大小写
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
	 * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型
	 * 如: public EmployeeDao extends BaseDao<Employee, String>
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
	 * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型
	 * 如: public EmployeeDao extends BaseDao<Employee, String>
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> Class<T> getSuperGenericType(Class clazz){
		return getSuperClassGenricType(clazz, 0);
	}
	
	
}
