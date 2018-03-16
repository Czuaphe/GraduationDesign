<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-6 col-md-offset-3">
        <h1>导入教师信息</h1>
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
           'action' : '/GraduationDesign/teacher/import',
            onComplete : function(response){
				$("#teacherInfo").dynamicTables({
		            'title' : ['账户', '姓名', '性别', '所属专业', '职称', '学位', 'QQ', '联系方式', '电子邮件', '备注'],
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
		                {
		                	'type' : 'select',
		                	'options' : response.major
		                },
		                {'type' : 'text'},
		                {'type' : 'text'},
		                {'type' : 'text'},
		                {'type' : 'text'},
		                {'type' : 'text'},
		                {'type' : 'text'},
		            ],
		            'noDels' : true,
		            'noAdd' : true,
		            'noSave' : true,
		        })
            }
        });
    })
</script>
<%@ include file="footer.jsp"%>