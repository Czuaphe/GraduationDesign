package com.graduation.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.graduation.db.DBUtils;

public class ProfessionDao {
	
	private QueryRunner runner = DBUtils.getQueryRunner();
	/**
	 * 通过专业负责人ID得到教师ID
	 * @param mid 专业ID
	 * @return
	 */
	public int getTeaIDByPID(int pid) {
		String sql = "select tea_id from t_profession where pid = ?";
		try {
			return runner.query(sql, new ScalarHandler<Integer>(), pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int getPIDByTeaID(int tea_id) {
		String sql = "select pid from t_profession where tea_id = ?";
		try {
			return runner.query(sql, new ScalarHandler<Integer>(), tea_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int getPIDByMID(int mid) {
		String sql = "select pid from t_profession where mid = ?";
		try {
			return runner.query(sql, new ScalarHandler<Integer>(), mid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
