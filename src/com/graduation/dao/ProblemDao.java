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
	public boolean updateAll(Problem problem) {
		
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
	
	public List<Problem> queryAll() {
		
		try {
			String sql = "select * from t_problem";
			return runner.query(sql, new BeanListHandler<>(Problem.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
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
	/**
	 * 根据页数和每页的数量返回当前页的所有课题
	 * @param page 页数
	 * @param pageSize 每页的数量
	 * @return 返回得到的所有课题
	 */
	public List<Problem> queryByPage(int page, int pageSize) {
		
		try {
			String sql = "select * from t_problem limit ?, ?";
			return runner.query(sql, new BeanListHandler<>(Problem.class), (page - 1) * pageSize, pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	/**
	 * 查询课题的总数量
	 * @return 返回一个 Long 类型的变量
	 */
	public long queryCount() {
		String sql = "select count(1) from t_problem";
		try {
			return runner.query(sql, new ScalarHandler<Long>());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	/**
	 * 得到一个专业的当前页的课题
	 * @param mid 专业ID
	 * @param page 当前页
	 * @param pageSize 每页的数量
	 * @return
	 */
	public List<Problem> queryByMIDPage(int mid, int page, int pageSize) {
		
		try {
			String sql = "select * from t_problem where mid = ? limit ?, ?";
			return runner.query(sql, new BeanListHandler<>(Problem.class), mid, (page - 1) * pageSize, pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	/**
	 * 查询一个专业的课题总数量
	 * @param mid
	 * @return
	 */
	public long queryByMIDCount(int mid) {
		
		String sql = "select count(1) from t_problem where mid = ?";
		try {
			return runner.query(sql, new ScalarHandler<Long>(), mid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
		
	}
	
	public List<Problem> queryByTea_id(int tea_id) {
		
		try {
			String sql = "select * from t_problem where tea_id = ?";
			return runner.query(sql, new BeanListHandler<>(Problem.class), tea_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public List<Problem> queryByMid(int mid) {
		
		try {
			String sql = "select * from t_problem where mid = ?";
			return runner.query(sql, new BeanListHandler<>(Problem.class), mid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	

}
