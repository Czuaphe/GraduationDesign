<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-10" >
        <h1>题目管理</h1>
        <hr/>
        <table id="problemInfo">
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
<div class="modal fade" tabindex="-1" id="editModal" role="dialog" aria-labelledby="editModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" >编辑和查看</h4>
            </div>
            <div class="modal-body">
                <div class="col-sm-12">
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
                                            <option value="1">计算机科学与技术</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">新题目</label>
                                    <div class="col-sm-6">
                                        <label class="radio-inline">
                                            <input type="radio" name="r0"  id="new1" value="1" checked> 是
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="r0" id="new0" value="0"> 否
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
                </div>
                <hr/>
                <h4>审核意见</h4>
                <textarea class="form-control" rows="5" disabled id="commit"></textarea>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary"  id="update"><span class="glyphicon glyphicon-floppy-disk"></span> 保存</button>
            </div>
        </div>
    </div>
</div>
<script>

    var stu_id = -1;
    var pro_id = -1;
    $(document).ready(function(){
        $.get("<%= basePath %>problem/show/teacher", function(response){
            if(response.status == true) {
                $("#problemInfo").children().remove();

                $("#problemInfo").dynamicTables({
                    'title': ['编号', '课题名称', '适合专业', '新题目', '课题类型', '课题来源', '课题性质', '选题方式', '教师姓名', '审核状态'],
                    'data': response.data,
                    'noAdd': true,
                    'noSave': true,
                    'delsURL': '<%= basePath %>problem/dels',
                    'typeConfig': [
                        {"edit": false},
                        {"edit": false},
                        {"edit": false},
                        {"edit": false},
                        {"edit": false},
                        {"edit": false},
                        {"edit": false},
                        {"edit": false},
                        {"edit": false},
                        {"edit": false},
                    ],
                    'noOperator': false,
                    'operatorHTML': '<th>' +
                    '<button class="btn btn-primary" type="button" onclick="edit($(this).parent().parent())" data-toggle="tooltip" data-placement="top" title="编辑、查看"><span class="glyphicon glyphicon-edit"></span></button>' +
                    '</th>',
                });
            }
        })

        $("#selectType").change(function(){
            if($(this).val() == 1)
                $("#typeTab").parent().show();
            else
                $("#typeTab").parent().hide();
        })

        $("#update").click(function(){
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
                    new Array(pro_id,name,major, newPro,type,from,attr,stu_id == -1 ? 0 : stu_id, description,require,scienceName)
                );
                $.post("<%= basePath %>problem/update", {infos : info}, function(response){
                    if(response.status == true) {
                        alert("修改成功！");
                        stu_id = -1;
                        document.forms[0].reset();
                        document.forms[1].reset();
                        document.forms[2].reset();
                        document.forms[3].reset();
                        window.location.reload();
                    } else {
                        alert("修改失败-" + response.info);
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
    function edit(target) {
        var id = $(target).children().eq(1).html();
        $.get("<%= basePath %>problem/details/teacher?pro_id=" + id, function(response){
            if(response.status == true) {
                pro_id = response.info[0];
                $("#name").val(response.info[1]);
                $("#major").val(response.info[2]);
                $(":radio[name='r0']").removeAttr("checked");
                $("#new" + response.info[3]).prop("checked", true);

                $("#type").val(response.info[4]);
                $("#from").val(response.info[5]);
                $("#scienceName").val(response.info[6]);

                $("#attr").val(response.info[7]);
                $("#selectType").val(response.info[8] == 0 ? 0 : 1);
                $("#studentID").val(response.info[11]);
                $("#studentName").val(response.info[12]);
                $("#description").val(response.info[13]);
                $("#require").val(response.info[14]);
                $("#commit").val(response.info[15]);
                stu_id = response.info[8];
                $("#stu_id").val(response.info[8]);

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

                $("#editModal").modal();
            } else {
                alert("获取题目信息失败");
            }
        })
    }
</script>
<%@ include file="footer.jsp"%>
