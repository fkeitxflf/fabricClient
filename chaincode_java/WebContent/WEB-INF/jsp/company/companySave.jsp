<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
		jQuery(function($){
			// init the district
			$('#type').combobox({
				// get the value and input companyId
				onSelect: function() {
					var value = $('#type').combobox('getValue');
					if (value == 0) {
						$('#companyId').val('HX');
					} else {
						$('#companyId').val('RZ');
					}
				}
			});
		});
		
		jQuery(function() {

			$('#companyAddForm').form({
				url : '${pageContext.request.contextPath}/company/add.do',
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
		
		$('#companyId').val('HX');
		
	</script>
	<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
	<form id="companyAddForm" method="post" style="margin: 10;text-align: center;">
		<input type="hidden" name="districtName" id="districtName">
		<table class="grid">
		<tr>
		<td>企业类型：</td>
		<td>
			<select id="type" name="type" style="width:140px;"> 
				<option value="0">核心企业</option>
				<option value="1">融资企业</option>
			</select>
		</td>
		<td>企业标识：</td><td><input id= "companyId" name="companyId" class="easyui-validatebox" data-options="required:true,validType:'maxLength[5]'"></td>
		</tr>
		<tr>
		<td>企业名称：</td><td><input name="companyName" class="easyui-validatebox" data-options="required:true"></td>
		<td>企业法人：</td><td><input name="manager" ></input></td>
		</tr>
		<tr>
		<td>法人电话：</td><td><input name="managerTel" class="easyui-validatebox" data-options="validType:'phoneRex'"></td>
		</tr>
		</table>
	</form>
	</div>
	</div>
