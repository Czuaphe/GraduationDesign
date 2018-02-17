package com.graduation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.graduation.dao.MajorDao;
import com.graduation.dao.StudentDao;
import com.graduation.entity.Major;
import com.graduation.entity.Student;

public class StudentService {
	
	private StudentDao studentDao = new StudentDao();
	private MajorDao majorDao = new MajorDao();
	
	private List<String> pathList = new ArrayList<>();
	
	private HttpServletRequest request = null;
	
	private JSONObject jsonObjectOutput = new JSONObject();
	
	public StudentService() {}
	
	public StudentService(List<String> pathList, HttpServletRequest request) {
		this.pathList = pathList;
		this.request = request;
	}
	
	public JSONObject redirectToPath() {
		
		switch (pathList.get(1)) {
		case "show":
			String  page =  request.getParameter("currentPage");
//			System.out.println("currentPage=" + page);
			showStudent(Integer.parseInt(page));
			break;
		case "dels":
			String[] delList = request.getParameterValues("ids[]");
			deleteStudent(delList);
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
			
			updateStudent(updateStringList);
			break;
		case "add":
			System.out.println("正在添加教师到数据库中！");
			String[] infoList = request.getParameterValues("info[]");
			addStudent(infoList);
			break;
		case "resetPassword":
			String id = request.getParameter("id");
			resetPassword(id);
			break;
		case "import":
			System.out.println("正在上传文件");
			importStudent(request);
			break;
		case "info":
			String username = request.getParameter("stu_id");
			getStudentInfo(username);
			break;
		default:
			break;
		}
		
		return jsonObjectOutput;
	}
	
