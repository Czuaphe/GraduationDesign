package com.graduation.test;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.graduation.dao.MajorDao;

public class MajorDaoTest {

	private MajorDao majorDao = new MajorDao();
	
	@Test
	public void queryAllMajorTest() {
		Map<Integer, String> map = majorDao.getAllMajor();
		
		Set<Integer> set = map.keySet();
		for (Integer string : set) {
			System.out.println(string + " : " + map.get(string));
		}
		
	}
	
	@Test
	public void getMajorNameByMIDTest() {
		String name = majorDao.getMajorNameByMID(1);
		System.out.println(name);
	}
	
	@Test
	public void test() {
		int mid = majorDao.getMIDByMajorName("计算机科学与技术");
		System.out.println(mid);
	}

}
