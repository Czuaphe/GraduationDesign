package com.graduation.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.graduation.dao.MajorDao;
import com.graduation.dao.ProblemDao;
import com.graduation.dao.StudentDao;
import com.graduation.entity.Major;
import com.graduation.entity.Problem;

public class ProblemService {
	
	private ProblemDao problemDao = new ProblemDao();
	private StudentDao studentDao = new StudentDao();
	private MajorDao majorDao = new MajorDao();
	
	private List<String> pathList = new ArrayList<>();

	private HttpServletRequest request = null;
	
	private JSONObject jsonObjectOutput = new JSONObject();
	
	public ProblemService() {}
	
	public ProblemService(List<String> pathList, HttpServletRequest request) {
		this.pathList = pathList;
		this.request = request;
	}
	
	public JSONObject redirectToPath() {
		
		jsonObjectOutput = new JSONObject();
		
		switch (pathList.get(1)) {
		case "add":
			String[] info = request.getParameterValues("info[]");
			addProblem(info);
			break;
		case "show":
			int currentPage = Integer.parseInt(request.getParameter("currentPage"));
			showProblem(currentPage);
			break;
		case "dels":
			String[] delList = request.getParameterValues("ids[]");
			deleteProblem(delList);
			break;
		case "details":
			int problem_id = Integer.parseInt(request.getParameter("pro_id"));
			detailsProblem(problem_id);
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
			updateProblem(updateStringList);
			
			break;
		default:
			System.out.println("二级路由无法解析！！！");
			break;
		}
		
		return jsonObjectOutput;
		
	}
	/**
	 * 添加课题
	 * @param info 课题的字符串数组信息
	 */
	public void addProblem(String[] info) {
		
		jsonObjectOutput = new JSONObject();
		

		List<String> list = new ArrayList<>();
		for (String string : info) {
			list.add(string);
		}
		
		// 检测数据的正确性
		String error = checkProblem(list);
		
		if (error != null) {
			
			jsonObjectOutput.put("status", false);
			jsonObjectOutput.put("info", error);
			
		} else {
			
			// 数据转换成对象
			Problem problem = toBeanAdd(list);
			
			// 保存到数据库中
			boolean b = problemDao.save(problem);
			
			// 将返回的结果保存到JSON中
			jsonObjectOutput.put("status", b);
			if (!b) {
				jsonObjectOutput.put("error", "未知原因保存失败！");
			}
			
		}
		
		
		
	}
	
	/**
	 * 显示当前页的数据
	 * @param page 页码
	 */
	public void showProblem(int page) {
		
		jsonObjectOutput = new JSONObject();
		
		boolean flag = false;
		
		int pageSize = 5;
		// 得到当前页的所有对象
		List<Problem> list = problemDao.queryByPage(page, pageSize);
		
		if (list != null) {
			flag = true;
		}
		
		// 课题的总数量
		long count = problemDao.queryCount();
		
		JSONArray jsonList = new JSONArray();
		// 将对象转换成 JSON 数据
		for (Problem problem : list) {
			
			List<Object> objectList = toObjectShow(problem);
			
			jsonList.add(objectList);
		}
		// 返回所有专业
		jsonObjectOutput.put("majors", getAllMajors());
		
		// 设置要返回的课题数据
		jsonObjectOutput.put("data", jsonList);
		// 设置当前页
		jsonObjectOutput.put("currentPage", page);
		// 设置返回状态
		jsonObjectOutput.put("status", flag);
		// 设置总页数
		jsonObjectOutput.put("totalPage", (count - 1) / pageSize + 1 );

	}
	/**
	 * 删除课题
	 * @param list 要删除的课题的ID的列表
	 */
	public void deleteProblem(String[] list) {
		jsonObjectOutput = new JSONObject();
		
		if (list.length != 0) {
			
			List<Integer> delList = new ArrayList<>();
			
			for (String string : list) {
				delList.add(Integer.parseInt(string));
			}
			
			List<Boolean> delFlagList = new ArrayList<>();
			
			for (Integer problem_id : delList) {
				boolean flag = problemDao.remove(problem_id);
				System.out.println("delete id: " + problem_id + ",result: " + flag);
				delFlagList.add(flag);
			}
			
			JSONArray delJsonArray = new JSONArray();
			
			for (int i = 0; i < delFlagList.size(); i++) {
				if (delFlagList.get(i)) {
					delJsonArray.add(delList.get(i));
				}
				
			}
			
			jsonObjectOutput.put("status", true);
			// 设置返回删除成功的课题的ID
			jsonObjectOutput.put("ids", delJsonArray);
			
		}
		
	}
	
	/**
	 * 显示某一课题的详情
	 * @param problem_id 课题的ID
	 */
	public void detailsProblem(int problem_id) {
		
		jsonObjectOutput = new JSONObject();
		
		Problem problem = problemDao.queryByProblem_id(problem_id);
		
		if (problem == null) {
			jsonObjectOutput.put("status", false);
		} else {
			
			List<Object> objectList = toObjectDetails(problem);
			
			JSONArray objectArray = new JSONArray();
			
			for (Object object : objectList) {
				objectArray.add(object);
			}
			
			jsonObjectOutput.put("status", true);
			
			jsonObjectOutput.put("info", objectArray);
		}
		
	}
	
