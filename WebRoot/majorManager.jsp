<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-6 col-md-offset-3">
        <h1>专业及负责人管理</h1>
        <hr/>
        <h4 class="text-danger text-center" id="info"></h4>
        <table id="majorTable"></table>
    </div>
</div>
<script>
    $(document).ready(function(){
        $.get("/GraduationDesign/major/show", function(response){
            if(response.status == true)
                $("#majorTable").dynamicTables({
                'title' : ['编号', '专业名称', '专业负责人账号','专业负责人姓名', '专业人数'],
                'data' : response.data,
                'delsURL':'/GraduationDesign/major/dels',
                'addURL':'/GraduationDesign/major/add',
                'saveURL':'/GraduationDesign/major/update',
                'typeConfig' : [
                    {'edit':  false},
                    {'type':  'text'},
                    {'type':  'text'},
                    {'edit':  false},
                    {'edit':  false},
                ]
            })
            else {
                $("#info").html("获取失败");
            }
            window.setTimeout('if($("#info").html() == "获取中请稍后") $("#info").html("请求超时")', 2000);


        })
    })
</script>
<%@ include file="footer.jsp"%>