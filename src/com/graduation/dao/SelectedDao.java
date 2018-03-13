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
import com.graduation.entity.Selected;

public class SelectedDao {
	
	private QueryRunner runner = DBUtils.getQueryRunner();
	
	public boolean save(Selected selected) {
		String sql = "insert into p_selected(problem_id, stu_id, time) values(?, ?, ?)";
		try {
			int num = runner.update(sql
					, selected.getProblem_id()
					, selected.getStu_id()
					, selected.getTime()
					);
			BigInteger id =  runner.query("SELECT LAST_INSERT_ID()", new ScalarHandler<BigInteger>());
			System.out.println("Save Selected ID: " + id.toString());
			selected.setSelected_id(Integer.parseInt(id.toString()));
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean save(Connection connection, Selected selected) {
		String sql = "insert into p_selected(problem_id, stu_id, time) values(?, ?, ?)";
		try {
			int num = runner.update(connection, sql
					, selected.getProblem_id()
					, selected.getStu_id()
					, selected.getTime()
					);
			BigInteger id =  runner.query("SELECT LAST_INSERT_ID()", new ScalarHandler<BigInteger>());
			System.out.println("Save Selected ID: " + id.toString());
			selected.setSelected_id(Integer.parseInt(id.toString()));
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean remove(int selected_id) {
		String sql = "delete from p_selected where selected_id = ?";
		try {
			return runner.update(sql, selected_id) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateAll(Selected selected) {
		
		String sql = "update p_selected set problem_id = ?, stu_id = ?, time = ? where selected_id = ?";
		
		try {
			return runner.update(sql
					, selected.getProblem_id()
					, selected.getStu_id()
					, selected.getTime()
					, selected.getSelected_id()
					) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public List<Selected> queryAll() {
		
		String sql = "select * from p_selected";
		try {
			return runner.query(sql, new BeanListHandler<>(Selected.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Selected queryBySelected_id(int selected_id) {
		
		String sql = "select * from p_selected where selected_id = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Selected.class), selected_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Selected queryByProblem_id(int problem_id) {
	
		String sql = "select * from p_selected where problem_id = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Selected.class), problem_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Selected queryByProblem_id(Connection connection, int problem_id) {
		String sql = "select * from p_selected where problem_id = ?";
		try {
			return runner.query(connection, sql, new BeanHandler<>(Selected.class), problem_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Selected queryByStu_id(int stu_id) {
		
		String sql = "select * from p_selected where stu_id = ?";
		try {
			return runner.query(sql, new BeanHandler<>(Selected.class), stu_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
