package com.lianyg.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianyg.dto.base.Json;
import com.lianyg.dto.base.SessionInfo;
import com.lianyg.dto.base.Tree;
import com.lianyg.dto.sys.ResourceDto;
import com.lianyg.framework.constant.GlobalConstant;
import com.lianyg.manager.ResourceServiceI;
import com.lianyg.web.base.BaseController;

@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {

	@Autowired
	private ResourceServiceI resourceService;

	@RequestMapping("/manager")
	public String manager() {
		return "user/resource";
	}

	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> tree(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		return resourceService.tree(sessionInfo);
	}

	@RequestMapping("/allTree")
	@ResponseBody
	public List<Tree> allTree(boolean flag) {// true��ȡȫ����Դ,falseֻ��ȡ�˵���Դ
		return resourceService.listAllTree(flag);
	}

	@RequestMapping("/treeGrid")
	@ResponseBody
	public List<ResourceDto> treeGrid() {
		return resourceService.treeGrid();
	}

	@RequestMapping("/get")
	@ResponseBody
	public ResourceDto get(Long id) {
		return resourceService.get(id);
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, Long id) {
		ResourceDto r = resourceService.get(id);
		request.setAttribute("resource", r);
		return "user/resourceEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(ResourceDto resource) throws InterruptedException {
		Json j = new Json();
		try {
			resourceService.edit(resource);
			j.setSuccess(true);
			j.setMsg("编辑成功");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(Long id) {
		Json j = new Json();
		try {
			resourceService.delete(id);
			j.setMsg("删除成功");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/addPage")
	public String addPage() {
		return "user/resourceAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(ResourceDto resource) {
		Json j = new Json();
		try {
			resourceService.add(resource);
			j.setSuccess(true);
			j.setMsg("添加成功");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

}
