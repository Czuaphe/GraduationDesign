package com.graduation.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Teacher implements Serializable {
	
	private int tea_id;
	private String username;
	private String password;
	private String realname;
	private int sex;
	private int mid;		// 专业ID
	private int number;		// 选题数量
	private String title;	// 职称
	private String degree;	// 学位
	private String qq;
	private String phone;
	private String email;
	private String remarks;	// 备注
	private String experience; // 个人经历
	private int show4stu;	// 是否向学生显示个人信息
	
	public int getTea_id() {
		return tea_id;
	}
	public void setTea_id(int tea_id) {
		this.tea_id = tea_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public int getShow4stu() {
		return show4stu;
	}
	public void setShow4stu(int show4stu) {
		this.show4stu = show4stu;
	}
	@Override
	public String toString() {
		return "Teacher [tea_id=" + tea_id + ", username=" + username
				+ ", password=" + password + ", realname=" + realname
				+ ", sex=" + sex + ", mid=" + mid + ", number=" + number
				+ ", title=" + title + ", degree=" + degree + ", qq=" + qq
				+ ", phone=" + phone + ", email=" + email + ", remarks="
				+ remarks + ", experience=" + experience + ", show4stu="
				+ show4stu + "]";
	}
	
	
	
}
