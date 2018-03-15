package com.graduation.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import oracle.net.aso.l;
import oracle.net.aso.r;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.graduation.dao.MajorDao;
import com.graduation.dao.SelectedDao;
import com.graduation.dao.StudentDao;
import com.graduation.entity.Major;
import com.graduation.entity.Selected;
import com.graduation.entity.Student;

public class StudentService {
	
	private StudentDao studentDao = new StudentDao();
	private MajorDao majorDao = new MajorDao();
	private SelectedDao selectedDao = new SelectedDao();
	
	private List<String> pathList = new ArrayList<>();
	
	private HttpServletRequest request = null;
	
	private JSONObject jsonObjectOutput = new JSONObject();
	
	public StudentService() {}
	
	public StudentService(List<String> pathList, HttpServletRequest request) {
		this.pathList = pathList;
		this.request = request;
	}
	
	public JSONObject redirectToPath() {
		
		HttpSession session = request.getSession();
		
//		// 学生
//		session.setAttribute("act", 1);
//		session.setAttribute("user", studentDao.queryByStu_id(10011));
		
		switch (pathList.get(1)) {
		case "show":
			showStudent();
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
		case "select":
			selectStudent();
			break;
		case "modify":
			modifyStudent();
			break;
		default:
			break;
		}
		
		return jsonObjectOutput;
	}
	
	public void showStudent() {
		
		jsonObjectOutput = new JSONObject();
		
		
System.out.println("学生进行选题中。。。");
		
		HttpSession session = request.getSession();
		// 没有登录
		Object actObject = session.getAttribute("act");
		String error = null;
		if (actObject == null) {
			error = "Not Login";
			System.out.println(error);
			return ;
		}
		
		int act = Integer.parseInt(String.valueOf(actObject));
		
		//学生登录
		if (act == 1) {
			System.out.println("显示学生的个人信息。。。");
			Student student = (Student)session.getAttribute("user");
			
			JSONArray jsonArray = new JSONArray();
			
			List<Object> studentList = toObjectShow4Student(student);
			
			for (Object object : studentList) {
				jsonArray.add(object);
			}
			
			jsonObjectOutput.put("status", true);
			jsonObjectOutput.put("info", jsonArray);
			
			System.out.println("完成");
		} else if (act == 3) {
			// 管理员登录
			int  page =  Integer.parseInt(request.getParameter("currentPage"));
			
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
//				objectList = produceTeacherOfShow(objectList);
				
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
			
		} else {
			error = "登录用户类型错误";
			System.out.println(error);
		}
		
		
		
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
		jsonObjectOutput = new JSONObject();
		
		// 将上传的文件转换成一个对象列表
		List<List<Object>> studentList = new ArrayList<>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			
			request.setCharacterEncoding("utf-8");
			
			List<FileItem> list = upload.parseRequest(new ServletRequestContext(request));
			System.out.println("文件数量为：" + list.size());
			// 读取每 一个文件
			if (list.get(0) != null) {
				FileItem item = list.get(0);
				String filename = item.getName();
				System.out.println("文件名为：" + filename);
				String extName = filename.substring(filename.lastIndexOf("."));
				System.out.println("后缀为：" + extName);
				InputStream inputStream = item.getInputStream();
				if (extName.equals(".xlsx")) {
					studentList = importXlsxData(inputStream);
				} else if (extName.equals(".xls")) {
					studentList = importXlsData(inputStream);
				} else {
					// 文件不是
					System.out.println("文件不是规则格式");
				}
				
				inputStream.close();
			} else {
				
				// 没有找到文件
				System.out.println("没有找到文件");
			}
			
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 开始创建JSON数据
		JSONArray teacherArray = new JSONArray();
		
		System.out.println(studentList.size());
		
		for (List<Object> teacher : studentList) {
			
			JSONArray array = new JSONArray();
			for (int i = 0; i < teacher.size(); i ++) {
				Object object = teacher.get(i);
				if (i == 2) {
					object = object.equals("男")? 0 : 1;
				}
				if (i == 3) {
					object = majorDao.queryByMajorName(String.valueOf(object)).getMid();

				}
//				if (i == 6) {
//					System.out.println("index 6 : " + object);
//				}
//				if (i == 7) {
//					System.out.println("index 7 : " + object);
//				}
				array.add(object);
			}
			teacherArray.add(array);
		}
		
		jsonObjectOutput.put("status", true);
		
		jsonObjectOutput.put("infos", teacherArray);
		
	}
	
