package com.graduation.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.graduation.entity.Teacher;
import com.graduation.service.ProblemService;

public class ProblemServiceTest {

	ProblemService problemService = new ProblemService();
	
	@Test
	public void ShowProblemTest() {
		
		problemService.showProblem();
		
		JSONObject object = problemService.getJsonObjectOutput();
		System.out.println(object.toString());
		
	}
	
	
	
	@Test
	public void exportByTeacher() {
		//模拟出自己想要的对象
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpSession session = Mockito.mock(HttpSession.class);
		Teacher teacher = Mockito.mock(Teacher.class);
		
		// 建立他们之间的联系
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(teacher);
		
		// 模拟各个对象中的数据
		
		// 模拟 session 中的数据
		// 1表示学生登录，2表示教师登录，3表示管理员登录，4表示专业负责人登录
		when(session.getAttribute("act")).thenReturn(2);
		
		// 模拟 teacher 对象中的数据
		when(teacher.getTea_id()).thenReturn(20015);
		
		
		
		
		
	}

}
