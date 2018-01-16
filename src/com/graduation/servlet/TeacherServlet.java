package com.graduation.servlet;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.graduation.dao.TeacherDao;
import com.graduation.db.DBUtils;
import com.graduation.entity.Teacher;

public class TeacherServlet extends HttpServlet {

	
	private TeacherDao teacherDao = new TeacherDao();
	
	/**
	 * Constructor of the object.
	 */
	public TeacherServlet() {
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

		
		@SuppressWarnings("unchecked")
		List<String> pathList = (ArrayList<String>)request.getAttribute("path");
		
		if (pathList.get(1).equals("show")) {
			Teacher teacher = teacherDao.queryByTea_id(200006);
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Origin", "*");
			
			JSONArray jsonList = new JSONArray();
			
			jsonList.add(arrayJSON(teacher));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("data", jsonList);
			jsonObject.put("currentPage", 1);
			jsonObject.put("status", true);
			jsonObject.put("totalPage", 1);
			
			
			
			PrintWriter out = response.getWriter();
			out.write(jsonObject.toString());
		}
		
		
		
		
		
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
//		out.println("<HTML>");
//		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//		out.println("  <BODY>");
//		out.print("    This is ");
//		out.print(this.getClass());
//		out.println(", using the GET method");
//		out.println("  </BODY>");
//		out.println("</HTML>");
//		out.flush();
//		out.close();
	}
	
	public List<Object> arrayJSON(Teacher teacher) {
		List<Object> list = new ArrayList<>();
		
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(teacher.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			
			System.out.println(propertyDescriptors.length);
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
				System.out.print(i + "\t");
				System.out.println(propertyDescriptor.getName());
			}
			// 14
			int[] mapping = DBUtils.getTeacherMapping();
			
			for (int i = 0; i < mapping.length; i++) {
				PropertyDescriptor propertyDescriptor = propertyDescriptors[mapping[i]];
				Method method = propertyDescriptor.getReadMethod();
				try {
					Object object = null;
					if (i == 3) {
						Object temp = method.invoke(teacher);
						if (((int)temp) == 0) {
							object = "男";
						}else {
							object = "女";
						}
					} else if (i == 4) {
						
						int mid = (int) method.invoke(teacher);
						// 应该从数据库中找到专业名称加入数组中
						String majorName = "计算机";
						object = majorName;
					} else if (i == 11) {
						// 此列为专业负责人ID
						int pid = (int) method.invoke(teacher);
						// 应该从数据库找到对应的专业负责人的名称
						object = "刘备";
						
					}
					
					else {
						object = method.invoke(teacher);
					}
					
					list.add(object);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			}
			
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
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
