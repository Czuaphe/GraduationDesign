package com.graduation.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.graduation.dao.AdminDao;
import com.graduation.entity.Admin;

public class AdminDaoTest {

	private AdminDao adminDao = new AdminDao();
	
	@Test
	public void test() {
		
		Admin admin = adminDao.queryByUsername("admin");
		
		System.out.println(admin.toString());
		
		assertNotNull(admin);
		
		
	}

}
