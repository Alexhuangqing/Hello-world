package com.atguigu.jdbc.java;
//Beanutils��ͨ��getter��setter����ȡ��ȡ����������ֵ
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class TestBeanutils {
	public static void main(String[] args) {
		Object obj = new Student();
		System.out.println(obj);
		try {
			BeanUtils.setProperty(obj, "idCard", "211211211");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(obj);
		
	}
	
}
