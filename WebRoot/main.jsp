<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-10">
        <h1>毕设选题</h1>
        <hr/>
        <p>时间：2018-01-17 17:00 ~ 2018-01-17 17:00</p>
        <p>题目数量：102题</p>
        <hr/>
        <div class="row">
            <div class="col-md-2">
                <div class="list-group" id="problemList">
                </div>
                <div class="row">
                    <div class="col-sm-3 text-center">
                        <button type="button" class="btn btn-default" id="prevPage"><span class="glyphicon glyphicon-arrow-left"></span></button>
                    </div>
                    <div class="col-sm-6 text-center">
                        <span id="pages">第1页 / 共1页</span>
                    </div>
                    <div class="col-sm-3 text-center">
                        <button type="button" class="btn btn-default" id="nextPage"><span class="glyphicon glyphicon-arrow-right"></span></button>
                    </div>
                </div>
            </div>
            <div class="col-md-8 well" id="problem">
                <div class="panel panel-default" hidden>
                    <div class="panel-heading">
                        <h3 class="text-center" id="name"></h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-sm-6">
                                <h5>适合专业：<small id="major"></small></h5>
                                <h5>课题类型：<small id="type"></small></h5>
                            </div>
                            <div class="col-sm-6">
                                <h5>课题来源：<small id="from"></small></h5>
                                <h5>课题性质：<small id="attr"></small></h5>
                            </div>
                        </div>

                        <h5>课题简介:</h5>
                        <textarea id="description" rows="5" class="form-control" disabled></textarea>
                        <h5>课题要求</h5>
                        <textarea id="require" rows="5" class="form-control" disabled></textarea>
                    </div>
                    <div class="panel-footer">
                        <button class="btn btn-danger" type="button" id="choose">选择此题</button>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<script>
    var currentPage = 1;
    var totalPage = 1;
    var pro_id = -1;
    $(document).ready(function () {
        update(1);
        $("#nextPage").click(function(){
            update(Math.min(currentPage + 1, totalPage));
        })

        $("#prevPage").click(function () {
            update(Math.max(currentPage - 1, 1));
        })
        
        $("#choose").click(function(){
            if(pro_id != -1) {
                $.post("/GraduationDesign/student/select", {pro_id:pro_id}, function (response) {
                    if(response.status == true) {
                        alert("选题成功");
                    } else {
                        alert(response.info);
                    }
                })
            }
        })

        $("#problemList").on('click', ".list-group-item", function(){
            if($(this).hasClass('active')) {

            } else {
                $(this).parent().children('.active').removeClass('active');
                $(this).addClass('active');
                var id = $(this).children().eq(0).html();
                $.get("/GraduationDesign/problem/details/student?pro_id=" + id, function (response) {
                    if(response.status == true) {
                        $("#problem").children().eq(0).removeAttr("hidden");
                        pro_id = response.info[0];
                        $("#name").html(response.info[1]);
                        $("#major").html(response.info[2]);
                        $("#type").html(response.info[4]);
                        $("#attr").html(response.info[5]);
                        $("#from").html(response.info[6]);
                        $("#description").html(response.info[7]);
                        $("#require").html(response.info[8]);
                    } else {
                        $("#problem").html('<h2 class="text-danger text-center">获取题目失败</h2>')
                    }
                })
            }
        })
    })

    function update(page) {
        $("#problemList").children().remove();
        $.get("/GraduationDesign/problem/show/major?currentPage=1", function(response){
            if(response.status == true) {
                str = '';
                for(var i = 0; i < response.data.length; i++) {
                    str += '<button type="button" class="list-group-item">' + response.data[i][1]+' <span hidden>' + response.data[i][0]+'</span></button>'
                }
                $("#problemList").html(str);
                currentPage = response.currentPage;
                totalPage = response.totalPage;
                $("#pages").html("第" + currentPage + "页 / 共" +totalPage + " 页");
            }
        })
    }
</script>
<%@ include file="footer.jsp"%>