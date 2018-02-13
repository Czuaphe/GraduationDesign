package com.graduation.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Selected implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7542975437582810884L;
	private int selected_id;	// 选题ID
	private int problem_id;		// 课题ID
	private int stu_id;			// 学生ID
	private Timestamp time;		// 选题时间
	
	public int getSelected_id() {
		return selected_id;
	}
	public void setSelected_id(int selected_id) {
		this.selected_id = selected_id;
	}
	public int getProblem_id() {
		return problem_id;
	}
	public void setProblem_id(int problem_id) {
		this.problem_id = problem_id;
	}
	public int getStu_id() {
		return stu_id;
	}
	public void setStu_id(int stu_id) {
		this.stu_id = stu_id;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "Selected [selected_id=" + selected_id + ", problem_id="
				+ problem_id + ", stu_id=" + stu_id + ", time=" + time + "]";
	}
	
	
	
}
