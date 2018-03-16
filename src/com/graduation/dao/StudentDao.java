package com.graduation.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.graduation.db.DBUtils;
import com.graduation.entity.Student;

public class StudentDao {

	private QueryRunner runner = DBUtils.getQueryRunner();
	
	public boolean save(Student student) {
		String sql = "insert into t_student(username, password, realname, sex, mid, qq, phone, email, remarks) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			int num = runner.update(sql
					,student.getUsername()
					,student.getPassword()
					,student.getRealname()
					,student.getSex()
					,student.getMid()
					,student.getQq()
					,student.getPhone()
					,student.getEmail()
					,student.getRemarks()
					);
			BigInteger id =  runner.query("SELECT LAST_INSERT_ID()", new ScalarHandler<BigInteger>());
			System.out.println("Save Student ID: " + id.toString());
			student.setStu_id(Integer.parseInt(id.toString()));
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean remove(int stu_id) {
		
		try {
			String sql = "delete from t_student where stu_id = ?";
			return runner.update(sql, stu_id) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateAll(Student student) {
		String sql = "update t_student set username = ?, realname = ?, sex = ?, mid = ?, qq= ?, phone = ?, email = ?, remarks = ? where stu_id = ?";
		try {
			return runner.update(sql
					,student.getUsername()
					,student.getRealname()
					,student.getSex()
					,student.getMid()
					,student.getQq()
					,student.getPhone()
					,student.getEmail()
					,student.getRemarks()
					,student.getStu_id()
					) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean resetPassword(int stu_id) {
		String sql = "update t_student set password = ? where stu_id = ?";
		try {
			return runner.update(sql, "123456", stu_id) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Student queryByStu_id(int stu_id) {
		
		try {
			String sql = "select * from t_student where stu_id = ?";
			return runner.query(sql, new BeanHandler<>(Student.class), stu_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Student queryByUsername(String username) {
		String sql = "select * from t_student where username = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Student.class), username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Student> queryAll() {
		
		try {
			String sql = "select * from t_student";
			return runner.query(sql, new BeanListHandler<>(Student.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Long queryCount() {
		String sql = "select count(1) from t_student";
		try {
			return runner.query(sql, new ScalarHandler<Long>());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public List<Student> queryByPage(int page, int pageSize) {
		
		try {
			String sql = "select * from t_student limit ?, ?";
			return runner.query(sql, new BeanListHandler<>(Student.class), (page - 1) * pageSize, pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Long queryMajorCount(int mid) {
		
		String sql = "select count(1) from t_student where mid = ?";
		try {
			return runner.query(sql, new ScalarHandler<Long>(), mid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public Connection getConnection() {
		
		Connection connection = null;
		
		try {
			connection = runner.getDataSource().getConnection();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return connection;
	}
	
}