	public List<List<Object>> importXlsxData(InputStream inputStream) {
		
		System.out.println("开始加载数据中。。。");
		
		List<List<Object>> studentList = new ArrayList<>();
		
		try {
			
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
			// 目前只读取第一个Sheet的数据
			Sheet sheet = xssfWorkbook.getSheetAt(0);
			System.out.println("一共有" + sheet.getLastRowNum() + "条数据");
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum ++) {
//				System.out.println("正在加载第" + rowNum + "数据");
				Row row = sheet.getRow(rowNum);
				List<Object> list = new ArrayList<>();
				for (int i = 0; i < 8; i++) {
					Cell cell = row.getCell(i);
					
					list.add(cell.getStringCellValue());
					
//					System.out.println(i);
				}
//				System.out.println(list.size());
				
				studentList.add(list);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return studentList;
	}
	
	/**
	 * TODO 待开发
	 * @param inputStream
	 * @return
	 */
	public List<List<Object>> importXlsData(InputStream inputStream) {
		
		return null;
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
	/**
	 * 学生选题
	 */
	public void selectStudent() {
		
		jsonObjectOutput = new JSONObject();
		
		System.out.println("学生进行选题中。。。");
		
		HttpSession session = request.getSession();
		// 没有登录
		Object actObject = session.getAttribute("act");
		String error = null;
		if (actObject == null) {
			error = "Not Login";
			System.out.println(error);
			return ;
		}
		
		int act = Integer.parseInt(String.valueOf(actObject));
		
		//不是学生登录
		if (act != 1) {
			error = "Not Studnet Login";
			System.out.println(error);
			return ;
		}
		
		Student student = (Student) session.getAttribute("user");
		
		int problem_id = Integer.parseInt(request.getParameter("pro_id"));
		// 选题状态
		boolean flag = false;
		
		Connection connection = studentDao.getConnection();
		
		try {
			connection.setAutoCommit(false);
			
			Selected selected = selectedDao.queryByProblem_id(connection, problem_id);
			
			if (selected == null) {
				// 当前课题可选
				selected = new Selected();
				selected.setStu_id(student.getStu_id());
				selected.setProblem_id(problem_id);
				selected.setTime(new Timestamp(new Date().getTime()));
				
				flag = selectedDao.save(connection, selected);
			} else {
				// 已经有人选择该课题
				System.out.println("already Has Student Select This Problem");
				System.out.println("Selected Student id :" + selected.getStu_id());
			}
			
			connection.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		jsonObjectOutput.put("status", flag);
		
	}
	
	public void modifyStudent() {
		
		jsonObjectOutput = new JSONObject();
		
		HttpSession session = request.getSession();
		
		// 没有登录
		Object actObject = session.getAttribute("act");
		String error = null;
		if (actObject == null) {
			error = "Not Login";
			System.out.println(error);
			return ;
		}
		
		int act = Integer.parseInt(String.valueOf(actObject));
		
		//不是学生登录
		if (act != 1) {
			error = "Not Studnet Login";
			System.out.println(error);
			return ;
		}
		
		Student student = (Student) session.getAttribute("user");
		
		
		String[] studentStringArray = request.getParameterValues("info[]");
		
		System.out.println("StudentParameterArray Size : " + studentStringArray.length);
		
		List<Object> studentObjectList = new ArrayList<>();
		
		for (String string : studentStringArray) {
			studentObjectList.add(string);
		}
		
		Student studentModify = toBeanModify(studentObjectList);
		
		// 将其它的信息加入对象中
		studentModify.setStu_id(student.getStu_id());
		studentModify.setUsername(student.getUsername());
		studentModify.setRemarks(student.getRemarks());
		if (studentModify.getPassword() == null) {
			studentModify.setPassword(student.getPassword());
		}
		System.out.println("要更改的学生信息为：" + studentModify.toString());
		System.out.println("更改学生信息中。。。");
		
		boolean flag = studentDao.updateAll(studentModify);
		
		jsonObjectOutput.put("status", flag);
		
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
	
	public List<Object> toObjectShow4Student(Student student) {
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
	
	public Student toBeanModify(List<Object> list) {
		
		Student student = new Student();
		
		student.setRealname(String.valueOf(list.get(0)));
		student.setSex(Integer.parseInt(String.valueOf(list.get(1))));
		student.setPhone(String.valueOf(list.get(2)));
		student.setQq(String.valueOf(list.get(3)));
		student.setEmail(String.valueOf(list.get(4)));
		
		if (list.get(5) != null && list.get(5).equals(list.get(6))) {
			student.setPassword(String.valueOf(list.get(5)));
		} else {
			student.setPassword(null);
		}
		
		return student;
	}

}
