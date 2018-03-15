package com.graduation.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Major implements Serializable {
	
	private int mid;
	private String major;
	private int tea_id;
	private Timestamp problem_start;
	private Timestamp problem_end;
	private Timestamp verify_start;
	private Timestamp verify_end;
	private Timestamp select_start;
	private Timestamp select_end;
	
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public int getTea_id() {
		return tea_id;
	}
	public void setTea_id(int tea_id) {
		this.tea_id = tea_id;
	}
	
	public Timestamp getProblem_start() {
		return problem_start;
	}
	public void setProblem_start(Timestamp problem_start) {
		this.problem_start = problem_start;
	}
	public Timestamp getProblem_end() {
		return problem_end;
	}
	public void setProblem_end(Timestamp problem_end) {
		this.problem_end = problem_end;
	}
	public Timestamp getVerify_start() {
		return verify_start;
	}
	public void setVerify_start(Timestamp verify_start) {
		this.verify_start = verify_start;
	}
	public Timestamp getVerify_end() {
		return verify_end;
	}
	public void setVerify_end(Timestamp verify_end) {
		this.verify_end = verify_end;
	}
	public Timestamp getSelect_start() {
		return select_start;
	}
	public void setSelect_start(Timestamp select_start) {
		this.select_start = select_start;
	}
	public Timestamp getSelect_end() {
		return select_end;
	}
	public void setSelect_end(Timestamp select_end) {
		this.select_end = select_end;
	}
	@Override
	public String toString() {
		return "Major [mid=" + mid + ", major=" + major + ", tea_id=" + tea_id
				+ "]";
	}
	
	
	
}
