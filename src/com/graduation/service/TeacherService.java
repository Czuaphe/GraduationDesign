package com.graduation.service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.junit.Test;

import com.graduation.dao.TeacherDao;
import com.graduation.db.DBUtils;
import com.graduation.entity.Teacher;
import com.sun.corba.se.spi.orb.StringPair;

import net.bytebuddy.asm.Advice.This;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TeacherService {
	
	private TeacherDao teacherDao = new TeacherDao();
	
	private List<String> pathList = new ArrayList<>();
	
	private HttpServletRequest request = null;
	
	private JSONObject jsonObjectOutput = new JSONObject();
	
	public TeacherService() {}
	
	public TeacherService(List<String> pathList, HttpServletRequest request) {
		this.pathList = pathList;
		this.request = request;
	}
	
	/**
	 * 根据路径导向不同的函数中去
	 * @return 要返回的JSON数据
	 */
	public JSONObject redirectToPath() {
		
		
		switch (pathList.get(1)) {
		case "show":
			String  page =  request.getParameter("currentPage");
//			System.out.println("currentPage=" + page);
			showTeacher(Integer.parseInt(page));
			break;
		case "dels":
			
			String[] delList = request.getParameterValues("ids[]");
			
			// 显示request中的的所有参数
//			Map map=request.getParameterMap();
//		    Set keSet=map.entrySet();
//		    for(Iterator itr=keSet.iterator();itr.hasNext();){
//		        Map.Entry me=(Map.Entry)itr.next();
//		        Object ok=me.getKey();
//		        // 过滤数据
//				if (ok.equals("ids[]")) {
//		        	Object ov=me.getValue();
//			        String[] value=new String[1];
//			        if(ov instanceof String[]){
//			            value=(String[])ov;
//			        }else{
//			            value[0]=ov.toString();
//			        }
//
//			        for(int k=0;k<value.length;k++){
//			            System.out.println(ok+"="+value[k]);
//			        }
//				}
//		        
//		     }
			
//			for (String string : list) {
//				System.out.println(string);
//			}
			
		    deleteTeacher(delList);
		    break;
		    
		case "update":
			List<String[]> updateStringList = new ArrayList<>();
			int num = 0;
			while (true) {
				String infos = "infos["+ (num ++) + "][]";
				String[] updateList = request.getParameterValues(infos);
				if (updateList != null) {
					updateStringList.add(updateList);
				} else {
					break;
				}
			}
			
			System.out.println(updateStringList.size());
			
			updateTeacher(updateStringList);
		    break;
		    
		case "resetPassword":
			
			String id = request.getParameter("id");
			
			resetPassword(id);
			
			break;
		default:
			break;
		}
		
//		if (pathList.get(1).equals("show")) {
//			
//			String  page =  request.getParameter("currentPage");
//			System.out.println("page=" + page);
//			showTeacher(Integer.parseInt(page));
//		}
		
		return jsonObjectOutput;
		
	}
	/**
	 * 显示教师的信息，将信息转换成JSON数据
	 * @param page 要显示的页数
	 */
	public void showTeacher(int page) {
		
		//Teacher teacher = teacherDao.queryByTid(200006);
		// 每页的大小
		int pageSize = 5;
		// 得到对应页的数据
		List<Teacher> list = teacherDao.queryByPage(page, pageSize);
		// 得到要显示的总数量
		long count = teacherDao.queryCount();
		
//		System.out.println("Teacher Numbers:" +  list.size());
		// 定义要返回的教师数据的JSON对象
		JSONArray jsonList = new JSONArray();
		
		for (Teacher teacher : list) {
			// 得到每一个教师的信息数组
			List<Object> objectList = teacher.showInfo();
			// 对信息数组进行加工
			objectList = produceTeacher(objectList);
			// 加入到要返回的教师数据的JSON对象中
			jsonList.add(objectList);
		}
		// 设置要返回的教师数据
		jsonObjectOutput.put("data", jsonList);
		// 设置当前页
		jsonObjectOutput.put("currentPage", page);
		// 设置返回状态
		jsonObjectOutput.put("status", true);
		// 设置总页数
		jsonObjectOutput.put("totalPage", (count - 1) / pageSize + 1 );
		
	}
	/**
	 * 删除教师
	 * @param list 要删除的教师的ID的数组
	 */
	public void deleteTeacher(String[] list) {
		
		List<String> delList = new ArrayList<>();
		
		if (list.length == 0) {
			jsonObjectOutput = new JSONObject();
		} else {
			
			for (String string : list) {
				delList.add(string);
			}
			
			for (String string : delList) {
				System.out.println("delete id:" + string);
			}
			// 存放每一个教师的删除结果
			List<Boolean> delFlagList = new ArrayList<>(delList.size());
			
			for (int i = 0; i < delList.size(); i++) {
				int tea_id = Integer.parseInt(delList.get(i));
				boolean flag = teacherDao.remove(tea_id);
				delFlagList.add(flag);
			}
			
			JSONArray delArray = new JSONArray();
			for (int i = 0; i < delFlagList.size(); i ++) {
				if (delFlagList.get(i)) {
					delArray.add(delList.get(i));
				}
			}
			
			jsonObjectOutput.put("status", true);
			// 设置返回删除成功的教师的ID
			jsonObjectOutput.put("ids", delArray);
			
		}
		
	}
	
	public void updateTeacher(List<String[]> updateList) {
		// 要更新的教师列表
		List<Teacher> updateTeacherList = new ArrayList<>();
		// 更新状态列表
		List<Boolean> updateFlagList = new ArrayList<>();
		// 将所有要更新的数组转换成对象
		for (String[] strings : updateList) {
			List<Object> list = new ArrayList<>();
			for (int i = 0; i < strings.length; i++) {
				list.add(strings[i]);
			}
			// 反向加工
			list = produceTeacherReverse(list);
			
			// 对象数组变成一个对象加入到要更新列表中
			updateTeacherList.add(toBean(list));
		}
		// 对所有的对象进行更新
		for (int i = 0; i < updateTeacherList.size(); i++) {
			boolean b = teacherDao.updateAll(updateTeacherList.get(i));
			updateFlagList.add(b);
		}
		
		
		jsonObjectOutput.put("status", true);
		
	}
	
	public void resetPassword(String id) {
		
		boolean b = teacherDao.resetPassword(Integer.parseInt(id));
		jsonObjectOutput.put("status", b);
		
	}
	
	public JSONObject getJsonObject() {
		return jsonObjectOutput;
	}
	
	/**
	 * 这个函数对教师的信息数组 中的某些信息进行加工
	 * @param list 传入一个包含教师信息的Object数组
	 * @return List<Object> 返回相同的数组
	 */
	private List<Object> produceTeacher(List<Object> list) {
		
		List<Object> realList = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			// 对性别字段进行加工
//			if (i == 3) {
//				
//				if (((int) object ) == 0) {
//					object = "男";
//				} else {
//					object = "女";
//				}
//			}
//			if (i == 4) {
//				// 用专业ID从数据库中找到对应的专业的名称
//				object = "计算机";
//			}
			if (i == 11) {
				// 从数据库中找到对应的专业负责人的真实姓名
				object = "刘备";
			}
			
			realList.add(object);
		}
		
		return realList;
	}
	
	public List<Object> produceTeacherReverse(List<Object> list) {
		
		List<Object> realList = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			// 对性别字段进行反向加工
//			if (i == 3) {
//				
//				if (((String)object).equals("男")) {
//					object = 0;
//				}else {
//					object = 1;
//				}
//				
////				if (((int) object ) == 0) {
////					object = "男";
////				} else {
////					object = "女";
////				}
//			}
//			if (i == 4) {
//				// 用专业ID从数据库中找到对应的专业的名称
//				//object = "计算机";
//				object = 1;
//			}
			// 对专业负责人字段进行反向加工
			if (i == 11) {
				// 从数据库中找到专业负责人的真实姓名对应的ID
				object= 300001;
			}
			
			realList.add(object);
		}
		
		return realList;
		
	}
	/**
	 * 将一个List<Object>列表转换成一个教师对象
	 * @param list 要转换的列表数据
	 * @return 一个教师对象
	 */
	public Teacher toBean(List<Object> list) {
		
		Teacher teacher = new Teacher();
		teacher.setTea_id(Integer.parseInt(String.valueOf(list.get(0))));
		teacher.setUsername(String.valueOf(list.get(1)));
		teacher.setRealname(String.valueOf(list.get(2)));
		teacher.setSex(Integer.parseInt(String.valueOf(list.get(3))));
//		System.out.println("sex = " + teacher.getSex());
		teacher.setMid(Integer.parseInt(String.valueOf(list.get(4))));
		teacher.setTitle(String.valueOf(list.get(5)));
		teacher.setDegree(String.valueOf(list.get(6)));
		teacher.setQq(String.valueOf(list.get(7)));
		teacher.setPhone(String.valueOf(list.get(8)));
		teacher.setEmail(String.valueOf(list.get(9)));
		teacher.setRemarks(String.valueOf(list.get(10)));
		teacher.setPid(Integer.parseInt(String.valueOf(list.get(11))));
		teacher.setNumber(Integer.parseInt(String.valueOf(list.get(12))));
		return teacher;
	}
	
	@Deprecated
	public List<Object> objectToJSONArray(Teacher teacher) {
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
					
					list.add(method.invoke(teacher));
					
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			}
			// 对数组中的数据进行加工
			list = produceTeacher(list);
			
			
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Deprecated
	public Teacher JSONArrayToObject(JSONArray array) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Teacher teacher = new Teacher();
		
		BeanInfo beanInfo = Introspector.getBeanInfo(teacher.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		
		int[] mapping = DBUtils.getTeacherMapping();
		
		for (int i = 0; i < mapping.length; i++) {
			Object object = array.get(i);
			
			Method method = propertyDescriptors[mapping[i]].getWriteMethod();
			method.invoke(teacher, object);
			
		}
		
		
		return teacher;
	}
	
}