	/**
	 * 更新课题信息
	 * @param updateList 要更新的课题数据列表
	 */
	public void updateProblem(List<String[]> updateList) {
		
		jsonObjectOutput = new JSONObject();
		// 待更新的课题对象列表
		List<Problem> updateProblemList = new ArrayList<>();
		
		// 将所有要更新的字符串数组转换成对象，放入上面的待更新对象列表中
		for (String[] strings : updateList) {
			List<Object> list = new ArrayList<>();
			for (String string : strings) {
				list.add(string);
			}
			updateProblemList.add(toBeanUpdate(list));
		}
		
		// 更新列表中的所有课题
		boolean flag = true;
		int problem_id = 0;  // 记录出错的课题的ID
		for (int i = 0; i < updateProblemList.size(); i++) {
			System.out.println("Update Student :" + updateProblemList.get(i).toString());
			flag = problemDao.updateAll(updateProblemList.get(i));
			if (!flag) {
				problem_id = updateProblemList.get(i).getProblem_id();
				break;
			}
		}
		
		jsonObjectOutput.put("status", flag);
		
		if (!flag) {
			String info = "未知原因，ID为" + problem_id + "的课题更新失败";
			jsonObjectOutput.put("info", info);
		}
	
	}
	
	/**
	 * 对要添加的课题的数据进行检测
	 * @param list 要检测的数据
	 * @return 返回出错的字符串，没有错则返回空
	 */
	public String checkProblem(List<String> list) {
		
		String error = null;
		
		for (int i = 0; i < list.size(); i++) {
			
			// 检测所有数据的非空
			if (list.get(i) == null || list.get(i).equals("")) {
				error = "数据不能为空！";
				break;
			}
			
			// 选题方式为指定学生，检测此学生是否已经被指定过
			if (i == 6 && !list.get(i).equals("0")) {
				List<Problem> wayList = problemDao.queryByWay(Integer.parseInt(list.get(i)));
				if (wayList != null && wayList.size() != 0) {
					error = "该学生已经被指定课题了！";
					break;
				}
			}
		}
		
		return error;
		
	}
	
	/**
	 * 在添加课题时，将字符串数组转换成对象
	 * @param list 字符串数组
	 * @return 返回一个课题对象
	 */
	public Problem toBeanAdd(List<String> list) {
		Problem problem = new Problem();
		
		problem.setName(list.get(0));
		problem.setMid(Integer.parseInt(list.get(1)));
		problem.setIs_new(Integer.parseInt(list.get(2)));
		problem.setType(Integer.parseInt(list.get(3)));
		problem.setSource(Integer.parseInt(list.get(4)));
		problem.setNature(Integer.parseInt(list.get(5)));
		problem.setWay(Integer.parseInt(list.get(6)));
		problem.setIntroduction(list.get(7));
		problem.setRequirement(list.get(8));
		
		return problem;
		
	}
	
	public Problem toBeanUpdate(List<Object> list) {
		Problem problem = new Problem();
		
		problem.setProblem_id(Integer.parseInt(String.valueOf(list.get(0))));
		problem.setName(String.valueOf(list.get(1)));
		problem.setMid(Integer.parseInt(String.valueOf(list.get(2))));
		problem.setIs_new(Integer.parseInt(String.valueOf(list.get(3))));
		problem.setType(Integer.parseInt(String.valueOf(list.get(4))));
		problem.setSource(Integer.parseInt(String.valueOf(list.get(5))));
		problem.setResearch_name(String.valueOf(list.get(6)));
		problem.setNature(Integer.parseInt(String.valueOf(list.get(7))));
		problem.setWay(Integer.parseInt(String.valueOf(list.get(8))));
		problem.setIntroduction(String.valueOf(list.get(9)));
		problem.setRequirement(String.valueOf(list.get(10)));
		
		return problem;
	}
	/**
	 * 显示课题时将对象转换成 Object 数组
	 * @param problem 要转换的对象
	 * @return 返回一个 Object 列表
	 */
	public List<Object> toObjectShow(Problem problem) {
		List<Object> list = new ArrayList<>();
		
		list.add(problem.getProblem_id());
		list.add(problem.getName());
		list.add(problem.getMid());
		list.add(problem.getIs_new());
		list.add(problem.getType());
		list.add(problem.getSource());
		list.add(problem.getNature());
		list.add(problem.getWay());
		list.add(problem.getStatus());
		
		return list;
	}
	
	/**
	 * 显示课题详情时将对象转换成 Object 数组
	 * @param problem 要转换的对象
	 * @return 返回一个 Object 列表
	 */
	public List<Object> toObjectDetails(Problem problem) {
		List<Object> list = new ArrayList<>();
		
		list.add(problem.getProblem_id());
		list.add(problem.getName());
		list.add(problem.getMid());
		list.add(problem.getIs_new());
		list.add(problem.getType());
		list.add(problem.getSource());
		list.add(problem.getResearch_name());
		list.add(problem.getNature());
		list.add(problem.getWay());
		list.add(problem.getIntroduction());
		list.add(problem.getRequirement());
		
		// 添加指定学生的姓名
		int way = problem.getWay();
		if (way == 0) {
			list.add(null);
		} else {
			list.add(studentDao.queryByStu_id(way).getRealname());
		}
		
		list.add(problem.getAudit_opinion());
		
		return list;
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
	
	
}
