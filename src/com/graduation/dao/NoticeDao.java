package com.graduation.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.graduation.db.DBUtils;
import com.graduation.entity.Notice;
import com.graduation.entity.Student;

public class NoticeDao {
	private QueryRunner runner = DBUtils.getQueryRunner();
	
	public boolean save(Notice notice) {
		
		String sql = "insert into t_notice(content, start, end, p1, p2, p4) "
				+ "values(?, ?, ?, ?, ?, ?)";
		try {
			int num = runner.update(sql
					, notice.getContent()
					, notice.getStart()
					, notice.getEnd()
					, notice.getP1()
					, notice.getP2()
					, notice.getP4()
					);
			BigInteger id =  runner.query("SELECT LAST_INSERT_ID()", new ScalarHandler<BigInteger>());
			System.out.println("Save Notice ID: " + id.toString());
			notice.setNotice_id(Integer.parseInt(id.toString()));
			return num > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean updateAll(Notice notice) {
		
		String sql = "update t_notice set content = ?, start = ?, end = ?, p1 = ?, p2 = ?, p4 = ? where notice_id = ?";
		try {
			return runner.update(sql
					, notice.getContent()
					, notice.getStart()
					, notice.getEnd()
					, notice.getP1()
					, notice.getP2()
					, notice.getP4()
					, notice.getNotice_id()
					) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean remove(int notice_id) {
		
		try {
			String sql = "delete from t_notice where notice_id = ?";
			return runner.update(sql, notice_id) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Notice> queryAll() {
		try {
			String sql = "select * from t_notice";
			return runner.query(sql, new BeanListHandler<>(Notice.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Notice> queryByPage(int page, int pageSize) {
		
		try {
			String sql = "select * from t_notice limit ?, ?";
			return runner.query(sql, new BeanListHandler<>(Notice.class), (page - 1) * pageSize, pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public long queryCount() {
		String sql = "select count(1) from t_notice";
		try {
			return runner.query(sql, new ScalarHandler<Long>());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	/**
	 * 查询当前所有可见的公告
	 * @return
	 */
	public List<Notice> queryAllCanSee() {
		
		Timestamp now = new Timestamp(new Date().getTime());
		
		try {
			String sql = "select * from t_notice where start < ? and end > ?";
			return runner.query(sql, new BeanListHandler<>(Notice.class), now, now);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 查询当前所有学生可见的公告
	 * @return
	 */
	public List<Notice> queryStudentCanSee() {
		
		Timestamp now = new Timestamp(new Date().getTime());
		
		try {
			String sql = "select * from t_notice where p1 = 1 and start < ? and end > ?";
			return runner.query(sql, new BeanListHandler<>(Notice.class), now, now);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	/**
	 * 查询当前所有教师可见的公告
	 * @return
	 */
	public List<Notice> queryTeacherCanSee() {
		
		Timestamp now = new Timestamp(new Date().getTime());
		
		try {
			String sql = "select * from t_notice where p2 = 1 and start < ? and end > ?";
			return runner.query(sql, new BeanListHandler<>(Notice.class), now, now);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	/**
	 * 查询当前所有专业负责人可见的公告
	 * @return
	 */
	public List<Notice> queryMajorTeacherCanSee() {
		
		Timestamp now = new Timestamp(new Date().getTime());
		
		try {
			String sql = "select * from t_notice where p4 = 1 and start < ? and end > ?";
			return runner.query(sql, new BeanListHandler<>(Notice.class), now, now);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
}
