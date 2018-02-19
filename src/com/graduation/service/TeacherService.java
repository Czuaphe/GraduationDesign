package com.graduation.service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.graduation.dao.MajorDao;
import com.graduation.dao.ProblemDao;
import com.graduation.dao.TeacherDao;
import com.graduation.db.DBUtils;
import com.graduation.entity.Major;
import com.graduation.entity.Problem;
import com.graduation.entity.Teacher;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TeacherService {
	
	private TeacherDao teacherDao = new TeacherDao();
	private ProblemDao problemDao = new ProblemDao();
	private MajorDao majorDao = new MajorDao();
	
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
			
			updateTeacher(updateStringList);
		    break;
		case "resetPassword":
			String id = request.getParameter("id");
			resetPassword(id);
			break;
		case "add":
			System.out.println("正在添加教师到数据库中！");
			String[] infoList = request.getParameterValues("info[]");
			addTeacher(infoList);
			break;
		case "import":
			System.out.println("正在上传文件");
			importTeacher(request);
			break;
		default:
			
			System.out.println("链接不存在！");
			
			break;
		}
		
		return jsonObjectOutput;
		
	}
	/**
	 * 显示教师的信息，将信息转换成JSON数据
	 * @param page 要显示的页数
	 */
	public void showTeacher(int page) {
		
		jsonObjectOutput = new JSONObject();
		
		// 每页的大小
		int pageSize = 5;
		// 得到对应页的数据
		List<Teacher> list = teacherDao.queryByPage(page, pageSize);
		// 得到要显示的总数量
		long count = teacherDao.queryCount();
		
		// 定义要返回的教师数据的JSON对象
		JSONArray jsonList = new JSONArray();
		
		/* 将对象转换成JSON数据 */
		for (Teacher teacher : list) {
			// 添加专业负责姓名
			Major major =  majorDao.queryByMID(teacher.getMid());
			// 得到专业负责人姓名
			String realname = teacherDao.queryByTea_id(major.getTea_id()).getRealname();
			
			// 把教师对象转换成Object数组
			List<Object> objectList = toObjectList(teacher, realname);
			// 对Object数组进行加工
//			objectList = produceTeacherOfShow(objectList);
			
			// 加入到要返回的教师数据的JSON对象中
			jsonList.add(objectList);
		}
		
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
	
	/**
	 * 删除教师功能
	 * @param list 要删除的教师的ID的数组
	 */
	public void deleteTeacher(String[] list) {
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
			
			if (delArray.size() == 0) {
				jsonObjectOutput.put("status", false);
			} else {
				jsonObjectOutput.put("status", true);
			}
			
			// 设置返回删除成功的教师的ID
			jsonObjectOutput.put("ids", delArray);
			
		}
		
	}
	/**
	 * 更改教师信息功能
	 * @param updateList 要更改的多个教师的数组信息
	 */
	public void updateTeacher(List<String[]> updateList) {
		jsonObjectOutput = new JSONObject();
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
			list = produceTeacherOfUpdateReverse(list);
			
			// 对象数组变成一个对象加入到要更新列表中
			updateTeacherList.add(toBeanUpdate(list));
		}
		// 对所有的对象进行更新，并将更新状态存入数组中
		for (int i = 0; i < updateTeacherList.size(); i++) {
			boolean b = teacherDao.updateAll(updateTeacherList.get(i));
			updateFlagList.add(b);
		}
		
		jsonObjectOutput.put("status", true);
		
	}
	
	/**
	 * 重置密码功能
	 * @param id 用户ID
	 */
	public void resetPassword(String id) {
		
		System.out.println("正在重置密码中。。。");
		
		jsonObjectOutput = new JSONObject();
		boolean b = teacherDao.resetPassword(Integer.parseInt(id));
		jsonObjectOutput.put("status", b);
		
		System.out.println("重置密码成功！");
		
	}
	
	/**
	 * 添加教师功能
	 * @param strings 要添加的教师数组
	 */
	public void addTeacher(String[] strings) {
		
		jsonObjectOutput = new JSONObject();
		
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		
//		for (Object object : list) {
//			System.out.println(object == null);
//			System.out.println(object.toString());
//		}
		
		// 进行数据检验
		Map<Integer, String> error = checkTeacher(list);
		System.out.println(error.size());
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
			Teacher teacher = toBeanAdd(list);
			// 保存对象
			boolean b = teacherDao.saveTeacher(teacher);
			
//			JSONArray array = new JSONArray();
//			array.add(teacher.getTea_id());
//			// 通过教师的负责人ID找到专业负责人的名称。
//			array.add("刘备");
			
			jsonObjectOutput.put("status", b);
//			jsonObjectOutput.put("info", array);
			
		}
	}
	/**
	 * TODO 导入教师文件功能，还有很多问题
	 * @param request 其中可以得到文件
	 */
	public void importTeacher(HttpServletRequest request) {
		
		jsonObjectOutput = new JSONObject();
		
		// 将上传的文件转换成一个对象列表
		List<List<Object>> teacherList = new ArrayList<>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			
			request.setCharacterEncoding("utf-8");
			
			List<FileItem> list = upload.parseRequest(new ServletRequestContext(request));
			// 读取每 一个文件
			if (list.get(0) != null) {
				FileItem item = list.get(0);
				String filename = item.getName();
				System.out.println("文件名为：" + filename);
				String extName = filename.substring(filename.lastIndexOf("."));
				System.out.println("后缀为：" + extName);
				InputStream inputStream = item.getInputStream();
				if (extName.equals(".xlsx")) {
					teacherList = importXlsxData(inputStream);
				} else if (extName.equals(".xls")) {
					teacherList = importXlsData(inputStream);
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
		
		System.out.println(teacherList.size());
		
		for (List<Object> teacher : teacherList) {
			// 返回专业名称转换成专业ID
			Object obj = teacher.get(3);
			obj = produceTeacherMajorReverse(obj);
			
			JSONArray array = new JSONArray();
			for (Object object : teacher) {
				array.add(object);
			}
			teacherArray.add(array);
		}
		
		jsonObjectOutput.put("status", true);
		
		jsonObjectOutput.put("infos", teacherArray);
		
		
		
	}
	
	/**
	 * 实现导入xlsx文件
	 * @param inputStream
	 * @return
	 */
	public List<List<Object>> importXlsxData(InputStream inputStream) {
		
		System.out.println("开始加载数据中。。。");
		
		List<List<Object>> teacherList = new ArrayList<>();
		
		try {
			
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
			// 目前只读取第一个Sheet的数据
			Sheet sheet = xssfWorkbook.getSheetAt(0);
			System.out.println("一共有" + sheet.getLastRowNum() + "条数据");
			for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum ++) {
//				System.out.println("正在加载第" + rowNum + "数据");
				Row row = sheet.getRow(rowNum);
				List<Object> list = new ArrayList<>();
				for (int i = 0; i < 10; i++) {
					Cell cell = row.getCell(i);
					if (i == 6) {
						list.add(cell.getNumericCellValue());
					} else {
						list.add(cell.getStringCellValue());
					}
//					System.out.println(i);
				}
//				System.out.println(list.size());
				
				teacherList.add(list);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return teacherList;
	}
	
	/**
	 * 实现导入xls文件
	 * @param inputStream
	 * @return
	 */
	public List<List<Object>> importXlsData(InputStream inputStream) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void exportTeacherProblems(HttpServletRequest request) {
		
		jsonObjectOutput = new JSONObject();
		
		HttpSession session = request.getSession();
		String error = null;
		Object actObject = session.getAttribute("act");
		
		if (actObject == null) {
			error = "没有登录！！无法导出课题。";
			System.out.println(error);
			return ;
		}
		
		Integer act = Integer.parseInt(String.valueOf(actObject));
		
		if (act != 2 || act != 4) {
			error = "登录身份不是教师或专业负责人，无法导出课题。";
			System.out.println(error);
			return ;
		}
		
		Teacher teacher = (Teacher) session.getAttribute("user");
		
		if (teacher == null) {
			error = "无法得到教师信息，不能导出课题。";
			System.out.println(error);
			return ;
		}
		
		// 得到当前教师的所有课题
		List<Problem> problemList = problemDao.queryByTea_id(teacher.getTea_id());
		
		
		
		
		
	}
	
	/**
	 * 得到要返回的JSON数据
	 * @return 返回JSON数据
	 */
	public JSONObject getJsonObject() {
		return jsonObjectOutput;
	}
	
	/**
	 * 在添加数据之前，对教师的信息进行检验
	 * @param list 
	 * @return 返回出错Map
	 */
	public Map<Integer, String> checkTeacher(List<Object> list) {
		Map<Integer, String> map = new HashMap<>();
		
		String NOT_NULL = "不能为空！";
		String USERNAME_ERROR = "用户名重复！";
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			// 对用户名进行检验
			if (i == 0) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
				if (teacherDao.queryByUserName(String.valueOf(object)) != null) {
					map.put(i, USERNAME_ERROR);
					continue;
				}
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
			// 职称
			if (i == 4) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 学位
			if (i == 5) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// QQ
			if (i == 6) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 手机
			if (i == 7) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 邮箱
			if (i == 8) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			// 备注可以为空
			if (i == 9) {
				
			}
			// 选题数量
			if (i == 10) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
		}
		
		return map;
	}
	
	/**
	 * 这个函数对教师的信息数组 中的某些信息进行加工，以实现显示教师功能
	 * @param list 传入一个包含教师信息的Object数组
	 * @return List<Object> 返回相同的数组
	 */
	public List<Object> produceTeacherOfShow(List<Object> list) {
		
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
//			if (i == 11) {
//				// 从数据库中通过救专业负责人ID找到对应的专业负责人的真实姓名
//				object = produceTeacherPID(object);
//			}
			
			realList.add(object);
		}
		
		return realList;
	}
	/**
	 * 在数据更改时，对教师数组中的一个或多个数据进行反向加工，之后才能变成对象
	 * @param list
	 * @return
	 */
	public List<Object> produceTeacherOfUpdateReverse(List<Object> list) {
		
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
//			if (i == 11) {
//				// 从数据库中找到专业负责人的真实姓名对应的ID
//				object= produceTeacherPIDReverse(object);
//			}
			
			realList.add(object);
		}
		
		return realList;
		
	}
	
	/**
	 * 将教师的专业ID加工成专业名称
	 * @param object
	 * @return
	 */
	public Object produceTeacherMajor(Object object) {
		return majorDao.queryByMID((int)object).getMajor();
	}
	
	/**
	 * 将教师的专业名称反向加工成专业ID
	 * @param object 专业名称
	 * @return 返回专业ID
	 */
	public Object produceTeacherMajorReverse(Object object) {
		return majorDao.queryByMajorName(String.valueOf(object)).getMajor();
	}
	
	/**
	 * 在更改教师时，将一个List<Object>列表转换成一个教师对象
	 * @param list 要转换的列表数据
	 * @return 一个教师对象
	 */
	public Teacher toBeanUpdate(List<Object> list) {
		
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
		// 专业负责人在更新时不能改变
		teacher.setNumber(Integer.parseInt(String.valueOf(list.get(12))));
		return teacher;
	}
	
	/**
	 * 在添加教师时将一个Object数组转换成对象
	 * @param list 要转换的列表数据
	 * @return 
	 */
	public Teacher toBeanAdd(List<Object> list) {
		
		Teacher teacher = new Teacher();
		teacher.setUsername(String.valueOf(list.get(0)));
		teacher.setRealname(String.valueOf(list.get(1)));
		teacher.setSex(Integer.parseInt(String.valueOf(list.get(2))));
//		System.out.println("sex = " + teacher.getSex());
		teacher.setMid(Integer.parseInt(String.valueOf(list.get(3))));
		teacher.setTitle(String.valueOf(list.get(4)));
		teacher.setDegree(String.valueOf(list.get(5)));
		teacher.setQq(String.valueOf(list.get(6)));
		teacher.setPhone(String.valueOf(list.get(7)));
		teacher.setEmail(String.valueOf(list.get(8)));
		teacher.setRemarks(String.valueOf(list.get(9)));
		
		teacher.setNumber(Integer.parseInt(String.valueOf(list.get(10))));
		teacher.setPassword("123456");
		return teacher;
	}
	
	/**
	 * 
	 * @param teacher
	 * @return
	 */
	public List<Object> toObjectList(Teacher teacher, String realname) {
		List<Object> list = new ArrayList<>();
		list.add(teacher.getTea_id());
		list.add(teacher.getUsername());
		list.add(teacher.getRealname());
		list.add(teacher.getSex());
		list.add(teacher.getMid());
		list.add(teacher.getTitle());
		list.add(teacher.getDegree());
		list.add(teacher.getQq());
		list.add(teacher.getPhone());
		list.add(teacher.getEmail());
		list.add(teacher.getRemarks());
		list.add(realname);
		list.add(teacher.getNumber());
		return list;
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
			list = produceTeacherOfShow(list);
			
			
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
