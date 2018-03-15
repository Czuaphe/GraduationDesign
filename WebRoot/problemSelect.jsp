<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-8 col-md-offset-1">
        <h1>选题结果</h1>
        <hr/>
        <table id="selectInfo">
        </table>
    </div>
</div>
<script>
    $(document).ready(function(){
        $.get("/GraduationDesign/problem/selected?currentPage=1", function (response) {
            $("#selectInfo").dynamicTables({
                'title' : ['编号', '学生姓名', '学生学号', '选题时间', '课题名称', '专业', '教师账号', '教师姓名', '选题方式'],
                'data' : response.data,
                'delsURL': '/GraduationDesign/problem/selected/dels',
                'noAdd' : true,
                'noSave' : true,
                'typeConfig' : [
                    {"edit": false},
                    {"edit": false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit": false},
                    {"edit": false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit" : false},
                ],
            });
        })
    })
</script>
<%@ include file="footer.jsp"%>
