<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
  <head>
  <jsp:include page="../taglibs.jsp"></jsp:include>
  <meta http-equiv="X-UA-Compatible" content="edge" />
  <c:if test="${fn:contains(sessionInfo.resourceList, '/company/update.do')}">
	<script type="text/javascript">
		$.canEdit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/company/delete.do')}">
	<script type="text/javascript">
	$.canDelete = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/company/audit.do')}">
	<script type="text/javascript">
	$.canAudit = true;
	</script>
</c:if>
<c:if test="${fn:contains(sessionInfo.resourceList, '/company/upload.do')}">
	<script type="text/javascript">
	$.canUpload = true;
	</script>
</c:if>
  <title>企业列表</title>
  <script type="text/javascript">
  var dataGrid;
  
	jQuery(function($){
		dataGrid = $('#dataGrid').datagrid({
			url:"${ctx}/company/companyQuery.do",
			singleSelect : true,
			fit : true,
			striped: true,
			collapsible:true,
			sortName: 'id',
			sortOrder: 'asc',
			idField:'id', //主键字段
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			pagination:true, //显示分页
			rownumbers:true, //显示行号
			columns:[[
			    //{field : 'id',title:'id',width:'80',sortable:true},
				{field:'companyId',title:'企业ID',width:80,sortable:true},
				{field:'companyName',title:'企业名称',width:180,sortable:true},
				{field:'manager',title:'企业法人',width:80,sortable:true},
				{field:'managerTel',title:'法人电话',width:100,sortable:true},
				{field:'type',title:'企业类型',width:80,sortable:true,formatter:function(value, row, index) {
					switch (value) {
					case '0':
						return '核心企业';
					case '1':
						return '融资企业';
					}
				}},
				{
				field : 'action',
				title : '操作',
				width : 150,
				formatter : function(value, row, index) {
					var str = '';
					if(row.isdefault!=0){
						if ($.canEdit) {
							str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\', \'{1}\');" >编辑</a>', row.id, row.status);
						}
						if ($.canDelete) {
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						}
					}
					return str;
				}
			}
			]],
			toolbar:'#toolbar',
			onLoadSuccess:function(){
				$('#dataGrid').datagrid('clearSelections');
			}
		});
	});
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加企业',
			width : 500,
			height : 200,
			href : '${ctx}/company/companySave.do',
			buttons : [ {
				text : '添加企业',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#companyAddForm');
					f.submit();
				}
			} ]
		});
	}
	
	function editFun(id, status) {
		
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
			status = row[0].status;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		if (status == '1') {
			parent.$.messager.show({
				title : '提示',
				msg : '不可以编辑已备案企业！'
			});
		} else {
			parent.$.modalDialog({
				title : '编辑企业',
				width : 500,
				height : 200,
				href : '${ctx}/company/companyUpdate.do?id=' + id,
				buttons : [ {
					text : '编辑企业',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#companyFormUpdate');
						f.submit();
					}
				} ]
			});
		}
	}
	
	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要删除当前企业', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/company/delete.do', {
					id : id
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert('提示', result.msg, 'info');
						dataGrid.datagrid('reload');
					}
					progressClose();
				}, 'JSON');
			}
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
					<th>企业名称:</th>
					<td><input name="companyName" id="companyName"/></td>
					<th>企业法人:</th>
					<td><input name="manager" id="manager"/></td>
					<th></th>
					<td>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:true,title:'企业列表'">
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	
	<div id="toolbar" style="display: none;">
		<c:if test="${fn:contains(sessionInfo.resourceList, '/company/add.do')}">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
		</c:if>
	</div>
	</body>
</html>
