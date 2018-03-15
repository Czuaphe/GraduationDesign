<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-10">
        <h1>题目审核</h1>
        <hr/>
        <table id="problems">
        </table>
    </div>
</div>
<div class="modal fade" tabindex="-1" id="detailModal" role="dialog" aria-labelledby="detailModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="name">基于Java的毕业设计选题系统</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="row" >
                            <div class="col-sm-6" id="info1">
                                <h5>适合专业：<small></small></h5>
                                <h5>新题目：<small></small></h5>
                                <h5>课题类型：<small></small></h5>
                                <h5>课题来源：<small></small></h5>
                                <h5>课题性质：<small></small></h5>
                            </div>
                            <div class="col-sm-6" id="info2">
                                <h5>选题方式：<small></small></h5>
                                <h5 class="text-danger">审核状态：<small>还未审核</small></h5>
                                <h5>审核时间：<small></small></h5>
                                <h5 class="text-danger">指导教师：<small></small></h5>
                                <h5 class="text-danger">联系方式：<small></small></h5>
                            </div>
                        </div>
                        <hr/>
                        <h4 id="str1">课题简介:</h4>
                        <textarea class="form-control" rows="5" disabled></textarea>
                        <h4 id="str2">课题要求</h4>
                        <textarea class="form-control" rows="5" disabled></textarea>
                        <hr/>
                        <h4 id="str3">审核意见</h4>
                        <textarea class="form-control" rows="5" disabled></textarea>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" tabindex="-1" id="commitModal" role="dialog" aria-labelledby="commitModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" >审核意见</h4>
            </div>
            <div class="modal-body">
                <textarea class="form-control" rows="20" id="commit"></textarea>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="submitCommit">提交</button>
            </div>
        </div>
    </div>
</div>
<script>
    var pid = -1;
    $(document).ready(function(){
        $.get("/problem/show?currentPage=1", function(response) {
            $("#problems").dynamicTables({
                'title' : ['编号', '课题名称', '适合专业', '新题目', '课题类型', '课题来源', '课题性质', '选题方式', '教师姓名','审核状态'],
                'data' : response.data,
                currentPage : response.currentPage,
                totalPage : response.totalPage,
                paginationURL : "/problem/show",
                'typeConfig' : [
                    {"edit": false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit" : false},
                    {"edit" : false},
                ],
                'noAdd' : true,
                'noDel' : true,
                'noSave' : true,
                'noOperator' : false,
                'operatorHTML' : '<th class="text-center">' +
                '<button class="btn btn-default btn-sm" onclick="pass_analysis($(this).parent().parent())" type="button" data-toggle="tooltip" data-placement="top" title="通过审核"><span class="glyphicon glyphicon-ok"></span></button> ' +
                ' <button class="btn btn-danger btn-sm" onclick="nopass_analysis($(this).parent().parent())" type="button" data-toggle="tooltip" data-placement="top" title="不通过审核"><span class="glyphicon glyphicon-remove"></span></button>' +
                ' <button class="btn btn-primary btn-sm" onclick="show($(this).parent().parent())" type="button" data-toggle="tooltip" data-placement="top" title="查看详情"><span class="glyphicon glyphicon-option-horizontal"></span></button>' +
                '</th>',
            });
        })

        $("#submitCommit").click(function(){
            var str = $("#commit").val();
            if(str.length == 0) {
                alert("审核意见为空！");
            } else {
                $.post("/problem/verify", {accepted:false, pro_id:pid, content:str}, function(response){
                    if(response.status == true) {
                        alert("提交成功");
                        $("#commit").val("");
                        window.location.reload();
                    } else {
                        alert(response.info);
                    }
                })
            }
        })
    })

    function pass_analysis(target) {
        if($(target).children().eq(9).html() == "通过审核") {
            alert("已经通过审核");
            return;
        }
        var id = $(target).children().eq(0).html();
        $.post("/problem/verify", {accepted:true, pro_id:id, content:""}, function(response){
            if(response.status == true) {
                alert("提交成功");
                window.location.reload();
            } else {
                alert(response.info);
            }
        })
    }

    function nopass_analysis(target) {
        pid = $(target).children().eq(0).html();

        $("#commitModal").modal();
    }

    function show(target){
        var id = $(target).children().eq(0).html();
        $.get("/problem/details?pro_id=" + id, function(response){
            if(response.status == true) {
                $("#name").html(response.info[1]);
                $("#info1").children().eq(0).children().eq(0).html(response.info[2]);
                $("#info1").children().eq(1).children().eq(0).html(response.info[3]);
                $("#info1").children().eq(2).children().eq(0).html(response.info[4]);
                $("#info1").children().eq(3).children().eq(0).html(response.info[5]);
                $("#info1").children().eq(4).children().eq(0).html(response.info[6]);
                $("#info2").children().eq(0).children().eq(0).html(response.info[7]);
                $("#info2").children().eq(1).children().eq(0).html(response.info[13]);
                $("#info2").children().eq(2).children().eq(0).html(response.info[11]);
                $("#info2").children().eq(3).children().eq(0).html(response.info[8]);
                $("#info2").children().eq(4).children().eq(0).html(response.info[14]);

                $("#str1").next().html(response.info[9]);
                $("#str2").next().html(response.info[10]);
                $("#str3").next().html(response.info[12]);
                $("#detailModal").modal();
            } else {
                alert("请求失败")
            }
        })
    }
</script>
<%@ include file="footer.jsp"%>