<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
  <head>
  <jsp:include page="../taglibs.jsp"></jsp:include>
  <meta http-equiv="X-UA-Compatible" content="edge" />
  <title>企业组列表</title>
  <script type="text/javascript">
  var dataGrid;
  
	jQuery(function($){
		dataGrid = $('#dataGrid').datagrid({
			url:"${ctx}/companygroup/companyGroupQuery.do",
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
			    //{field : 'id',title:'',width:'80',sortable:true},
				//{field:'companyIds',title:'企业ID',width:80,sortable:true},
				{field:'companyNames',title:'企业组',width:800,sortable:true},
				
			]],
			frozenColumns:[[
				{
					field : 'action',
					title : '操作',
					width : 150,
					formatter : function(value, row, index) {
						var str = '';
						if(row.isdefault!=0){
								str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >编辑</a>', row.id);
								str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
								str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
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
			title : '添加企业组',
			width : 420,
			height : 520,
			href : '${ctx}/companygroup/companygroupSave.do',
			buttons : [ {
				text : '添加企业组',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#companytreeForm');
					f.submit();
				}
			} ]
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
				title : '编辑企业组',
				width : 420,
				height : 520,
				href : '${ctx}/companygroup/companygroupUpdate.do?id=' + id,
				buttons : [ {
					text : '编辑企业组',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#companytreeFormUpdate');
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
		parent.$.messager.confirm('询问', '您是否要删除当前企业组', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/companygroup/delete.do', {
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
	</script>	
  </head>
    <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:true,title:'企业列表'">
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	
	<div id="toolbar" style="display: none;">
		<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
	</div>
	</body>
</html>
