<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<hr/>
<nav class="navbar navbar-default navbar-fixed-bottom">
    <p class="text-center navbar-text">毕业设计选题系统</p>
</nav>
<script>
	$(document).ready(function(){
		var url = window.location.href;
		url = url.substr(url.indexOf("/GraduationDesign/", 0) + 18);
		
	
		$(".nav-stacked").find(".active").removeClass("active");
	
		$(".nav-stacked li").each(function(){
			if($(this).children().eq(0).attr("href") == url) {
			    $(this).addClass("active");
			}
		})
	})
</script>
</body>
</html>