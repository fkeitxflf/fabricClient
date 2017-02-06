<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<% 
String _SHA1;
String _3DES;
int b = 0;
int a = 0;
_SHA1 = "";
_3DES = "";
Random r = new Random();
for (int i = 0; i < 32; i++) {
	a = r.nextInt(26);
	b = (char) (a + 65);
	_SHA1 += new Character((char) b).toString();
}
for (int i = 0; i < 24; i++) {
	a = r.nextInt(26);
	b = (char) (a + 65);
	_3DES += new Character((char) b).toString();
}		
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript"  src="js/mToken_K1Mgr.js" ></script>
  	<script type="text/javascript"  src="js/K1mTokenPluginMgr.js" ></script>
  	<script type="text/javascript"  src="js/base64.js" ></script>
  </head>
  
  
  <script type="text/javascript">
  
    var xmlhttp ;

	//加载AJAX
	function loadXMLDoc(url, cfunc) {
		
	
		if(xmlhttp == null) {
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
		}

		xmlhttp.onreadystatechange = cfunc;

		xmlhttp.open("POST", url, true);
		xmlhttp.send();

	}
	//密钥随机数
	function mi(){
		document.getElementById("strPriKey").value = "<%=_3DES%>";
	}
	//种子码随机数
	function zhongzi11(){
		document.getElementById("strSeed").value = "<%=_SHA1%>";
	}
	//查找
	function  SeleFind (){
		K1mTokenMgrFindDevice();
	}
	//本地写KEY
    function writeKey(){
    	
    	K1MgrmTokenSetUserInfo();
    	var strSuperPin = document.getElementById("strSuperPin").value;
		var strNewSuperPin = document.getElementById("strNewSuperPin").value;
		var strUserPin = document.getElementById("strUserPin").value;
		var strSeed = document.getElementById("strSeed").value;
		var strPriKey = document.getElementById("strPriKey").value;
		var hID = document.getElementById("hID").value;
    	
    }
  
  	function Clear(){
		document.getElementById("strSuperPin").value = "";
		document.getElementById("strNewSuperPin").value = "";
		document.getElementById("strUserPin").value = "";
		document.getElementById("strSeed").value = "";
		document.getElementById("strPriKey").value = "";
		document.getElementById("strKeyName").value = "";
		document.getElementById("strDescripion").value = "";
		document.getElementById("strUrl").value = "";
		document.getElementById("strOther").value = "";
		document.getElementById("application").value = "";
	}
  </script>
<BODY style="BACKGROUND-POSITION-Y: -120px; BACKGROUND-IMAGE: url(images/bg.gif); BACKGROUND-REPEAT: repeat-x">
<object id="mTokenPlugin" type="application/x-mtokenplugin" width="0" height="0">
    		<param value="pluginLoaded" />
		</object>

