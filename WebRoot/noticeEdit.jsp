<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-6 col-md-offset-1">
        <h1>公告编辑</h1>
        <hr/>
        <table id="notice">
            <tr>
                <td>
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <img src="./image/loading.gif" class="img-circle text-center"/>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
<script>
    $(document).ready(function(){
        $.get("<%= basePath %>notice/show?currentPage=1", function(response){
            $("#notice").html("");
            $("#notice").dynamicTables({
                title : ["编号", "公告内容", "开始显示时间", "结束显示时间","可见性控制"
                ],
                delsURL : '<%= basePath %>notice/dels',
                saveURL  : '<%= basePath %>notice/update',
                addURL : '<%= basePath %>notice/add',
                totalPage : response.totalPage,
                currentPage : response.currentPage,
                data : response.data,
                typeConfig: [
                    {'edit' : false},
                    {'type' : 'textarea'},
                    {'type' : 'date'},
                    {'type' : 'date'},
                    {
                        'type' : 'select',
                        'options' : [
                            ['1', "仅学生可见"],
                            ['2', "仅教师可见"],
                            ['3', "仅专业负责人可见"],
                            ['4', "全部可见"],
                        ]
                    }
                ]
            })
        })
    })
</script>
<%@ include file="footer.jsp"%>