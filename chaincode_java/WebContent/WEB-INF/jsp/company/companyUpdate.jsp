<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#companyFormUpdate').form({
			url : '${ctx}/company/update.do',
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
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
		
		$("#companyId").val('${companyDto.companyId}');
		$("#companyName").val('${companyDto.companyName}');
		$("#manager").val('${companyDto.manager}');
		$("#managerTel").val('${companyDto.managerTel}');
		$("#type").val('${companyDto.type}');
		
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
	<form id="companyFormUpdate" method="post" style="margin: 10;text-align: center;">
		<table class="grid">
		<tr>
		<td>企业类型：</td><td><input id="type" name="type" disabled="disabled"><input name="id" id="id" type="hidden" value="${companyDto.id}"></td>
		<td>企业标识：</td><td><input id="companyId" name="companyId" disabled="disabled"></td>
		</tr>
		<tr>
		<td>企业名称：</td><td><input id="companyName" name="companyName" class="easyui-validatebox" data-options="required:true"></td>
		<td>企业法人：</td><td><input id="manager" name="manager"></input></td>
		</tr>
		<tr>
		<td>法人电话：</td><td><input id="managerTel" name="managerTel" class="easyui-validatebox" data-options="validType:'phoneRex'"></td>
		</tr>
		</table>
	</form>
</div>
</div>
