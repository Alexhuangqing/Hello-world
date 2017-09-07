package com.atguigu.jdbc.java;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import org.junit.Test;

public class AddStudentExcer {
	@Test
	public void testSelectStudent() throws Exception {
		// 1.得到查询学生信息的类型
		int type = getTypeFromConsole();
		// 2.根据类型查询到这个学生，返回这个学生
		Student student = searchStudent(type);
		// 3.打印这个学生信息
		printStudent(student);
	}

	/**
	 * 依次打印student对象的信息
	 * 
	 * @param student
	 */
	private void printStudent(Student student) {
		if (student != null) {

			System.out.println(student.toString());
		} else {
			System.out.println("查无此人");
		}

	}

	/**
	 * 具体查询学生信息，返回一个学生对象，若没有，返回null
	 * 
	 * @param type
	 *            1或2
	 * @return
	 * @throws Exception
	 */
	private Student searchStudent(int type) throws Exception {
		// 这里sql语句里 用""设置别名（String中使用转义字符\），因为列名在Oracle里面是全大写，
		// 大写返回不能与属性名对应，会报错NoSuchFieldException
		String sql = "select flowID  \"flowID\", type \"type\", idCard \"idCard\""
				+ ", examCard \"examCard\",studentName \"studentName\", location \"location\",grade \"grade\""
				+ " from examstudent2 where ";

		// 1 根据type信息，进行提示用户输入
		// 如果是type=1：从控制台获取examCard； 如果是2：用身份证查询idCard
		Scanner scanner = new Scanner(System.in);
		if (type == 1) {
			System.out.println("请输入您的准考证号");
			String examCard = scanner.next();
			sql = sql + "examCard='" + examCard + "'";
		} else {
			System.out.println("请输入您的身份证号");
			String idCard = scanner.next();
			sql = sql + "idCard='" + idCard + "'";

		}

		// 2.根据输入的信息确定sql语句

		// 3.执行查询
		Student stu = JDBCTools.getSelectObject(Student.class, sql);

		// 4，如存在查询结果，则把查询结果封装成一个student对象里
		return stu;

	}

	private Student getStudent(String sql) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Student student = null;
		try {
			connection = JDBCTools.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (rs.next()) {
				student = new Student();
				student.setFlowID(rs.getInt(1));
				student.setType(rs.getInt(2));
				student.setIdCard(rs.getString(3));
				student.setExamCard(rs.getString(4));
				student.setStudentName(rs.getString(5));
				student.setLocation(rs.getString(6));
				student.setGrade(rs.getInt(7));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTools.release(rs, statement, connection);
		}

		return student;
	}

	/**
	 * 从控制台返回1或2的整数。
	 * 
	 * @return 1:用学生证examCard查询，2:用身份证查询idCard;其他 报错输入有误
	 * @throws InterruptedException
	 */

	private int getTypeFromConsole() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("请选择查询方式：1或2？（1:用学生证examCard查询；2:用学生证查询idCard）");
		int type = scanner.nextInt();
		if (type != 1 && type != 2) {
			System.out.println("请重新输入！");
			throw new RuntimeException();
		}

		return type;
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void TestAddStudent() throws Exception {
		// (1)从控制台获得数据 创建是student对象
		Student stu = getNewStudent();
		// (2)执行addStudent（）方法
		addStudent(stu);

	}

	/**
	 * 实现增，删，改
	 * 
	 * @param student
	 * @throws Exception
	 */

	public void addStudent(Student student) throws Exception {
		// 1)sql语句
		String sql = "insert into examstudent values(?,?,?,?,?,?,?) ";
		// 2)updateData(sql)
		JDBCTools.upDateData(sql, student.getFlowID(), student.getType(), student.getIdCard(), student.getExamCard(),
				student.getStudentName(), student.getLocation(), student.getGrade());
	}

	/**
	 * 接收来自控制台的数据，得到要插入的student对象
	 * 
	 * @return Student
	 */

	private Student getNewStudent() {
		Student student = new Student();
		Scanner scanner = new Scanner(System.in);

		System.out.print("flowID:");
		student.setFlowID(scanner.nextInt());

		System.out.print("type:");
		student.setType(scanner.nextInt());

		System.out.print("idCard:");
		student.setIdCard(scanner.next());

		System.out.print("examCard:");
		student.setExamCard(scanner.next());

		System.out.print("studentName:");
		student.setStudentName(scanner.next());

		System.out.print("Location:");
		student.setLocation(scanner.next());

		System.out.print("Grade:");
		student.setGrade(scanner.nextInt());

		scanner.close();
		return student;
	}

}
