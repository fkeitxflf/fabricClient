<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../taglibs.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<c:if test="${fn:contains(sessionInfo.resourceList, '/user/edit')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/user/delete')}">
	<script type="text/javascript">
	$.canDelete = true;
	</script>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
	<script type="text/javascript">
	var dataGrid;
	var organizationTree;
	$(function() {
	
		organizationTree = $('#organizationTree').tree({
			url : '${ctx}/company/tree.do',
			lines : true,
			onClick : function(node) {
				if ($('#organizationTree').tree('isLeaf', node.target)) {
					dataGrid.datagrid('load', {
					    companyId: node.id
					});
				} else {
					dataGrid.datagrid('load', {
					    districtId: node.id
					});
				}
			}
		});
	
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/user/dataGrid.do',
			fit : true,
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			sortName : 'createdatetime',
			sortOrder : 'asc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			columns : [ [ 
			              {width : '80',title : '登录名',field : 'loginname',sortable : true}, 
			              {width : '80',title : '姓名',field : 'name',sortable : true},
			              {width : '150',title : '密钥',field : 'password',sortable : true},
			              {width : '80',title : '企业ID',field : 'companyId',hidden : true},
			              {width : '150',title : '企业名',field : 'companyName'},
						  {width : '100',title : '电话',field : 'telephone',sortable : true},
						  {width : '100',title : '用户角色',field : 'roleNames',sortable : true},
						  {field : 'action',title : '操作',width : 100,formatter : function(value, row, index) {
							var str = '';
							if(row.isdefault!=0){
								if ($.canEdit) {
									str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.id);
								}
								if ($.canDelete) {
								str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
									str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
								}
							}
							return str;
				}
			}] ],
			toolbar : '#toolbar'
		});
	});
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 600,
			height : 300,
			href : '${ctx}/user/addPage.do',
			buttons : [ {
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#userAddForm');
					f.submit();
				}
			} ]
		});
	}
	
	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要删除当前用户？', function(b) {
			if (b) {
				var currentUserId = '${sessionInfo.id}';/*当前登录用户的ID*/
				if (currentUserId != id) {
					progressLoad();
					$.post('${ctx}/user/delete.do', {
						id : id
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('reload');
						}
						progressClose();
					}, 'JSON');
				} else {
					parent.$.messager.show({
						title : '提示',
						msg : '不可以删除自己！'
					});
				}
			}
		});
	}
	
	function editFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '编辑',
			width : 600,
			height : 300,
			href : '${ctx}/user/editPage.do?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#userEditForm');
					f.submit();
				}
			} ]
		});
	}
	
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}
	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
		<form id="searchForm">
			<table>
				<tr>
					<th>姓名:</th>
					<td><input name="name" placeholder="请输入用户姓名"/></td>
					<th></th>
					<td>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:true,title:'用户列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<div data-options="region:'west',border:true,split:false,title:'组织机构'"  style="width:240px;overflow: auto; ">
		<ul id="organizationTree"  style="width:160px;margin: 10px 10px 10px 10px">
		</ul>
	</div>
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/user/add')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</c:if>
	</div>
</body>
</html>