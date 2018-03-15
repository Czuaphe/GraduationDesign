package com.graduation.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.graduation.db.DBUtils;
import com.graduation.entity.Major;

public class MajorDao {

	private QueryRunner runner = DBUtils.getQueryRunner();
	
	/**
	 * 保存一个专业
	 * @param major 要保存的专业对象
	 * @return
	 */
	public boolean save(Major major) {
		boolean flag = false;
		String sql = "insert into t_major(major, tea_id) values(?, ?)";
		try {
			flag = runner.update(sql, major.getMajor(), major.getTea_id()) > 0;
			if (flag) {
				BigInteger id =  runner.query("SELECT LAST_INSERT_ID()", new ScalarHandler<BigInteger>());
				major.setMid(Integer.parseInt(id.toString()));
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 删除一个专业
	 * @param mid 专业ID
	 * @return
	 */
	public boolean remove(int mid) {
		String sql = "delete from t_major where mid = ?";
		try {
			return runner.update(sql, mid) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 更改一个专业的名称，专业负责人ID信息
	 * @param major 
	 * @return
	 */
	public boolean update(Major major) {
		String sql = "update t_major set major = ?, tea_id = ? where mid = ?";
		
		try {
			return runner.update(sql, major.getMajor(), major.getTea_id(), major.getMid()) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	/**
	 * 更新专业出题，审核，选题时间信息
	 * @param major
	 * @return
	 */
	public boolean updateTime(Major major) {
		
		String sql = "update t_major set problem_start = ?, problem_end = ?, verify_start = ?, verify_end = ?, select_start = ?, select_end = ? where mid = ?";
		try {
			return runner.update(sql
					, major.getProblem_start()
					, major.getProblem_end()
					, major.getVerify_start()
					, major.getVerify_end()
					, major.getSelect_start()
					, major.getSelect_end()
					, major.getMid()
					) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 得到所有的专业
	 * @return 返回一个专业列表
	 */
	public List<Major> getAllMajor() {
		String sql = "select * from t_major";
		try {
			return runner.query(sql, new BeanListHandler<>(Major.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过专业ID找到专业对象
	 * @param mid 专业ID
	 * @return 返回专业名称
	 */
	public Major queryByMID(int mid) {
		
		try {
			String sql = "select * from t_major where mid = ?";
			return runner.query(sql, new BeanHandler<>(Major.class), mid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过专业名称找到对象
	 * @param name 专业名称
	 * @return 
	 */
	public Major queryByMajorName(String name) {
		String sql = "select * from t_major where major = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Major.class), name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Major queryByTea_id(int tea_id) {
		String sql = "select * from t_major where tea_id = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Major.class), tea_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
