package com.graduation.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.graduation.dao.AdminDao;
import com.graduation.dao.StudentDao;
import com.graduation.dao.TeacherDao;
import com.graduation.service.CommonService;
import com.graduation.service.MajorService;
import com.graduation.service.NoticeService;
import com.graduation.service.ProblemService;
import com.graduation.service.StudentService;
import com.graduation.service.TeacherService;

public class AllServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3451497886919209570L;

	/**
	 * Constructor of the object.
	 */
	public AllServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		HttpSession session = request.getSession();
		// 管理员
		session.setAttribute("act", 3);
		session.setAttribute("user", new AdminDao().queryByUsername("admin"));
		// 学生
//		 session.setAttribute("act", 1);
//		 session.setAttribute("user", new StudentDao().queryByStu_id(10011));
		// 教师
//		 session.setAttribute("act", 2);
//		 session.setAttribute("user", new TeacherDao().queryByTea_id(20015));
		
		System.out.println( "pathInfo: " +  request.getPathInfo());
		System.out.println("ServletPath: " + request.getServletPath());
		// 得到访问的URL，例如：/teacher/show
		String path = request.getPathInfo();
		if (path == null || path.equals("")) {
			path = request.getServletPath();
		}
		// 去掉第一个/
		path = path.substring(1);
		
		// 切分出一级路由和二级路由，放入列表中
		String[] strings = path.split("/");
		List<String> pathList = new ArrayList<>();
		for (String string : strings) {
			pathList.add(string);
		}
		
		// 将路由信息放入request请求中
		request.setAttribute("path", pathList);
		
		// 定义要返回的JSON数据
		JSONObject jsonObjectOutput = new JSONObject();
		
		// 得到第一级路由，以确定跳转到对应的服务
		String firstPathString = strings[0];
		
		switch (firstPathString) {
		case "teacher":
			TeacherService teacherService = new TeacherService(pathList, request);
			// 得到要返回的JSON数据
			jsonObjectOutput = teacherService.redirectToPath();
			break;
		case "student":
			StudentService studentService = new StudentService(pathList, request);
			jsonObjectOutput = studentService.redirectToPath();
			break;
//		case "profession":
//			ProfessionService professionService = new ProfessionService(pathList, request);
//			jsonObjectOutput = professionService.redirectToPath();
//			break;
		case "major":
			MajorService majorService = new MajorService(pathList, request);
			jsonObjectOutput = majorService.redirectToPath();
			break;
		case "problem":
			// 因为课题有生成选题文件功能，返回的不是JSON数据
			ProblemService problemService = new ProblemService(pathList, request, response);
			jsonObjectOutput = problemService.redirectToPath();
			// 所以 ProblemService 中请求的返回值由自己选择
			return ;
		case "notice":
			NoticeService noticeService = new NoticeService(pathList, request);
			jsonObjectOutput = noticeService.redirectToPath();
			break;
		default:
			CommonService commonService = new CommonService(pathList, request, response);
			jsonObjectOutput = commonService.redirectToPath();
			break;
		}
		
		// 返回JSON数据
		PrintWriter out = response.getWriter();
		out.write(jsonObjectOutput.toString());
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("使用POST方式进行传入数据");
		
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
