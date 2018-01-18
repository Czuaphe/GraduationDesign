package com.graduation.entity;

import java.io.Serializable;

public class Major implements Serializable {
	
	private int mid;
	private String major;
	private int tea_id;
	
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
	
	@Override
	public String toString() {
		return "Major [mid=" + mid + ", major=" + major + ", tea_id=" + tea_id
				+ "]";
	}
	
	
	
}
