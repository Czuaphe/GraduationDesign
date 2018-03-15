<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>

    <div class="col-md-10">
        <h1>教师信息管理</h1>
        <hr/>
        <h4 class="text-danger text-center" id="info"></h4>
        <table id="teacherInfo">
        </table>
    </div>
</div>
<div class="modal fade" id="repassModal" tabindex="-1" role="dialog" aria-labelledby="repassModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h3 class="modal-title">操作提醒</h3>
            </div>
            <div class="modal-body">
                <p class="text-center text-danger" id="repassInfo">您正在重置用户密码，请确认您的操作</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="reset" onclick="do_repass()">确认重置</button>
            </div>
        </div>
    </div>
</div>
<script>
    var currID = -1;
    $(document).ready(function(){
        $.get("/GraduationDesign/teacher/show?currentPage=1", function(response){
            if(response.status == true) {
                $("#info").html("");
                $("#teacherInfo").dynamicTables({
                    'title': ['编号', '账户', '姓名', '性别', '所属专业', '职称', '学位', 'QQ', '联系方式', '电子邮件', '备注', '专业负责人', '出题数量'],
                    'data' : response.data,
                    'delsURL': '/GraduationDesign/teacher/dels',
                    'saveURL': '/GraduationDesign/teacher/update',
                    'addURL' : '/GraduationDesign/teacher/add',
                    'paginationURL' : '/GraduationDesign/teacher/show',
                    'totalPage': response.totalPage,
                    'currentPage': response.currentPage,
                    'noOperator': false,
                    'operatorHTML': '<td class="text-center">' +
                    '<button class="btn btn-danger btn-sm" onclick="refresh_password($(this).parent().parent())" type="button" data-toggle="tooltip" data-placement="top" title="重置密码"><span class="glyphicon glyphicon-refresh"></span></button>' +
                    '</td>',
                    'typeConfig': [{"edit": false},
                        {"type": "text"},
                        {"type": "text"},
                        {
                            "type": "select",
                            "options": [
                                ["0", "男"],
                                ["1", "女"]
                            ]
                        },
                        {
                            "type": "select",
                            "options": response.majors,
                        },
                        {
                            "type": "select",
                            "options": [
                                ["助教", "助教"],
                                ["讲师", "讲师"],
                                ["副教授", "副教授"],
                                ["教授", "教授"],
                            ]
                        },
                        {
                            "type": "select",
                            "options": [
                                ["学士", "学士"],
                                ["硕士", "硕士"],
                                ["博士", "博士"],
                            ]
                        },
                        {"type": "text"},
                        {"type": "text"},
                        {"type": "text"},
                        {"type": "text"},
                        {"edit": false},
                        {"type": "text"},
                    ],
                });

            } else {
                $("#info").html("获取失败");
            }
        })
        $("#info").html("获取中请稍后");
        window.setTimeout('if($("#info").html() == "获取中请稍后") $("#info").html("请求超时")', 2000);
    })

    function refresh_password(target) {
        currID = $(target).children().eq(1).html();
        $("#repassModal").modal({});
    }

    function do_repass() {
        if(currID != -1) {
            $.post("/GraduationDesign/teacher/resetPassword", {id:currID}, function(data){
                if(data.status == true) {
                    $("#repassInfo").html("重置成功 密码已经更改为123456");
                    $("#reset").hide();
                } else {
                    $("#repassInfo").html("重置失败，请重试！");
                }
            })
            $("#repassInfo").html("操作中，请稍后！");
        }

    }
</script>
<%@ include file="footer.jsp"%>