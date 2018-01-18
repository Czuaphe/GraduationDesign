package com.graduation.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class CommonService {
	
private List<String> pathList = new ArrayList<>();
	
	private HttpServletRequest request = null;
	
	private JSONObject jsonObjectOutput = new JSONObject();
	
	public CommonService() {}
	
	public CommonService(List<String> pathList, HttpServletRequest request) {
		this.pathList = pathList;
		this.request = request;
	}
	
	public JSONObject redirectToPath() {
		
		switch (pathList.get(0)) {
		case "login":
			int act = Integer.parseInt(request.getParameter("act"));
			login();
			break;
		case "logout":
			
			break;
			
		default:
			break;
		}
		
		return jsonObjectOutput;
		
	}
	
	public void login() {
		
	}
	
}
