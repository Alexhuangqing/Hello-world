package com.atguigu.jdbc.java;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import org.junit.Test;

public class AddStudentExcer {
	@Test
	public void testSelectStudent() throws Exception {
		// 1.�õ���ѯѧ����Ϣ������
		int type = getTypeFromConsole();
		// 2.�������Ͳ�ѯ�����ѧ�����������ѧ��
		Student student = searchStudent(type);
		// 3.��ӡ���ѧ����Ϣ
		printStudent(student);
	}

	/**
	 * ���δ�ӡstudent�������Ϣ
	 * 
	 * @param student
	 */
	private void printStudent(Student student) {
		if (student != null) {

			System.out.println(student.toString());
		} else {
			System.out.println("���޴���");
		}

	}

	/**
	 * �����ѯѧ����Ϣ������һ��ѧ��������û�У�����null
	 * 
	 * @param type
	 *            1��2
	 * @return
	 * @throws Exception
	 */
	private Student searchStudent(int type) throws Exception {
		// ����sql����� ��""���ñ�����String��ʹ��ת���ַ�\������Ϊ������Oracle������ȫ��д��
		// ��д���ز�������������Ӧ���ᱨ��NoSuchFieldException
		String sql = "select flowID  \"flowID\", type \"type\", idCard \"idCard\""
				+ ", examCard \"examCard\",studentName \"studentName\", location \"location\",grade \"grade\""
				+ " from examstudent2 where ";

		// 1 ����type��Ϣ��������ʾ�û�����
		// �����type=1���ӿ���̨��ȡexamCard�� �����2�������֤��ѯidCard
		Scanner scanner = new Scanner(System.in);
		if (type == 1) {
			System.out.println("����������׼��֤��");
			String examCard = scanner.next();
			sql = sql + "examCard='" + examCard + "'";
		} else {
			System.out.println("�������������֤��");
			String idCard = scanner.next();
			sql = sql + "idCard='" + idCard + "'";

		}

		// 2.�����������Ϣȷ��sql���

		// 3.ִ�в�ѯ
		Student stu = JDBCTools.getSelectObject(Student.class, sql);

		// 4������ڲ�ѯ�������Ѳ�ѯ�����װ��һ��student������
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
	 * �ӿ���̨����1��2��������
	 * 
	 * @return 1:��ѧ��֤examCard��ѯ��2:�����֤��ѯidCard;���� ������������
	 * @throws InterruptedException
	 */

	private int getTypeFromConsole() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("��ѡ���ѯ��ʽ��1��2����1:��ѧ��֤examCard��ѯ��2:��ѧ��֤��ѯidCard��");
		int type = scanner.nextInt();
		if (type != 1 && type != 2) {
			System.out.println("���������룡");
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
		// (1)�ӿ���̨������� ������student����
		Student stu = getNewStudent();
		// (2)ִ��addStudent��������
		addStudent(stu);

	}

	/**
	 * ʵ������ɾ����
	 * 
	 * @param student
	 * @throws Exception
	 */

	public void addStudent(Student student) throws Exception {
		// 1)sql���
		String sql = "insert into examstudent values(?,?,?,?,?,?,?) ";
		// 2)updateData(sql)
		JDBCTools.upDateData(sql, student.getFlowID(), student.getType(), student.getIdCard(), student.getExamCard(),
				student.getStudentName(), student.getLocation(), student.getGrade());
	}

	/**
	 * �������Կ���̨�����ݣ��õ�Ҫ�����student����
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
