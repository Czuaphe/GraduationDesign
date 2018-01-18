package com.graduation.service;

import static org.hamcrest.CoreMatchers.nullValue;

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
import com.graduation.dao.TeacherDao;
import com.graduation.entity.Major;
import com.graduation.entity.Student;
import com.graduation.entity.Teacher;

public class MajorService {
	
	private StudentDao studentDao = new StudentDao();
	private MajorDao majorDao = new MajorDao();
	private TeacherDao teacherDao = new TeacherDao();
	
	private List<String> pathList = new ArrayList<>();
	
	private HttpServletRequest request = null;
	
	private JSONObject jsonObjectOutput = new JSONObject();
	
	public MajorService() {}
	
	public MajorService(List<String> pathList, HttpServletRequest request) {
		this.pathList = pathList;
		this.request = request;
	}
	
	public JSONObject redirectToPath() {
		
		switch (pathList.get(1)) {
		case "show":
			System.out.println("正在显示专业中。。。");
			showMajor();
			break;
		case "dels":
			String[] delList = request.getParameterValues("ids[]");
			deleteMajor(delList);
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
			updateMajor(updateStringList);
			break;
		case "add":
			System.out.println("正在添加教师到数据库中！");
			String[] infoList = request.getParameterValues("info[]");
			addMajor(infoList);
			break;
		default:
			break;
		}
		
		return jsonObjectOutput;
	}


	public void showMajor() {
		
		jsonObjectOutput = new JSONObject();
		
		// 得到所有的数据
		List<Major> list = majorDao.getAllMajor();
		
		// 定义要返回的教师数据的JSON对象
		JSONArray jsonList = new JSONArray();
		
		/* 将对象转换成JSON数据 */
		for (Major major : list) {
			
			// 找到专业负责人的姓名和用户名
			Teacher teacher = teacherDao.queryByTea_id(major.getTea_id());
			String realname = teacher.getRealname();
			String username = teacher.getUsername();
			// 得到本专业的人数
			Long num = teacherDao.queryMajorCount(major.getMid());
			
			// 把教师对象和其它数据一起转换成Object数组
			List<Object> objectList = toObjectList(major, username, realname, num);
			
			// 加入到要返回的教师数据的JSON对象中
			jsonList.add(objectList);
		}
		
		// 设置要返回的教师数据
		jsonObjectOutput.put("data", jsonList);
		// 设置返回状态
		jsonObjectOutput.put("status", true);
		
		
	}
	
	public void deleteMajor(String[] list) {
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
			// 存放每一个专业的删除结果
			List<Boolean> delFlagList = new ArrayList<>(delList.size());
			
			for (int i = 0; i < delList.size(); i++) {
				int mid = Integer.parseInt(delList.get(i));
				boolean flag = majorDao.remove(mid);
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
	
	public void updateMajor(List<String[]> updateList) {
		jsonObjectOutput = new JSONObject();
		
		boolean flag = true;
		
		for (String[] strings : updateList) {
			
			for (String string : strings) {
				System.out.println(string);
			}
			
			// 得到专业ID
			int mid = Integer.parseInt(strings[0]);
			// 得到新的专业名称
			String major = String.valueOf(strings[1]);
			// 得到新的教师用户名
			String username = String.valueOf(strings[2]);
			
			// 如果教师不存在就更新失败
			if (teacherDao.queryByUserName(username) == null) {
				flag = false;
				break;
			}
			
			// 构造成一个专业
			Major majorObject = new Major();
			majorObject.setMid(mid);
			majorObject.setMajor(major);
			int tea_id = teacherDao.queryByUserName(username).getTea_id();
			majorObject.setTea_id(tea_id);
			
			// 更新整个专业
			majorDao.update(majorObject);
			
		}
		
		jsonObjectOutput.put("status", flag);
	}
	
	public void addMajor(String[] infoList) {
		
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
		Map<Integer, String> error = checkMajor(list);
		System.out.println("Add Major Errors : " + error.size());
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
			// 将教师用户名转换成教师ID
			list = produceMajorOfAddReverse(list);
			
			// 数据检验成功，进行下一步
			Major major = toBeanAdd(list);
			
			boolean b = majorDao.save(major);
			
			jsonObjectOutput.put("status", b);
			
		}
	}
	
	public Map<Integer, String> checkMajor(List<Object> list) {
		Map<Integer, String> map = new HashMap<>();
		String NOT_NULL = "不能为空";
		String CHONGFU = "专业重复！";
		String TEACHER_NULL = "教师不存在";
		for(int i = 0; i < list.size(); i ++ ) {
			Object object = list.get(i);
			// 专业名称
			if (i == 0) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
				if (majorDao.queryByMajorName(String.valueOf(object)) != null) {
					map.put(i, CHONGFU);
					continue;
				}
			}
			// 教师用户名
			if (i == 1) {
				if (object == null || object.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
				if (teacherDao.queryByUserName(String.valueOf(object)) == null) {
					map.put(i, TEACHER_NULL);
					continue;
				}
			}
		}
		
		return map;
	}
	
	public List<Object> toObjectList(Major major, String username, String realname, long num) {
		
		List<Object> list = new ArrayList<>();
		
		list.add(major.getMid());
		list.add(major.getMajor());
		list.add(username);
		list.add(realname);
		list.add(num);
		
		return list;
	}
	
	public Major toBeanAdd(List<Object> list) {
		Major major = new Major();
		
		major.setMajor(String.valueOf(list.get(0)));
		major.setTea_id(Integer.parseInt(String.valueOf(list.get(1))));
		
		return major;
		
	}
	/**
	 * 
	 * @param list
	 * @return
	 */
	public List<Object> produceMajorOfAddReverse(List<Object> list) {
		List<Object> list2 = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			// 教师用户名转换成教师ID
			if (i == 1) {
				object = teacherDao.queryByUserName(String.valueOf(object)).getTea_id();
			}
			
			list2.add(object);
		}
		
		return list2;
	}
	
	
}
