<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-8 col-md-offset-1">
        <h1>选题结果</h1>
        <hr/>
            <div class="panel panel-default" id="problem">
                <div class="panel-heading">
                    <h3 class="text-center" id="name"></h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-6">
                            <h5>适合专业：<small id="major"></small></h5>
                            <h5>课题类型：<small id="type"></small></h5>
                            <h5>新题目：<small id="newPro"></small></h5>
                            <h5>选题方式：<small id="selectType"></small></h5>
                        </div>
                        <div class="col-sm-6">
                            <h5>课题来源：<small id="from"></small></h5>
                            <h5>课题性质：<small id="attr"></small></h5>
                            <h5>教师姓名：<small id="teacherName"></small></h5>
                            <h5>联系方式：<small id="teacherPhone"></small></h5>
                        </div>
                    </div>
                    <hr/>
                    <h5>课题简介:</h5>
                    <textarea id="description" rows="5" class="form-control" disabled></textarea>
                    <h5>课题要求</h5>
                    <textarea id="require" rows="5" class="form-control" disabled></textarea>

                </div>
            </div>
            
            <div class="panel panel-danger hidden" id="teacher">
            	<div class="panel-heading">
            		<h3 class="text-center">教师详细信息</h3>
            	</div>
            	<div class="panel-body">
            		<div class="row">
            			<div class="col-sm-6">
            				<h5>教师职称：<small id="title">ggg</small></h5>
            			</div>
            			<div class="col-sm-6">
            				<h5>教师学位：<small id="degree">ggg</small></h5>
            			</div>
            		</div>
            		<hr />
            		<h5>个人经历：</h5>
            		<textarea id="experience" rows="5" class="form-control" disabled>ggg</textarea>
            	</div>
            </div>
            <div class="row" style="margin-bottom: 50px;">
            	<div class="col-sm-12">
            		<button class="btn btn-danger hidden" id="unselect">退选</button>
            	</div>
            </div>
            
        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        $.get("/GraduationDesign/problem/selected/student", function (response) {
            if(response.status == true) {
            
                $("#problem").children().eq(0).removeAttr("hidden");
                $("#name").html(response.info[1]);
                $("#major").html(response.info[2]);
                $("#type").html(response.info[4]);
                $("#attr").html(response.info[6]);
                $("#from").html(response.info[5]);
                $("#newPro").html(response.info[3]);
                $("#teacherName").html(response.info[10]);
                $("#teacherPhone").html(response.info[11]);
                $("#selectType").html(response.info[7]);
                $("#description").html(response.info[9]);
                $("#require").html(response.info[8]);
                // 显示退选按钮
                $("#unselect").removeClass("hidden");
            }
            if(response.check == true) {
            	$("#teacher").removeClass("hidden");
            	$("#title").html(response.teacher[0]);
            	$("#degree").html(response.teacher[1]);
            	$("#experience").html(response.teacher[2]);
            }
        });
        
        $("#unselect").click(function() {
			$.get("/GraduationDesign/student/unselect", function(response) {
				if (response.status == true) {
					alert("退选成功");
				} else {
					alert(response.info);
				}
			})
		})
        
    })
</script>
<%@ include file="footer.jsp"%>