<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
/*
jQuery(function($){
	// 初始化行政区域下拉框
	$('#loanCpId').combobox({
		url: "${ctx}/company/companylist.do?type=" + "1",
		valueField:'loanCpId',
		textField:'loanCpName',
		method:'get',
		fitColumns: true,
		striped: true,
		editable:false,
	});
}
);
*/
		
jQuery(function() {

	$('#loanAddForm').form({
		url : '${pageContext.request.contextPath}/loan/add.do',
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
	<form id="loanAddForm" method="post" style="margin: 10;text-align: center;">
		<table class="grid">
		<tr>
		<td>贷款企业：</td><td><input name="loanCpId" id="loanCpId" data-options="required:true"></td>
		<td>贷款银行：</td>
		<td>
			<select id="bank" name="bank" style="width:140px;"> 
				<option value="CCB">中国建设银行</option>
				<option value="ICBC">中国工商银行</option>
				<option value="CMB">招商银行</option>
				<option value="BOC">中国银行</option>
			</select>
		</td>
		</tr>
		<tr>
		<td>贷款金额：</td><td><input name="amount" class="easyui-numberbox" type="text" data-options="precision:2"></td>
		</tr>
		</table>
	</form>
	</div>
	</div>
