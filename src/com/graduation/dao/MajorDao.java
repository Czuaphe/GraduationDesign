package com.graduation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.graduation.db.DBUtils;

public class MajorDao {

	private QueryRunner runner = DBUtils.getQueryRunner();
	/**
	 * 得到所有专业封装成一个Map，Key是专业ID，Value为专业的名称
	 * @return 返回一个Map
	 */
	public Map<Integer, String> getAllMajor() {
		String sql = "select mid, major from t_major";
		try {
			return runner.query(sql, new ResultSetHandler<Map<Integer, String>>() {

				@Override
				public Map<Integer, String> handle(ResultSet rs)
						throws SQLException {
					Map<Integer, String> map = new HashMap<>();
					while (rs.next()) {
						map.put(rs.getInt("mid"), rs.getString("major"));
					}
					return map;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过专业ID找到专业名称
	 * @param mid 专业ID
	 * @return 返回专业名称
	 */
	public String getMajorNameByMID(int mid) {
		
		try {
			String sql = "select major from t_major where mid = ?";
			return runner.query(sql, new ScalarHandler<String>(), mid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过专业名称找到专业ID
	 * @param name 专业名称
	 * @return 返回专业ID
	 */
	public int getMIDByMajorName(String name) {
		String sql = "select mid from t_major where major = ?";
		try {
			return runner.query(sql, new ScalarHandler<Integer>(), name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}
