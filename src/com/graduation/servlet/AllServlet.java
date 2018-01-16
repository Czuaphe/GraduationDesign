package com.graduation.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.graduation.service.TeacherService;

public class AllServlet extends HttpServlet {

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
		
		System.out.println(request.getPathInfo());
		// 得到访问的URL，例如：/teacher/show
		String path = request.getPathInfo();
		
		// 去掉第一个/
		path = path.substring(1);
		
		// 切分出一级路由和二级路由，放入列表中
		String[] strings = path.split("/");
		List<String> pathList = new ArrayList<>();
		for (String string : strings) {
			pathList.add(string);
		}
		
//		System.out.println(strings.length);
//		for (String string : strings) {
//			System.out.println(string);
//		}
		
		// 将路由信息放入request请求中
		request.setAttribute("path", pathList);
		
		// 定义要返回的JSON数据
		JSONObject jsonObjectOutput = new JSONObject();
		
		// 得到第一级路由，以确定跳转到对应的服务
		String firstPathString = strings[0];
		if (firstPathString.equals("teacher")) {
			TeacherService teacherService = new TeacherService(pathList, request);
			// 得到要返回的JSON数据
			jsonObjectOutput = teacherService.redirectToPath();
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
