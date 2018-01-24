package com.graduation.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.graduation.dao.ProblemDao;
import com.graduation.entity.Problem;

public class ProblemDaoTest {

	private ProblemDao problemDao = new ProblemDao();
	
	@Test
	public void saveTest() {
		
		Problem problem = new Problem();
		
		problem.setName("课题题目测试3");
		problem.setMid(5);
		problem.setIs_new(0);
		problem.setType(0);
		problem.setSource(0);
		problem.setTea_id(20015);
		
		System.out.println(problem.getProblem_time());
		
		problem.setIntroduction("课题简介测试");
		problem.setRequirement("课题要求测试");
		
		boolean b = problemDao.save(problem);
		System.out.println(b);
		assertNotNull(b);
		
	}
	
	@Test
	public void DeleteTest() {
		
		boolean b = problemDao.remove(1);
		System.out.println(b);
	}
	
	@Test
	public void queryByPrblem_idTest() {
		Problem problem = problemDao.queryByProblem_id(3);
		
		System.out.println(problem.toString());
		
		assertNotNull(problem);
	}
	
	@Test
	public void updateProblemTest() {
		
		Problem problem = problemDao.queryByProblem_id(3);
		problem.setName("选题题目名称修改测试");
		problem.setMid(4);
		boolean b = problemDao.updateProblem(problem);
		System.out.println(b);
		assertNotNull(b);
		
	}

}
