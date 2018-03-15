package com.graduation.test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	@Test
	public void testDate() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		System.out.println(df.parse("2018-03-15 00:00\n").getTime());
	}

}
