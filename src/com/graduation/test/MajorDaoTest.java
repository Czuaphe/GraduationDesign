package com.graduation.test;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.graduation.dao.MajorDao;

public class MajorDaoTest {

	private MajorDao majorDao = new MajorDao();
	
	@Test
	public void saveTest() {
		
	}
	
	@Test
	public void queryAllMajorTest() {
		
		
	}
	
	@Test
	public void getMajorNameByMIDTest() {
		String name = majorDao.queryByMID(1).getMajor();
		System.out.println(name);
	}
	
	@Test
	public void test() {
		int mid = majorDao.queryByMajorName("计算机科学与技术").getMid();
		System.out.println(mid);
	}

}
