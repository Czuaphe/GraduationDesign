package com.graduation.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.graduation.dao.StudentDao;
import com.graduation.entity.Student;

public class StudentDaoTest {

	private StudentDao studentDao = new StudentDao();
	
	@Test
	public void test() {
		
		Student student = studentDao.queryByStu_id(10005);
		System.out.println(student.toString());
		student.setQq("6543218");
		boolean b = studentDao.updateAll(student);
		System.out.println(b);
		System.out.println(student.toString());
		assertEquals(b, true);
	}

}
