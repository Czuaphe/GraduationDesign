package com.graduation.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notice implements Serializable {
	
	private int notice_id;		// 公告ID
	
	private String content;		// 公告内容
	
	private Timestamp start;	// 开始时间
	
	private Timestamp end;		// 结束时间
	
	private int p1;				// 学生可见性
	
	private int p2;				// 教师可见性
	
	private int p4;				// 专业负责人可见性

	public int getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(int notice_id) {
		this.notice_id = notice_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getStart() {
		return start;
	}

	public void setStart(Timestamp start) {
		this.start = start;
	}

	public Timestamp getEnd() {
		return end;
	}

	public void setEnd(Timestamp end) {
		this.end = end;
	}

	public int getP1() {
		return p1;
	}

	public void setP1(int p1) {
		this.p1 = p1;
	}

	public int getP2() {
		return p2;
	}

	public void setP2(int p2) {
		this.p2 = p2;
	}

	public int getP4() {
		return p4;
	}

	public void setP4(int p4) {
		this.p4 = p4;
	}

	@Override
	public String toString() {
		return "Notice [notice_id=" + notice_id + ", content=" + content
				+ ", start=" + start + ", end=" + end + ", p1=" + p1 + ", p2="
				+ p2 + ", p4=" + p4 + "]";
	}
	
	
}
