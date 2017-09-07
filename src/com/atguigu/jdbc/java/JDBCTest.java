package com.atguigu.jdbc.java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

public class JDBCTest {
	/**
	 * ��ȡ blob ����: 1. ʹ�� getBlob ������ȡ�� Blob���� 2.���� Blob �� getBinaryStream()
	 * �����õ�����������ʹ�� IO ��������.
	 */
	@Test
	public void testLoadBlob() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "select photo from emp1 where employee_id = 1004 ";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			// 1. ʹ�� getBlob ������ȡ�� Blob ����

			rs.next();
			Blob photo = rs.getBlob(1);

			// 2. ���� Blob �� getBinaryStream()

			is = photo.getBinaryStream();
			os = new FileOutputStream("2.jpg");

			byte[] b = new byte[1024];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				os.write(b, 0, len);
			}
			os.flush();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JDBCTools.release(rs, ps, connection);
		}

	}

	/**
	 * 
	 * ���� BLOB ���͵����ݱ���ʹ�� PreparedStatement����Ϊ BLOB ���� ������ʱ�޷�ʹ���ַ���ƴд�ġ�
	 * 
	 * ���� setBlob(int index, InputStream inputStream)
	 * 
	 */
	@SuppressWarnings("resource")
	@Test
	public void testAddBlob() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			// 1�������blob: insert into javatest(name,content)
			// values(?,empty_blob());

			connection = JDBCTools.getConnection();
			String sql1 = "insert into emp1 values(?,?,?,empty_blob())";
			ps = connection.prepareStatement(sql1);
			ps.setInt(1, 1004);
			ps.setInt(2, 12345);
			ps.setString(3, "@qq.com");
			ps.executeUpdate();

			// 2�����blob��cursor:select content from javatest where name= ? for
			// update;
			// ע��: ���for update���������У�ֱ�����б��޸���ϣ���֤������������ͻ��
			String sql2 = "select photo from emp1 where employee_id = 1004 for update";
			ps = connection.prepareStatement(sql2);
			rs = ps.executeQuery();
			rs.next();// ��ѯ�õ����صĽ������ָ���ȡ����
			Blob blob = rs.getBlob(1);// �õ���blob����

			// 3������ io���ͻ�ȡ����cursor�����ݿ�д������
			os = blob.setBinaryStream(0);
			is = new FileInputStream("1.JPG");

			int len = 0;
			while ((len = is.read()) != -1) {
				os.write(len);
			}
			os.flush();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JDBCTools.release(null, ps, connection);
		}

	}

}
