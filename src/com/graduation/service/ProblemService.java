package com.graduation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.graduation.dao.ProblemDao;
import com.graduation.entity.Problem;

public class ProblemService {
	
	private ProblemDao problemDao = new ProblemDao();
	
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
			
		default:
			break;
		}
		
		return jsonObjectOutput;
		
	}
	
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
	 * 对课题的数据的检测
	 * @param list
	 * @return
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
	
	
}
