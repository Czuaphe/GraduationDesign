package com.graduation.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.graduation.dao.AdminDao;
import com.graduation.dao.MajorDao;
import com.graduation.dao.StudentDao;
import com.graduation.dao.TeacherDao;
import com.graduation.entity.Admin;
import com.graduation.entity.Student;
import com.graduation.entity.Teacher;

import net.sf.json.JSONObject;

public class CommonService {
	
	private TeacherDao teacherDao = new TeacherDao();
	private StudentDao studentDao=  new StudentDao();
	private AdminDao adminDao = new AdminDao();
	private MajorDao majorDao = new MajorDao();
	
	private List<String> pathList = new ArrayList<>();

	private HttpServletRequest request = null;
	
	private JSONObject jsonObjectOutput = new JSONObject();
	
	public CommonService() {}
	
	public CommonService(List<String> pathList, HttpServletRequest request) {
		this.pathList = pathList;
		this.request = request;
	}
	
	public JSONObject redirectToPath() {
		
		jsonObjectOutput = new JSONObject();
		
		switch (pathList.get(0)) {
		case "login":
			login();
			break;
		case "logout":
			
			break;
			
		default:
			break;
		}
		
		return jsonObjectOutput;
		
	}
	/**
	 * 登录函数，根据不同的用户类型跳转到对应的登录函数中
	 */
	public void login() {
		
		int act = Integer.parseInt(request.getParameter("act"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		// act 表示用户类型，1是学生，2是教师，3为管理员，4专业负责人
		switch (act) {
		case 1:
			loginStudent(username, password);
			break;
		case 2:
		case 4:
			loginTeacher(username, password);
			break;
		case 3:
			loginAdmin(username, password);
			break;
		default:
			System.out.println("末识别的用户类型！");
			break;
		}
	}
	
	public void loginStudent(String username, String password) {
		
		System.out.println("正在进行学生登录！");
		
		boolean flag = true;
		String error = null;
		
		Student student = studentDao.queryByUsername(username);
		
		if (student == null) {
			flag = false;
			error = "没有此用户！";
		} else if (student.getPassword().equals(password)) {
			// 密码相同，登录成功
			flag = true;
			HttpSession session = request.getSession();
			session.setAttribute("act", 1);
			session.setAttribute("user", student);
			session.setMaxInactiveInterval(600);
			
		} else {
			// 密码不同
			flag = false;
			error = "密码错误！";
		}
		
		jsonObjectOutput.put("status", flag);
		
		if (!flag) {
			jsonObjectOutput.put("info", error);
		}
		
		
	}
	
	public void loginTeacher(String username,String  password) {
		
		System.out.println("正在进行教师登录！");
		
		boolean flag = true;
		String error = null;
		
		Teacher teacher = teacherDao.queryByUserName(username);
		
		if (teacher == null) {
			flag = false;
			error = "没有此用户！";
		} else if (teacher.getPassword().equals(password)) {
			// 密码相同，登录成功
			flag = true;
			
			// TODO 判断是否为专业负责人
			HttpSession session = request.getSession();
			if (majorDao.queryByMID(teacher.getMid()) != null) {
				session.setAttribute("act", 4);
			} else {
				session.setAttribute("act", 2);
			}
			
			session.setAttribute("user", teacher);
			session.setMaxInactiveInterval(600);
		} else {
			// 密码不同
			flag = false;
			error = "密码错误！";
		}
		
		jsonObjectOutput.put("status", flag);
		
		if (!flag) {
			jsonObjectOutput.put("info", error);
		}
		
	}
	
	public void loginAdmin(String username,String password) {
		
		System.out.println("正在进行管理员登录！");
		
		boolean flag = true;
		String error = null;
		
		Admin admin = adminDao.queryByUsername(username);
		
		if (admin == null) {
			flag = false;
			error = "没有此用户！";
		} else if (admin.getPassword().equals(password)) {
			// 密码相同，登录成功
			flag = true;
			HttpSession session = request.getSession();
			session.setAttribute("act", 3);
			session.setAttribute("user", admin);
			session.setMaxInactiveInterval(600);
		} else {
			// 密码不同
			flag = false;
			error = "密码错误！";
		}
		
		jsonObjectOutput.put("status", flag);
		
		if (!flag) {
			jsonObjectOutput.put("info", error);
		}
		
	}
	
}
