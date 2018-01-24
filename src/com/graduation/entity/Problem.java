package com.graduation.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Problem implements Serializable {
	
	private int problem_id;	// 课题ID
	private String name;	// 课题名称
	private int mid;		// 所属专业ID
	private int is_new;		// 是否为新课题
	private int type;		// 课题类型
	private int source;		// 课题来源
	private String research_name;	// 科研项目名称
	private int nature;		// 课题性质
	private int way;		// 选题方式，0表示盲选，其它数字则表达指定学生的ID
	private String introduction;	// 课题简介
	private String requirement;		// 课题要求
	private int tea_id;		// 教师ID
	private Timestamp problem_time;	// 出题时间
	private int status;		// 课题状态
	private Timestamp audit_time;	// 审核时间
	private String audit_opinion;	// 审核意见
	private int is_outer;	// 是否为校外选题
	
	public int getProblem_id() {
		return problem_id;
	}
	public void setProblem_id(int problem_id) {
		this.problem_id = problem_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getIs_new() {
		return is_new;
	}
	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getResearch_name() {
		return research_name;
	}
	public void setResearch_name(String research_name) {
		this.research_name = research_name;
	}
	public int getNature() {
		return nature;
	}
	public void setNature(int nature) {
		this.nature = nature;
	}
	public int getWay() {
		return way;
	}
	public void setWay(int way) {
		this.way = way;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public int getTea_id() {
		return tea_id;
	}
	public void setTea_id(int tea_id) {
		this.tea_id = tea_id;
	}
	public Timestamp getProblem_time() {
		return problem_time;
	}
	public void setProblem_time(Timestamp problem_time) {
		this.problem_time = problem_time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(Timestamp audit_time) {
		this.audit_time = audit_time;
	}
	public String getAudit_opinion() {
		return audit_opinion;
	}
	public void setAudit_opinion(String audit_opinion) {
		this.audit_opinion = audit_opinion;
	}
	
	public int getIs_outer() {
		return is_outer;
	}
	public void setIs_outer(int is_outer) {
		this.is_outer = is_outer;
	}
	
	@Override
	public String toString() {
		return "Problem [problem_id=" + problem_id + ", name=" + name
				+ ", mid=" + mid + ", is_new=" + is_new + ", type=" + type
				+ ", source=" + source + ", research_name=" + research_name
				+ ", nature=" + nature + ", way=" + way + ", introduction="
				+ introduction + ", requirement=" + requirement + ", tea_id="
				+ tea_id + ", problem_time=" + problem_time + ", status="
				+ status + ", audit_time=" + audit_time + ", audit_opinion="
				+ audit_opinion + ", is_outer=" + is_outer + "]";
	}
	
	
	
}