<DIV>
  <TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0>
    <TBODY>
     
      <TR>
        <TD style="BACKGROUND-IMAGE: url(images/main_ls.gif)">&nbsp;</TD>
        <TD style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 10px; COLOR: #566984; PADDING-TOP: 10px; BACKGROUND-COLOR: white" vAlign=top align=middle>
          <DIV>
            <TABLE class=gridView id=ctl00_ContentPlaceHolder2_GridView1  style="WIDTH: 100%; BORDER-COLLAPSE: collapse" cellSpacing=0 rules=all border=1>
	  		
              <TBODY>
				<TR>
                  <TD height="395" align="center" valign="middle" class=gridViewItem >
				  <form method="post" action = "UpdateDB" >
				  <table width="686" height="378" cellspacing="0">
                    <tr>
                      <td width="168" align="right">硬件ID:</td>
                      <td height="18" align="left">
                      	<input id = "hID" style = "width:350px" name = "uid"></input>
                      </td>
                    </tr>
                    <tr>
                      <td width="168" align="right">超级密码:</td>
                      <td width="494" align="left">
                      <input type="text" name="strSuperPin" id="strSuperPin" style="width:350px" value="admin">
                      
                      <input type="button" value="登陆"  onClick="return K1mTokenMgrOpen()"/>
                      </td>
                    </tr>
                    <tr>
                      <td align="right">新超级密码:</td>
                      <td align="left"><input type="text" name="strNewSuperPin" id="strNewSuperPin" style="width:350px" value="admin">
                      					<input type = "button" value = "修改" onClick = "K1mTokenMgrChangePwd();">
                      </td>
                    </tr>
                    <tr>
                      <td align="right">用户密码:</td>
                      <td align="left"><input type="text" name="strUserPin" id="strUserPin" style="width:350px" value="12345678"></td>
                    </tr>
                    <tr>
                      <td align="right">种子码:</td>
                      <td align="left"><input type="text" name="strSeed" id="strSeed" style="width:350px" value="FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF">
                        <input type="button" name="zhongzi" value="随机" onClick="zhongzi11();">
                        <input type="button" name="Submit2" value="设置" onClick="K1MgrmTokenSetSeedKey();"></td>
                    </tr>
                    <tr>
                      <td align="right">3DES密钥:</td>
                      <td align="left"><input type="text" name="strPriKey" id="strPriKey" style="width:350px" value="ABCDEFGhijklmn0123456789">
                      	<input type="button" name="miyao" value="随机" onClick="mi();">
                        <input type="button" name="Submit22" value="设置" onClick="K1MgrmTokenSetMainKey();"></td>
                    </tr>
                   
                    <tr>
                      <td align="right">USB Key别名:</td>
                      <td align="left"><input type="text" name="strKeyName" id="strKeyName" style="width:350px"></td>
                    </tr>
                    <tr>
                      <td align="right">公司名称:</td>
                      <td align="left"><input type="text" name="strDescripion" id="strDescripion" style="width:350px"></td>
                    </tr>
                    <tr>
                      <td align="right">公司网址:</td>
                      <td align="left"><input type="text" name="strUrl" id="strUrl" style="width:350px" value="www.longmai.com.cn"></td>
                    </tr>
                    <tr>
                      <td height="24" align="right">备注信息:</td>
                      <td align="left"><input type="text" name="strOther" id="strOther" style="width:350px"></td>
                    </tr>
					
                    <tr align="right">
					  <td align="right">选择浏览器:</td>
                      <td height="24"  align="left">
					    <input name="radiobutton" type="radio" value="0" checked>
                        使用默认浏览器进入网站&nbsp;
                        <input name="radiobutton" type="radio" value="1">
                        指定使用IE浏览器进入网站                        </td>
                      </tr>
                    <tr align="right">
					  <td align="right">远程注册:</td>
                      <td height="38" align="left"><input name="nEnableRemote" type="radio" value="1">
                        开启&nbsp;
                        <input name="nEnableRemote" type="radio" value="0" checked>
                        关闭</td>
                    </tr>
                    <tr align="right">
                      <td height="36" colspan="2" align="center">
                          <input type="button" name="button" value="查找USB Key" style="width:100px" onClick="SeleFind();">
                          &nbsp;
                        <input type="submit" name="Submit" value="本地写Key" style="width:100px" onClick="writeKey()">
                        &nbsp;
                        <input type="button" name="reset" value="清空" style="width:100px" onClick="Clear();">                      </td>
                      </tr>
                    <tr align="right">
                      <td height="22" colspan="2" align="center"><a href="Login.jsp">写Key成功后点击此链接跳转登录页面</a></td>
                    </tr>
                  </table>
                  </form></TD>
                </TR>
              </TBODY>
            </TABLE>
          </DIV>
        </TD>
        <TD style="BACKGROUND-IMAGE: url(images/main_rs.gif)"></TD>
      </TR>
      
    </TBODY>
  </TABLE>
</DIV>
</BODY>
</html>
