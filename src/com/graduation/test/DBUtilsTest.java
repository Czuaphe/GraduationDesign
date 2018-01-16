package com.graduation.test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.graduation.db.DBUtils;

public class DBUtilsTest {

	@Test
	public void test() throws SQLException {
		System.out.println(DBUtils.getConnection());
		assertNotNull(DBUtils.getConnection());
	}
	
	@Test
	public void saveTeacher() throws SQLException {
		QueryRunner runner = DBUtils.getQueryRunner();
		
		String getIDSql = "select nextval('seq_tid')";
		
		int tid = runner.query(getIDSql, new ScalarHandler<Integer>());
		
		System.out.println(tid);
		assertNotNull(tid);
		
	}

}
