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
        $("#teacherInfo").dynamicTables({
            'title' : ['账户', '姓名', '性别', '所属专业', '职称', '学位', 'QQ', '联系方式', '电子邮件', '备注'],
            'noDel' : true,
            'noAdd' : true,
            'noSave' : true,
        })

        $("#uploadFile").AjaxFileUpload({
           'action' : '<%= basePath %>teacher/import',
            onComplete : function(response){

            }
        });
    })
</script>
<%@ include file="footer.jsp"%>