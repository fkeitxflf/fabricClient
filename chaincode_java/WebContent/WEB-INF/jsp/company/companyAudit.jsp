<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#companyAudtiForm').form({
			url : '${ctx}/company/audit.do',
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
		
		$("#note").val('${companyDto.note}');
		$("#status").val('${companyDto.status}');
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
	<form id="companyAudtiForm" method="post" style="margin: 10;text-align: center;">
		<table class="grid">
		<tr>
		<td>备案状态：</td>
		<td><input name="id" id="id" type="hidden" value="${companyDto.id}"><select class="easyui-combobox" id="status" name="status" data-options="width:150,height:25,editable:false,panelHeight:'auto'">
					<option value="0">未备案</option>
					<option value="1">已备案</option>
			   </select></td>
		</tr>
		<tr>
		<td>备注：</td>
		<td><textarea id="note" rows=5 name="note" class="textarea easyui-validatebox"></textarea></td>
		</tr>
		</table>
	</form>
	</div>
	</div>