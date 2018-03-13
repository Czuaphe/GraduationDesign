package com.graduation.test;

import static org.junit.Assert.*;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.graduation.dao.TeacherDao;
import com.graduation.entity.Teacher;
import com.graduation.service.TeacherService;

public class TeacherServiceTest {

	private TeacherService teacherService = new TeacherService();
	private TeacherDao teacherDao = new TeacherDao();
	
	@Test
	public void showTeacherTest() {
		int page = 2;
		teacherService.showTeacher();
		JSONObject jsonObject =  teacherService.getJsonObject();
		Object object = jsonObject.get("data");
		System.out.println(object.toString());
		
	}
	@Test
	public void deleteTeacherTest() {
		String[] list = {"20006", "20004"};
		teacherService.deleteTeacher(list);
		JSONObject jsonObject = teacherService.getJsonObject();
		System.out.println(jsonObject.toString());
	}
	
	@Test
	public void objectToJSONArray() {
		Teacher teacher = teacherDao.queryByTea_id(20011);
		System.out.println(teacher.toString());
		List<Object> list = teacherService.objectToJSONArray(teacher);
		for (Object object : list) {
			System.out.println(object);
		}
	}

}
