<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>

    <div class="col-md-4 col-md-offset-2">
        <h1>修改用户信息</h1>
        <hr/>
        <form class="form-horizontal" id="userInfo">
            <div class="form-group">
                <label class="col-sm-2 control-label">用户名</label>
                <div class="col-sm-6">
                    <input type="text" value="" disabled class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">姓名</label>
                <div class="col-sm-6">
                    <input type="text" id="name" formCheck-noEmpty formCheck-info="姓名不能为空" value="" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">性别</label>
                <div class="col-sm-6">
                    <select class="form-control" id="sex">
                        <option value="0">男</option>
                        <option value="1">女</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">联系方式</label>
                <div class="col-sm-6">
                    <input type="text" id="phone" formCheck-regex="[1-9]{1}[0-9]{10}" formCheck-info="联系方式填写错误" value="" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">QQ</label>
                <div class="col-sm-6">
                    <input type="text" id="qq" formCheck-regex="\d{5,12}" formCheck-info="QQ填写错误" value="" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">电子邮件</label>
                <div class="col-sm-6">
                    <input type="text" id="mail" formCheck-email value="" class="form-control">
                </div>
            </div>
            <hr/>
            <div class="form-group">
                <label class="col-sm-2 control-label">新密码</label>
                <div class="col-sm-6">
                    <input type="password" id="password1" placeholder="如需修改密码，请填写" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">确认密码</label>
                <div class="col-sm-6">
                    <input type="password" id="password2" placeholder="如需修改密码，请填写" class="form-control">
                </div>
            </div>
        </form>
        <button id="save" class="btn btn-danger" style="float:right" type="button">保存</button>
    </div>
</div>
<script>
    $(document).ready(function () {
        $.get("http://10.165.215.202:8080/GraduationDesign/student/show", function(response){
            if(response. status == true) {
                $(":text:disabled").val(response.info[1]);
                $("#name").val(response.info[2]);
                $("#sex").val(response.info[3]);
                $("#phone").val(response.info[6]);
                $("#qq").val(response.info[5]);
                $("#mail").val(response.info[7]);
            }
        })

        $("#save").click(function(){
            if($.formCheck($("form"))) {
                var password1 = $("#password1").val();
                var password2 = $("#password2").val();

                if(password1.length != 0 && password1 != password2) {
                    $("#password1").parent().addClass('has-error');
                    $("#password2").parent().addClass('has-error');
                } else {
                    var info = new Array();
                    $("#userInfo").find(".form-control").each(function(){
                        if(typeof $(this).attr("id") !== "undefined") {
                            info.push($(this).val());
                        }
                    })
                    $.post("http://10.165.215.202:8080/GraduationDesign/student/modify", {info:info}, function (response) {
                        if(response.status == true) {
                            alert("更新成功");
                            window.location.reload();
                        } else {
                            alert("更新失败");
                        }
                    })
                }
            }
        })
    })
</script>
<%@ include file="footer.jsp"%>