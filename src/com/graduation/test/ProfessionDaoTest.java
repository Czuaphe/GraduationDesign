package com.graduation.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.graduation.dao.ProfessionDao;

public class ProfessionDaoTest {

	private ProfessionDao professionDao = new ProfessionDao();
	
	@Test
	public void getTeaIDByPID() {
		
		int tea_id = professionDao.getTeaIDByPID(30001);
		System.out.println(tea_id);
		
	}

}
