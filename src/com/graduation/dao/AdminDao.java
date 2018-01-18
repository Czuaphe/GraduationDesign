package com.graduation.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.graduation.db.DBUtils;
import com.graduation.entity.Admin;

public class AdminDao {

	private QueryRunner runner = DBUtils.getQueryRunner();
	
	public Admin queryByUsername(String username) {
		String sql = "select * from t_admin where username = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Admin.class), username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
