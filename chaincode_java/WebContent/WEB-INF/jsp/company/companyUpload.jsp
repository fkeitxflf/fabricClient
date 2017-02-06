<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(function() {
		
		$('#companyUploadForm').form({
			url : '${ctx}/company/upload.do',
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
		
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
	<form id="companyUploadForm" method="post" style="margin: 10;text-align: center;" enctype="multipart/form-data">
		<table class="grid">
		<tr>
		<td>选择营业执照</td><td><input type="file" name="file1"><input name="id" id="id" type="hidden" value="${companyDto.id}"></td>
		</tr>
		<tr>
		<td>选择资质证书</td><td><input type="file" name="file2"></td>
		</tr>
		</table>
	</form>
	</div>
	</div>