	public void showStudent(int page) {
		
		jsonObjectOutput = new JSONObject();
		
		// 每页的大小
		int pageSize = 5;
		// 得到对应页的数据
		List<Student> list = studentDao.queryByPage(page, pageSize);
		// 得到要显示的总数量
		long count = studentDao.queryCount();
		
		// 定义要返回的教师数据的JSON对象
		JSONArray jsonList = new JSONArray();
		
		/* 将对象转换成JSON数据 */
		for (Student student : list) {
			// 把教师对象转换成Object数组
			List<Object> objectList = toObjectList(student);
			// 不需要对Object数组进行加工
//			objectList = produceTeacherOfShow(objectList);
			
			// 加入到要返回的教师数据的JSON对象中
			jsonList.add(objectList);
		}
		
		// 返回所有专业
		jsonObjectOutput.put("majors", getAllMajors());
		
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
	 * 得到所有的专业
	 * @return
	 */
	public JSONArray getAllMajors() {
		
		List<Major> majors = majorDao.getAllMajor();
		JSONArray majorArray = new JSONArray();
		for (Major major : majors) {
			JSONArray array = new JSONArray();
			array.add(major.getMid());
			array.add(major.getMajor());
			majorArray.add(array);
		}
		
		return majorArray;
	}
	
	public void deleteStudent(String[] list) {
		jsonObjectOutput = new JSONObject();
		
		// 数据为空则也返回空数据
		if (list.length == 0) {
			jsonObjectOutput = new JSONObject();
		} else {
			
			List<String> delList = new ArrayList<>();
			
			for (String string : list) {
				delList.add(string);
			}
			
			for (String string : delList) {
				System.out.println("delete id:" + string);
			}
			// 存放每一个教师的删除结果
			List<Boolean> delFlagList = new ArrayList<>(delList.size());
			
			for (int i = 0; i < delList.size(); i++) {
				int stu_id = Integer.parseInt(delList.get(i));
				boolean flag = studentDao.remove(stu_id);
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
	
	public void updateStudent(List<String[]> updateList) {
		jsonObjectOutput = new JSONObject();
		// 要更新的教师列表
		List<Student> updateTeacherList = new ArrayList<>();
//		// 更新状态列表
//		List<Boolean> updateFlagList = new ArrayList<>();
		// 将所有要更新的数组转换成对象
		for (String[] strings : updateList) {
			List<Object> list = new ArrayList<>();
			for (int i = 0; i < strings.length; i++) {
				list.add(strings[i]);
			}
			// 对象数组变成一个对象加入到要更新列表中
			updateTeacherList.add(toBeanUpdate(list));
		}
		// 对所有的对象进行更新，并将更新状态存入数组中
		boolean flag = true;
		for (int i = 0; i < updateTeacherList.size(); i++) {
			System.out.println("Update Student :" + updateTeacherList.get(i).toString());
			flag = studentDao.updateAll(updateTeacherList.get(i));
			if (!flag) {
				break;
			}
//			updateFlagList.add(b);
		}
		
		jsonObjectOutput.put("status", flag);
	}

	public void resetPassword(String id) {
		System.out.println("正在重置密码中。。。");
		
		jsonObjectOutput = new JSONObject();
		boolean b = studentDao.resetPassword(Integer.parseInt(id));
		jsonObjectOutput.put("status", b);
		
		System.out.println("重置密码成功！");
	}
	
	public void addStudent(String[] infoList) {
		
		jsonObjectOutput = new JSONObject();
		
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < infoList.length; i++) {
			list.add(infoList[i]);
		}
		
//		for (Object object : list) {
//			System.out.println(object == null);
//			System.out.println(object.toString());
//		}
		
		// 进行数据检验
		Map<Integer, String> error = checkStudent(list);
		System.out.println("Add Student Errors : " + error.size());
		if (error.size() != 0) {
			
			jsonObjectOutput.put("status", false);
			JSONArray errorArray = new JSONArray();
			
			Set<Integer> set = error.keySet();
			
			for (Integer integer : set) {
				JSONArray array = new JSONArray();
				array.add(integer);
				array.add(error.get(integer));
				errorArray.add(array);
			}
			
			jsonObjectOutput.put("errors", errorArray);
			
		} else {
			// 数据检验成功，进行下一步
			Student student = toBeanAdd(list);
			
			boolean b = studentDao.save(student);
			
			jsonObjectOutput.put("status", b);
			
		}
	}
	
	public Map<Integer, String> checkStudent(List<Object> list) {
		Map<Integer, String> map = new HashMap<>();
		
		String NOT_NULL = "不能为空！";
		
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			// 对用户名进行检验
			if (i == 0) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
				//String name = (String) object;
			}
			// 对姓名进行检验
			if (i == 1) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 性别 
			if (i == 2) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 专业ID
			if (i == 3) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// QQ
			if (i == 4) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 手机
			if (i == 5) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 邮箱
			if (i == 6) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 备注可以为空
			if (i == 7) {
				
			}
		}
		
		return map;
	}
	
	/**
	 * TODO 还没有开发，等待中
	 * @param request
	 */
	public void importStudent(HttpServletRequest request) {
		
	}
	/**
	 * 返回中学生的部分信息
	 * @param username
	 */
	public void getStudentInfo(String username) {
		
		jsonObjectOutput = new JSONObject();
		
		Student student = studentDao.queryByUsername(username);
		
		if (student == null) {
			jsonObjectOutput.put("status", false);
		} else {
			jsonObjectOutput.put("status", true);
			jsonObjectOutput.put("id", student.getStu_id());
			jsonObjectOutput.put("stu_name", student.getRealname());
		}
		
	}
	
	public List<Object> toObjectList(Student student) {
		
		List<Object> list = new ArrayList<>();
		list.add(student.getStu_id());
		list.add(student.getUsername());
		list.add(student.getRealname());
		list.add(student.getSex());
		list.add(student.getMid());
		list.add(student.getQq());
		list.add(student.getPhone());
		list.add(student.getEmail());
		list.add(student.getRemarks());
		
		return list;
		
	}
	
	public Student toBeanUpdate(List<Object> list) {
		Student student = new Student();
		student.setStu_id(Integer.parseInt(String.valueOf(list.get(0))));
		student.setUsername(String.valueOf(list.get(1)));
		student.setRealname(String.valueOf(list.get(2)));
		student.setSex(Integer.parseInt(String.valueOf(list.get(3))));
		student.setMid(Integer.parseInt(String.valueOf(list.get(4))));
		student.setQq(String.valueOf(list.get(5)));
		student.setPhone(String.valueOf(list.get(6)));
		student.setEmail(String.valueOf(list.get(7)));
		student.setRemarks(String.valueOf(list.get(8)));
		
		return student;
		
	}
	
	public Student toBeanAdd(List<Object> list) {
		Student student = new Student();
		student.setUsername(String.valueOf(list.get(0)));
		student.setRealname(String.valueOf(list.get(1)));
		student.setSex(Integer.parseInt(String.valueOf(list.get(2))));
		student.setMid(Integer.parseInt(String.valueOf(list.get(3))));
		student.setQq(String.valueOf(list.get(4)));
		student.setPhone(String.valueOf(list.get(5)));
		student.setEmail(String.valueOf(list.get(6)));
		student.setRemarks(String.valueOf(list.get(7)));
		student.setPassword("123456");
		
		return student;
	}

}
