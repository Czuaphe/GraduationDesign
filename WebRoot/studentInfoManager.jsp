<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>

    <div class="col-md-8">
        <h1>学生信息管理</h1>
        <hr/>
        <h4 class="text-danger text-center" id="info"></h4>
        <table id="studentInfo">
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
     currID = -1;
    $(document).ready(function() {
        $.get("<%= basePath %>student/show?currentPage=1", function (response) {
            if (response.status == true) {
                $("#info").html("");
                $("#studentInfo").dynamicTables({
                    'title': ['编号', '学号', '姓名', '性别', '所属专业', 'QQ', '联系方式', '电子邮件', '备注'],
                    'data': response.data,
                    'delsURL': '<%= basePath %>student/dels',
                    'saveURL': '<%= basePath %>student/update',
                    'addURL': '<%= basePath %>student/add',
                    'paginationURL': '<%= basePath %>student/show',
                    'totalPage': response.totalPage,
                    'currentPage': response.currentPage,
                    'noOperator': false,
                    'operatorHTML': '<td class="text-center">' +
                    '<button class="btn btn-danger btn-sm" onclick="refresh_password($(this).parent().parent())" type="button" data-toggle="tooltip" data-placement="top" title="重置密码"><span class="glyphicon glyphicon-refresh"></span></button>' +
                    '</td>',
                    'typeConfig': [
                        {"edit": false},
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
                        {"type": "text"},
                        {"type": "text"},
                        {"type": "text"},
                        {"type": "text"},
                        {"edit": false},
                    ],
                });

            } else {
                $("#info").html("获取失败");
            }
            window.setTimeout('if($("#info").html() == "获取中请稍后") $("#info").html("请求超时")', 2000);

        })
    })

     var stu_id = -1;
     $(document).ready(function () {

         if(stu_id != -1) {
             $("#bind").hide();
         } else {
             $("#bind").show();
             $("#studentName").val("");
             $("#studentID").val("");
         }

         if($("#selectType").val() != 1)
             $("#typeTab").parent().hide();
         if($("#from").val() == 2)
             $("#scienceName").parent().parent().show();
         $("#selectType").change(function(){
             if($(this).val() == 1)
                 $("#typeTab").parent().show();
             else
                 $("#typeTab").parent().hide();
         })

         $("#save").click(function(){
             var name = $("#name").val();
             var major = $("#major").val();
             var newPro = $(":radio[name='r0']").val();
             var scienceName = $("#scienceName").val();
             var type = $("#type").val();
             var from = $("#from").val();
             var attr = $("#attr").val();
             var selectType = $("#selectType").val();
             var description = $("#description").val();
             var require = $("#require").val();

             if(name.length == 0)
                 $("#name").parent().parent().addClass('has-error');
             else if(major == 0)
                 $("#major").parent().parent().addClass('has-error');
             else if(type == 0)
                 $("#type").parent().parent().addClass('has-error');
             else if(from == 0)
                 $("#from").parent().parent().addClass('has-error');
             else if(from == 2 && scienceName.length == 0)
                 $("#scienceName").parent().parent().addClass('has-error');
             else if(attr == 0)
                 $("#attr").parent().parent().addClass('has-error');
             else if(selectType == -1)
                 $("#selectType").parent().parent().addClass('has-error');
             else if($("div .has-error").length != 0)
                 $("[aria-controls='t1']").html("<span class='text-danger'>基本设置 <span class='glyphicon glyphicon-warning-sign'></span></span>");
             else if(description.length == 0)
             {
                 $("[aria-controls='t2']").html("<span class='text-danger'>课题简介 <span class='glyphicon glyphicon-warning-sign'></span></span>");
             }
             else if(require.length == 0)
             {
                 $("[aria-controls='t3']").html("<span class='text-danger'>课题要求 <span class='glyphicon glyphicon-warning-sign'></span></span>");
             }
             else if($("#selectType").val() == 1 && ($("#stu_id").val() == -1 || $("#stu_id").val() == "")) {
                 $("[aria-controls='t4']").html("<span class='text-danger'>指定学生 <span class='glyphicon glyphicon-warning-sign'></span></span>");
             }
             else {
                 var info = new Array(
                     name,major, newPro,type,from,attr,stu_id == -1 ? 0 : stu_id, description,require,scienceName
                 );
                 $.post("<%= basePath %>problem/add", {info : info}, function(response){
                     if(response.status == true) {
                         alert("添加成功！");
                         stu_id = -1;
                         document.forms[0].reset();
                         document.forms[1].reset();
                         document.forms[2].reset();
                         document.forms[3].reset();
                         window.location.reload();
                     } else {
                         alert("添加失败-" + response.info);
                     }
                 })
             }
             window.setTimeout(function(){
                     $(".has-error").removeClass("has-error");
                     $("[aria-controls=\'t1\']").html("基本设置");
                     $("[aria-controls=\'t2\']").html("课题简介");
                     $("[aria-controls=\'t3\']").html("课题要求");
                     $("[aria-controls=\'t4\']").html("指定学生");
                 }
                 , 3000);
         })

         $("#searchStudent").click(function(){
             $("#studentID").next().html("");
             if($("#studentID").val().length == 0) {
                 $("#studentID").parent().parent().addClass('has-error');
             } else {
                 var id = $("#studentID").val();
                 $.post("<%= basePath %>student/info", {stu_id:id}, function(data){
                     if(data.status == true) {
                         $("#studentName").val(data.stu_name);
                         stu_id = data.id;
                     } else {
                         $("#studentID").next().html("学号错误，找不到相关学生!");
                     }
                 })
             }
         })

         $("#bind").click(function(){
             $(this).hide();
             $("#stu_id").val(stu_id);
         })

         $("#unbind").click(function(){
             $("#bind").show()
             $("#studentName").val("");
             $("#stu_id").val(-1);
         })
     })

    function refresh_password(target) {
        currID = $(target).children().eq(1).html();
        $("#repassModal").modal({});
    }

    function do_repass() {
        if(currID != -1) {
            $.post("<%= basePath %>student/resetPassword", {id:currID}, function(data){
                if(data.status == true) {
                    $("#repassI nfo").html("重置成功 密码已经更改为123456");
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