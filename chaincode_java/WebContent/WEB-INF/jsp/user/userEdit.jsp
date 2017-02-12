<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
	
		$('#companyId').combobox({
			valueField:'companyId', //值字段
		    textField:'companyName', //显示的字段
		    url:"${ctx}/company/companySelect.do?",
		    method:'get',
		    panelHeight:'auto',
		    required:true,
			editable:false,
			striped: true,
			value:'${user.companyId}'
		});
		
		$('#roleIds').combotree({
			url : '${ctx}/role/tree.do',
			parentField : 'pid',
			lines : true,
			panelHeight : 'auto',
			multiple : false,
			required: true,
			cascadeCheck : false,
			value : $.stringToList('${user.roleIds}')
		});
		
		$('#userEditForm').form({
			url : '${ctx}/user/edit.do',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});
	
	$("#loginname").val('${user.loginname}');
	$("#name").val('${user.name}');
	$("#telephone").val('${user.telephone}');
	$("#roleIds").val('${user.roleIds}');
	$("#companyId").val('${user.companyId}');
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="userEditForm" method="post">
			<table class="grid">
				<tr>
					<td>登录名</td>
					<td><input name="id" type="hidden"  value="${user.id}">
					<input name="loginname" id="loginname" type="text" style="width: 190px" disabled="disabled"></td>
					<td>姓名</td>
					<td><input name="name" id="name" type="text" style="width: 190px" placeholder="请输入姓名" class="easyui-validatebox" data-options="required:true"></td>
				</tr>
				<tr>
					<td>电话</td>
					<td><input type="text" name="telephone" id="telephone" style="width: 190px" class="easyui-validatebox" data-options="validType:'phoneRex'"/></td>
					<td>角色</td>
					<td><input  id="roleIds" name="roleIds" style="width: 190px"/></td>
				</tr>
				<tr>
					<td>企业</td>
					<td><select id="companyId" name="companyId" style="width: 190px" class="easyui-validatebox" data-options="required:true"></select></td>
				</tr>
			</table>
		</form>
	</div>
</div>