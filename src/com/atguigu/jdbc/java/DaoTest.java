package com.atguigu.jdbc.java;

import java.util.List;

import org.junit.Test;

public class DaoTest {

	@Test
	public void testUpdate() {
		String sql = "insert into  values(?,?,?,?,?,?,?)";
		Dao.update(sql,1002,6,"123456","123456","jame","¹ã¶«",100);
	}
	@Test
	public void testGet() {
		String sql = "select flowID  \"flowID\", type \"type\", idCard \"idCard\""
				+ ", examCard \"examCard\",studentName \"studentName\", location \"location\",grade \"grade\""
				+ " from examstudent2 where examCard=?";

		Student stu = Dao.get( Student.class ,sql,"1");
		System.out.println(stu);
	}
	@Test
	public void testGetForList() {
		String sql = "select flowID  \"flowID\", type \"type\", idCard \"idCard\""
				+ ", examCard \"examCard\",studentName \"studentName\", location \"location\",grade \"grade\""
				+ " from examstudent2 ";
		
		List<Student> list = Dao.getForList( Student.class ,sql);
		System.out.println(list);
	}
	@Test
	public void testGetForValue() {
		String sql = "select flowID  \"flowID\" FROM examstudent2 where examCard=?";
		Object obj = Dao.getForValue(sql,"1");
		System.out.println(obj);
	}

}
