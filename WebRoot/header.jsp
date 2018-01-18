<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>毕业设计选题系统</title>
    <link href="./css/bootstrap.min.css" rel="stylesheet">
    <link href="./css/bootstrap-datepicker.min.css" rel="stylesheet">
    <script src="./js/jquery-3.2.1.min.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    <script src="./js/bootstrap-datepicker.min.js"></script>
    <script src="./js/dynamicTables.js"></script>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">毕业设计选题系统</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"><a href="index.html">首页</a></li>
            	<%
            		int act = Integer.parseInt(String.valueOf(session.getAttribute("act")));
            		if ( act == 3) {
            	 %>
                <li><a href="teacherInfoManager.html">教师信息管理</a></li>
                <li><a href="studentInfoManager.html">学生信息管理</a></li>
                <li><a href="majorManager.html">学科信息管理</a></li>
                <li><a href="#">选题系统配置</a></li>
                <li><a href="#">导出选题信息</a></li>
                <li><a href="#">公告编辑</a></li>
            	<%
            		} else if( act == 2) {
            	 %>
                <li><a href="problemManager.html">题目管理</a></li>
                <li><a href="problemSelect.html">查看选题结果</a></li>
            	<%
            		} else if(act == 1){
            	 %>
                <li><a href="#">查看选题结果</a></li>
                <li><a href="#">题目浏览</a></li>
                <%
            	 	} else {
            	  %>
                <li><a href="problemManager.html">题目管理</a></li>
                <li><a href="problemSelect.html">查看选题结果</a></li>
                <li><a href="problemAnalysis">题目审核</a></li>
                <%
            	 	}
            	  %>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">个人中心</a></li>
                <li><a href="login.html">退出登录</a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>