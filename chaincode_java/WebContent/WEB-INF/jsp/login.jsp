<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="taglibs.jsp"></jsp:include>
<meta charset="utf-8">
<title>欢迎您登录融资贷款管理系统</title>
<link href="${ctx}/styles/login.css" rel="stylesheet" type="text/css" media="screen" />
<link href="${ctx}/styles/u_login.css" rel="stylesheet" type="text/css" media="screen" />
<script>


	var sessionInfo_userId = '${sessionInfo.id}';
	if (sessionInfo_userId) {//如果登录,直接跳转到index页面
		window.location.href='${ctx}/admin/index.do';
	}
	
	$(function() {
		
		$('#loginform').form({
		    url:'${ctx}/admin/login.do',
		    onSubmit : function() {
		    	progressLoad();
				var isValid = $(this).form('validate');
				if(!isValid){
					progressClose();
					return false;
				}
			},
		    success:function(result){
		    	result = $.parseJSON(result);
		    	progressClose();
		    	if (result.success) {
		    		window.location.href='${ctx}/admin/index.do';
		    	}else{
		    		$.messager.show({
		    			title:'提示',
		    			msg:'<div class="light-info"><div class="light-tip icon-tip"></div><div>'+result.msg+'</div></div>',
		    			showType:'show'
		    		});
		    	}
		    }
		});
	});
	
	function submitForm(){
		$('#loginform').submit();
	}
	
	function clearForm(){
		$('#loginform').form('clear');
	}
	
	//回车登录
	function enterlogin(){
		if (event.keyCode == 13){
        	event.returnValue=false;
        	event.cancel = true;
        	$('#loginform').submit();
    	}
	}
	
	
</script>
</head>
<body onkeydown="enterlogin();">
	<object id="mTokenPlugin" type="application/x-mtokenplugin" width="0" height="0">
    		<param value="pluginLoaded" />
	</object>
<form id="loginform" method="post" >
<div id="mainwar">
	<div id="leftbox">
		<div id="sysname_bj">
			<div id="sysname">区块链信用担保系统</div>
		</div>
		<div id="mainimg"></div>
	</div>
	<div id="rightbox">
		<div id="topbog"></div>
		<div class="twolin">
			<div id="login_in">
			<p><label>登录用户:</label><input  name="userName" id="userName" style="width:118px;" class="required" class="easyui-validatebox" data-options="required:true"/></p>
			<p><label>登陆密码:</label><input type="password" name="password" style="width:118px;" class="easyui-validatebox" data-options="required:true"/></p>
			<p>
				<label>&nbsp;</label>
				<button class="button"  type="button" onclick="submitForm()">登录</button>
			</p>
			</div>
		</div>
		<div id="loginfo">
		为了您更方便的使用本系统建议使用<br/>
		Internet Explorer 8.0以上版本<br/>
		技术支持：<a>复杂美科技</a><br/><br/>
		</div>
		<div class="twolin" style="height:70px; width:228px; text-align:center;"></div>
		<div id="botbog"></div>
	</div>
</div>
	    <div id="tip"></div>
</form>
</body>
</html>
