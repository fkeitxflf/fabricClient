<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	var companyTree;
	$(function() {
		companyTree = $('#companyTree').tree({
			url : '${ctx}/company/tree.do',
			parentField : 'pid',
			lines : true,
			checkbox : true,
			onClick : function(node) {
			},
			onLoadSuccess : function(node, data) {
				progressLoad();
				$.post( '${ctx}/companygroup/get.do', {
					id : '${companyGroupDto.id}'
				}, function(result) {
					var ids;
					if (result.companyIds != undefined&&result.companyIds!= undefined) {
						ids = $.stringToList(result.companyIds);
					}
					if (ids.length > 0) {
						for ( var i = 0; i < ids.length; i++) {
							if (companyTree.tree('find', ids[i])) {
								companyTree.tree('check', companyTree.tree('find', ids[i]).target);
							}
						}
					}
				}, 'json');
				progressClose();
			},
			cascadeCheck : false
		});

		$('#companytreeFormUpdate').form({
			url : '${ctx}/companygroup/update.do',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				var checknodes = companyTree.tree('getChecked');
				var ids = [];
				var names = [];
				if (checknodes) {
					if (checknodes.length < 2) {
						alert('请选择至少2个或以上的企业');
						progressClose();
						return false;
					} else {
						for ( var i = 0; i < checknodes.length; i++) {
							ids.push(checknodes[i].id);
							names.push(checknodes[i].text);
						}
					}
					$('#companyIds').val(ids);
					$('#companyNames').val(names);
				} else {
					alert('请选择至少2个或以上的企业');
					progressClose();
					return false;
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

</script>
<div id="roleGrantLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'west'" title="企业列表" style="width: 400px; padding: 1px;">
		<div class="well well-small">
			<form id="companytreeFormUpdate" method="post">
			<input name="id" type="hidden"  value="${companyGroupDto.id}" readonly="readonly">
				<ul id="companyTree"></ul>
				<input id="companyIds" name="companyIds" type="hidden" />
				<input id="companyNames" name="companyNames" type="hidden" />
			</form>
		</div>
	</div>
</div>