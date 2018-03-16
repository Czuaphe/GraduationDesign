<%@page import="com.graduation.entity.Major"%>
<%@page import="com.graduation.dao.MajorDao"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-6 col-md-offset-1">
        <h1>添加题目</h1>
        <hr/>
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#t1" aria-controls="t1" role="tab" data-toggle="tab">基本设置</a></li>
                <li role="presentation"><a href="#t2" aria-controls="t2" role="tab" data-toggle="tab">课题简介</a></li>
                <li role="presentation"><a href="#t3" aria-controls="t3" role="tab" data-toggle="tab">课题要求</a></li>
                <li role="presentation"><a id="typeTab" href="#t4" aria-controls="t4" role="tab" data-toggle="tab">指定学生</a></li>
            </ul>
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="t1">
                    <hr/>
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">课题名称</label>
                            <div class="col-sm-6">
                                <input type="text" id="name" value="" class="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">适合专业</label>
                            <div class="col-sm-6">
                                <select class="form-control" id="major">
                                    <option value="0">请选择专业</option>
                                    <% 	List<Major> majorList = new MajorDao().getAllMajor();
                                    	for (Major major : majorList) {
                                     %>
                                    <option value="<%=major.getMid() %>"><%=major.getMajor() %></option>
                                    <%	} %>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">新题目</label>
                            <div class="col-sm-6">
                                <label class="radio-inline">
                                    <input type="radio" name="r0"  value="1" checked> 是
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="r0"value="0"> 否
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">课题类型</label>
                            <div class="col-sm-6" >
                                <select class="form-control" id="type">
                                    <option value="0">请选择课题类型</option>
                                    <option value="1">毕业设计</option>
                                    <option value="2">毕业论文</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">课题来源</label>
                            <div class="col-sm-6">
                                <select class="form-control" id="from" onchange="if($(this).val() == 2) $(this).parent().parent().next().show(); else $(this).parent().parent().next().hide();">
                                    <option value="0">请选择课题来源</option>
                                    <option value="1">自拟题目</option>
                                    <option value="2">科研项目</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" style="display: none;">
                            <label class="col-sm-2 control-label">科研项目</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="scienceName" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">课题性质</label>
                            <div class="col-sm-6">
                                <select class="form-control" id="attr">
                                    <option value="0">请选择课题性质</option>
                                    <option value="1">理论研究</option>
                                    <option value="2">应用基础及其理论研究</option>
                                    <option value="3">工程技术研究</option>
                                    <option value="4">其他</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">选题方式</label>
                            <div class="col-sm-6">
                                <select class="form-control" id="selectType">
                                    <option value="-1">请选择选题方式</option>
                                    <option value="0" selected>盲选</option>
                                    <option value="1">指定学生</option>
                                </select>
                            </div>
                        </div>
                    </form>
                </div>
                <div role="tabpanel" class="tab-pane" id="t2">
                    <hr/>
                    <form class="form-horizontal">
                        <div class="form-group">
                            <div class="col-sm-10 col-sm-offset-1">
                                <textarea id="description" rows="20" class="form-control" placeholder="请填写课题简介"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div role="tabpanel" class="tab-pane" id="t3">
                    <hr/>
                    <form class="form-horizontal">
                        <div class="form-group">
                            <div class="col-sm-10 col-sm-offset-1">
                                <textarea rows="20" id="require" class="form-control" placeholder="请填写课题要求"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div role="tabpanel" class="tab-pane" id="t4">
                    <hr/>
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-2">学号</label>
                            <div class="col-sm-4">
                                <input type="text" value="" id="studentID" placeholder="请输入完整学号" class="form-control" />
                                <p class="text-danger"></p>
                            </div>
                            <div class="col-sm-4">
                                <button class="btn btn-default" id="searchStudent" type="button"><span class="glyphicon glyphicon-search"></span> 搜索</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">姓名</label>
                            <div class="col-sm-4">
                                <input type="text" id="studentName" value="" disabled class="form-control" />
                                <input type="hidden" id="stu_id">
                            </div>
                            <div class="col-sm-4">
                                <button class="btn btn-danger" id="bind" type="button"><span class="glyphicon glyphicon-lock"></span> 绑定</button>
                                <button class="btn btn-danger" id="unbind" type="button"><span class="glyphicon glyphicon-unlock"></span> 取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        <p class="text-right"><button class="btn btn-primary" id="save" type="button"><span class="glyphicon glyphicon-floppy-disk"></span> 保存</button></p>
    </div>
</div>
<script>
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
                $.post("/GraduationDesign/problem/add", {info : info}, function(response){
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
                $.post("/GraduationDesign/student/info", {stu_id:id}, function(data){
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
</script>
<%@ include file="footer.jsp"%>