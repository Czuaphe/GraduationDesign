package com.graduation.entity;

public class Admin {
	
	private int rid;
	private String username;
	private String password;
	
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
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
	
	@Override
	public String toString() {
		return "Admin [rid=" + rid + ", username=" + username + ", password="
				+ password + "]";
	}
	
	
}
