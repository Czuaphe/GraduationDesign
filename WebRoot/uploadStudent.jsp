<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-6 col-md-offset-1">
        <h1>导入学生信息</h1>
        <form id="upload" class="form-horizontal" role="form" method="post" action="" enctype="multipart/form-data">
            <label>请选择Excel文件：<input class="form-control" type="file" name="file" id="uploadFile" /></label>
        </form>
        <hr/>
        <table id="teacherInfo"></table>
    </div>
</div>
<script>
    $(document).ready(function(){
        

        $("#uploadFile").AjaxFileUpload({
           'action' : '/GraduationDesign/student/import',
            onComplete : function(filename, response){
            	response = eval("("+ /\{.*\}/.exec(response) + ")");
				$("#teacherInfo").dynamicTables({
		            'title' : ['账户', '姓名', '性别', '所属专业', 'QQ', '联系方式', '电子邮件', '备注'],
		            'data' : response.infos,
		            typeConfig : [
		                {'type' : 'text'},
		                {'type' : 'text'},
		                {
		                	'type' : 'select',
		                	'options' : [
		                		['0', '男'],
		                		['1', '女'],
		                	]
		                },
		                {'type' : 'text'},
		                {'type' : 'text'},
		                {'type' : 'text'},
		                {'type' : 'text'},
		                {'type' : 'text'},
		            ],
		            'noDel' : true,
		            'noAdd' : true,
		            'saveURL' : '/GraduationDesign/student/adds'
		        })
	        }
        })
    })
</script>
<%@ include file="footer.jsp"%>