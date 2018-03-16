<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="header.jsp"%>
    <div class="col-md-8">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h1>公告</h1>
            </div>
            <div class="panel-body" id="notice">
                <blockquote>
                    <p>暂无公告</p>
                </blockquote>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        $.get("", function(response){
            if(response.status == true) {
                $("#notice").html("");
                var str = ""
                for(var i = 0; i < response.data.length; i++) {
                    str += '<blockqute><p>'+ response.data[i] +'</p></blovkqute>'
                }
            }
        })
    })
</script>
<%@ include file="footer.jsp"%>