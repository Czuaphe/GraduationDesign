<%@page import="com.graduation.dao.NoticeDao"%>
<%@page import="com.graduation.entity.Notice"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>

    <div class="col-md-8">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h1>公告</h1>
            </div>
            <div class="panel-body" id="notice">
                	<% 	List<Notice> noticeList = null;
                		NoticeDao noticeDao = new NoticeDao();
                		if (act == 1) { 
                			noticeList = noticeDao.queryStudentCanSee();
                		} else if(act == 2) {
                			noticeList = noticeDao.queryTeacherCanSee();
                		} else if(act == 4) {
                			noticeList = noticeDao.queryMajorTeacherCanSee();
                		} else if(act == 3) {
                			noticeList = noticeDao.queryAllCanSee();
                		}
                		if (noticeList.size() == 0) {
                	%>
                	<blockquote><p>空</p></blockquote>
                	
                	<% 
                		} else {
                		
                			for(Notice notice: noticeList) {
                	%>
                    <blockquote><p><%=notice.getContent() %></p></blockquote>
                    <%		} 
                    	}
                    %>
            </div>
        </div>
    </div>
</div>
<%@ include file="footer.jsp"%>