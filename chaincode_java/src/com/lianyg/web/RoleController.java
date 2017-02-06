package com.lianyg.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianyg.dto.base.Grid;
import com.lianyg.dto.base.Json;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.Tree;
import com.lianyg.dto.sys.RoleDto;
import com.lianyg.manager.RoleServiceI;
import com.lianyg.web.base.BaseController;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleServiceI roleService;

	@RequestMapping("/manager")
	public String manager() {
		return "user/role";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(RoleDto role, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(roleService.dataGrid(role, ph));
		grid.setTotal(roleService.count(role, ph));
		return grid;
	}

	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> tree() {
		return roleService.tree();
	}

	@RequestMapping("/get")
	@ResponseBody
	public RoleDto get(Long id) {
		return roleService.get(id);
	}

	@RequestMapping("/grantPage")
	public String grantPage(HttpServletRequest request, Long id) {
		RoleDto r = roleService.get(id);
		request.setAttribute("role", r);
		return "user/roleGrant";
	}

	@RequestMapping("/grant")
	@ResponseBody
	public Json grant(RoleDto role) {
		Json j = new Json();
		try {
			roleService.grant(role);
			j.setMsg("授权成功");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

}
