<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>登录成功</title>
		<meta http-equiv="content-type" content="text/html; charset=gbk">
	</head>   
	
	
	<body>     
	<p>通过摘要认证,你已登录成功. 你可以继续下列操作 </p>
	<hr>	
  	</br>
	<div> <a href="K1_ChangePassword.html" target="_self">修改密码</a> -- 修改当前USB Key的用户密码。</div> 
		</br>
	<div><a href="K1_UserStore.html">操作用户存储区</a> -- 操作Key内2K字节的用户存储区，写入和读取都为明文。</div> 
		</br>
	<div><a href="K1_SecureStore.html">操作安全存储区</a> -- 操作Key内2K字节的安全存储区，写入和读取均由Key内3DES加密并进行Base64编码。</div> 
		</br>
	<div><a href="K1_KeyInfo.html" target="_self">查看当前Key信息</a> -- 查看当前Key中信息。</div> 
		</br>
	<div><a href="K1_UserChangePassword.html">密码重置申请</a> -- 用户权限的密码重置申请</div> 
		</br>
		<hr>
	
	</body>
</html>
