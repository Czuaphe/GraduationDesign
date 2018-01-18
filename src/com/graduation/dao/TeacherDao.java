package com.graduation.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.graduation.db.DBUtils;
import com.graduation.entity.Teacher;

public class TeacherDao {
	
	private QueryRunner runner = DBUtils.getQueryRunner();
	/**
	 * 保存教师，传入的对象会得到TID，可以继续使用此对象
	 * @param teacher 要保存的教师
	 * @return boolean 返回保存结果
	 */
	public boolean saveTeacher(Teacher teacher) {
		String sql = "insert into t_teacher(username, password, realname, sex, mid, number, title, degree, qq, phone, email, remarks) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			int num = runner.update(sql
					,teacher.getUsername()
					,teacher.getPassword()
					,teacher.getRealname()
					,teacher.getSex()
					,teacher.getMid()
					,teacher.getNumber()
					,teacher.getTitle()
					,teacher.getDegree()
					,teacher.getQq()
					,teacher.getPhone()
					,teacher.getEmail()
					,teacher.getRemarks()
					);
			BigInteger id =  runner.query("SELECT LAST_INSERT_ID()", new ScalarHandler<BigInteger>());
			System.out.println(id.toString());
			teacher.setTea_id(Integer.parseInt(id.toString()));
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 根据教师的TID删除教师
	 * @param tid 教师的TID
	 * @return boolean 返回删除的结果
	 */
	public boolean remove(int tea_id) {
		
		try {
			String sql = "delete from t_teacher where tea_id = ?";
			return runner.update(sql, tea_id) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 根据教师的TID更新教师的所有字段
	 * @param teacher 要更新的教师对象
	 * @return boolean 返回更新的结果
	 */
	public boolean updateAll(Teacher teacher) {
		String sql = "update t_teacher set username = ?, realname = ?, sex = ?, mid = ?, number = ?, title = ?, degree = ?, qq= ?, phone = ?, email = ?, remarks = ? where tea_id = ?";
		try {
			return runner.update(sql
					,teacher.getUsername()
					,teacher.getRealname()
					,teacher.getSex()
					,teacher.getMid()
					,teacher.getNumber()
					,teacher.getTitle()
					,teacher.getDegree()
					,teacher.getQq()
					,teacher.getPhone()
					,teacher.getEmail()
					,teacher.getRemarks()
					,teacher.getTea_id()
					) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean resetPassword(int tea_id) {
		String sql = "update t_teacher set password = ? where tea_id = ?";
		try {
			return runner.update(sql, "123456", tea_id) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据教师的TID查询教师对象
	 * @param tid 教师的TID
	 * @return 返回查询到的教师对象，如果没有返回NULL
	 */
	public Teacher queryByTea_id(int tea_id) {
		
		try {
			String sql = "select * from t_teacher where tea_id = ?";
			return runner.query(sql, new BeanHandler<>(Teacher.class), tea_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Teacher> queryAll() {
		
		try {
			String sql = "select * from t_teacher";
			return runner.query(sql, new BeanListHandler<>(Teacher.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 查询教师总人数
	 * @return
	 */
	public Long queryCount() {
		String sql = "select count(1) from t_teacher";
		try {
			return runner.query(sql, new ScalarHandler<Long>());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public Long queryMajorCount(int mid) {
		String sql = "select count(1) from t_teacher where mid = ?";
		try {
			return runner.query(sql, new ScalarHandler<Long>(), mid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public List<Teacher> queryByPage(int page, int pageSize) {
		
		try {
			String sql = "select * from t_teacher limit ?, ?";
			return runner.query(sql, new BeanListHandler<>(Teacher.class), (page - 1) * pageSize, pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 通过用户名得到教师对象
	 * @param username
	 * @return
	 */
	public Teacher queryByUserName(String username) {
		String sql = "select * from t_teacher where username = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Teacher.class), username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 查询结果不唯一，不建议使用
	 * @param realname
	 * @return
	 */
	@Deprecated
	public Teacher queryByRealName(String realname) {
		String sql = "select * from t_teacher where realname = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Teacher.class), realname);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
