package com.graduation.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.graduation.dao.TeacherDao;
import com.graduation.entity.Teacher;

public class TeacherDaoTest {

	private TeacherDao teacherDao = new TeacherDao();
	
	@Test
	public void saveTest() {
		Teacher teacher = new Teacher();
		teacher.setUsername("asd234");
		teacher.setPassword("123123");
		teacher.setRealname("张飞");
		teacher.setSex(0);
		teacher.setMid(1);
		teacher.setNumber(5);
		teacher.setTitle("教授");
		teacher.setDegree("博士");
		teacher.setQq("1462256645");
		teacher.setPhone("13233562322");
		teacher.setEmail("123123@qq.com");
		teacher.setPid(300001);
		teacher.setRemarks("xxxxxxx");
		boolean b =  teacherDao.saveTeacher(teacher);
		
		assertNotNull(b);
		
		System.out.println(teacher.getTea_id());
	}
	
	@Test
	public void removeTest() {
		
		boolean b = teacherDao.remove(20003);
		
		System.out.println(b);
		
		assertNotNull(b);
		
	}
	
	@Test
	public void updateAllTest() {
		Teacher teacher = teacherDao.queryByTea_id(20011);
		System.out.println(teacher.getPassword());
		teacher.setPassword("fjtidslk");
		boolean b = teacherDao.updateAll(teacher);
		System.out.println(b);
		assertNotNull(b);
	}
	
	@Test
	public void resetPassword() {
		boolean b = teacherDao.resetPassword(20011);
		System.out.println(b);
	}
	
	@Test
	public void queryByTea_id() {
		Teacher teacher = teacherDao.queryByTea_id(20008);
		System.out.println(teacher.toString());
		assertNotNull(teacher);
		
	}
	
	@Test
	public void queryAllTest() {
		List<Teacher> list = teacherDao.queryAll();
		System.out.println(list.size());
	}
	
	@Test
	public void queryByPage() {
		List<Teacher> list = teacherDao.queryByPage(2, 5);
		for (Teacher teacher : list) {
			System.out.println(teacher.toString());
		}
		assertNotNull(list);
	}
	
	@Test
	public void queryCount() {
		long num = teacherDao.queryCount();
		System.out.println(num);
		assertNotNull(num);
		
	}
	

}
