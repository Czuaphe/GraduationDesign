package com.graduation.entity;

import java.io.Serializable;

public class Student implements Serializable{
	
	private int stu_id;			// 学生ID
	private String username;	// 用户名
	private String password;	// 密码
	private String realname;	// 姓名
	private int sex;			// 0表示男，1表示女 
	private String qq;			// QQ
	private int mid;			// 专业ID
	private String email;		// 邮箱
	private String phone;		// 手机
	private String remarks;		// 备注
	
	public int getStu_id() {
		return stu_id;
	}
	public void setStu_id(int stu_id) {
		this.stu_id = stu_id;
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
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString() {
		return "Student [stu_id=" + stu_id + ", username=" + username
				+ ", password=" + password + ", realname=" + realname
				+ ", sex=" + sex + ", qq=" + qq + ", mid=" + mid + ", email="
				+ email + ", phone=" + phone + ", remarks=" + remarks + "]";
	}
	
	
	
}
