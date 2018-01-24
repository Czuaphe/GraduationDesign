package com.graduation.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.graduation.db.DBUtils;
import com.graduation.entity.Problem;

public class ProblemDao {
	
	private QueryRunner runner = DBUtils.getQueryRunner();
	
	public boolean save(Problem problem) {
		
		String sql = "insert into t_problem(name, mid, is_new, type, source, research_name, nature, way, introduction, requirement, tea_id, problem_time, status, audit_time, audit_opinion, is_outer) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			int num = runner.update(sql
					, problem.getName()
					, problem.getMid()
					, problem.getIs_new()
					, problem.getType()
					, problem.getSource()
					, problem.getResearch_name()
					, problem.getNature()
					, problem.getWay()
					, problem.getIntroduction()
					, problem.getRequirement()
					, problem.getTea_id()
					, problem.getProblem_time()
					, problem.getStatus()
					, problem.getAudit_time()
					, problem.getAudit_opinion()
					, problem.getIs_outer()
					);
			BigInteger id =  runner.query("SELECT LAST_INSERT_ID()", new ScalarHandler<BigInteger>());
			System.out.println("新插入的课题对象的ID为：" + id.toString());
			problem.setProblem_id(Integer.parseInt(id.toString()));
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean remove(int problem_id) {
		String sql = "delete from t_problem where problem_id = ?";
		try {
			int num = runner.update(sql, problem_id);
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	/**
	 * 更新课题的部分信息
	 * @param problem
	 * @return
	 */
	public boolean updateProblem(Problem problem) {
		
		String sql = "update t_problem set name = ?, mid = ?, is_new = ?, type = ?, source = ?, research_name = ?, nature = ?, way = ?, introduction = ?, requirement = ? where problem_id = ?";
		
		try {
			int num = runner.update(sql
					, problem.getName()
					, problem.getMid()
					, problem.getIs_new()
					, problem.getType()
					, problem.getSource()
					, problem.getResearch_name()
					, problem.getNature()
					, problem.getWay()
					, problem.getIntroduction()
					, problem.getRequirement()
					, problem.getProblem_id()
					);
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Problem queryByProblem_id(int problem_id) {
		String sql = "select * from t_problem where problem_id = ?";
		
		try {
			return runner.query(sql, new BeanHandler<>(Problem.class), problem_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 得到指定选题方式的所有课题
	 * @param way 0表示盲选，其它则表示指定学生的ID
	 * @return 返回指定选题方式的所有课题
	 */
	public List<Problem> queryByWay(int way) {
		String sql = "select * from t_problem where way = ?";
		try {
			return runner.query(sql, new BeanListHandler<>(Problem.class), way);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
