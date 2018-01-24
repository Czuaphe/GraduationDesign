package com.graduation.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.graduation.dao.ProblemDao;

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
		case "show":
			showProblem();
			break;
			
		default:
			break;
		}
		
		return jsonObjectOutput;
		
	}
	
	public void showProblem() {
		
	}
	
}
