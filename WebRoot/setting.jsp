<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-7 col-md-offset-1">
        <h1>系统配置</h1>
        <hr/>
        <div class="row">
            <div class="col-md-4">
                <div class="list-group" id="majors">
                </div>
            </div>
            <div class="col-md-8 well">
                <form class="form-horizontal" id="timeSetting">
                    <div class="form-group">
                        <label class="control-label col-sm-2">录入开始时间</label>
                        <div class="col-sm-3">
                            <input type="text" id="t1" value="" formCheck-noEmpty formCheck-info="时间不能为空" class="form-control" />
                        </div>
                        <label class="control-label col-sm-2">录入结束时间</label>
                        <div class="col-sm-3">
                            <input type="text" id="t2" value="" formCheck-noEmpty formCheck-info="时间不能为空" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2">审核开始时间</label>
                        <div class="col-sm-3">
                            <input type="text" id="t3" value="" formCheck-noEmpty formCheck-info="时间不能为空" class="form-control" />
                        </div>
                        <label class="control-label col-sm-2">审核结束时间</label>
                        <div class="col-sm-3">
                            <input type="text" id="t4" value="" formCheck-noEmpty formCheck-info="时间不能为空" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2">选题开始时间</label>
                        <div class="col-sm-3">
                            <input type="text" id="t5" value="" formCheck-noEmpty formCheck-info="时间不能为空" class="form-control" />
                        </div>
                        <label class="control-label col-sm-2">选题结束时间</label>
                        <div class="col-sm-3">
                            <input type="text" id="t6" value="" formCheck-noEmpty formCheck-info="时间不能为空" class="form-control" />
                        </div>
                    </div>
                </form>
                <hr/>
                <button class="btn btn-danger" style="float: right;" type="button" id="save">保存</button>
            </div>
        </div>
    </div>
</div>
<script>
    var mid = -1;
    var data = new Array();
    $(document).ready(function () {
        $.get("/GraduationDesign/major/showTime", function(response){
            if(response.status == true) {
                var str = '';
                for(var i = 0; i < response.info.length; i++) {
                    str += '<a href="#" class="list-group-item">'+response.info[i][1]+'<span hidden>'+response.info[i][0]+'</span></a>';
                    data[response.info[i][0]] = new Array(
                       response.info[i][2],
                       response.info[i][3],
                       response.info[i][4],
                       response.info[i][5],
                       response.info[i][6],
                       response.info[i][7]
                    );
                }
                $("#majors").html(str);
            }
        })

        $.fn.datetimepicker.dates['zh-CN'] = {
            days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
            daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
            daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
            months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            today: "今天",
            suffix: [],
            meridiem: ["上午", "下午"]
        };
        $("#timeSetting .form-control").datetimepicker({
            'weekStart' : '1',
            'autoclose' : true,
            'language' : 'zh-CN',
        });

        $("#save").click(function(){
            if($("#majors").children(".active").length == 0)
                alert("还未选择任何专业");
            else {
                if($.formCheck($("#timeSetting"))) {
                    var t = new Array();
                    $("[formCheck-noEmpty]").each(function () {
                        t[$(this).attr("id")] = $(this).val();
                    })
                    if(t["t1"] > t["t2"] || t["t3"] > t["t4"] || t["t5"] > t["t6"]) {
                        alert("结束时间小于开始时间")
                    } else {
                        var info = new Array(
                          mid, t["t1"], t["t2"], t["t3"], t["t4"], t["t5"], t["t6"]
                        );
                        $.post("/GraduationDesign/major/updateTime", {info:info}, function (response) {
                            if(response.status == true) {
                                alert("更新成功");
                                window.location.reload()
                            } else {
                                alert(response.info)
                            }
                        })
                    }
                }
            }
        })
        
        $("#majors").on("click", ".list-group-item", function () {
            $("#majors").children(".active").removeClass('active');
            $(this).addClass('active');
            mid = $(this).children().eq(0).html();

            for(var i = 0; i < 6; i++) {
                $("#t" + parseInt(i + 1)).val(data[mid][i])
            }
        })
    })
</script>
<%@ include file="footer.jsp"%>