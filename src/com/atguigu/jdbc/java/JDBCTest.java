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
	 * 读取 blob 数据: 1. 使用 getBlob 方法读取到 Blob对象 2.调用 Blob 的 getBinaryStream()
	 * 方法得到输入流。再使用 IO 操作即可.
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
			// 1. 使用 getBlob 方法读取到 Blob 对象

			rs.next();
			Blob photo = rs.getBlob(1);

			// 2. 调用 Blob 的 getBinaryStream()

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
	 * 插入 BLOB 类型的数据必须使用 PreparedStatement：因为 BLOB 类型 的数据时无法使用字符串拼写的。
	 * 
	 * 调用 setBlob(int index, InputStream inputStream)
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
			// 1、插入空blob: insert into javatest(name,content)
			// values(?,empty_blob());

			connection = JDBCTools.getConnection();
			String sql1 = "insert into emp1 values(?,?,?,empty_blob())";
			ps = connection.prepareStatement(sql1);
			ps.setInt(1, 1004);
			ps.setInt(2, 12345);
			ps.setString(3, "@qq.com");
			ps.executeUpdate();

			// 2、获得blob的cursor:select content from javatest where name= ? for
			// update;
			// 注意: 须加for update，锁定该行，直至该行被修改完毕，保证不产生并发冲突。
			String sql2 = "select photo from emp1 where employee_id = 1004 for update";
			ps = connection.prepareStatement(sql2);
			rs = ps.executeQuery();
			rs.next();// 查询得到返回的结果集，指针读取数据
			Blob blob = rs.getBlob(1);// 得到空blob对象

			// 3、利用 io，和获取到的cursor往数据库写数据流
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
