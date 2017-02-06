package com.lianyg.web;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lianyg.dto.CompanyDto;
import com.lianyg.dto.base.Grid;
import com.lianyg.dto.base.Json;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.SessionInfo;
import com.lianyg.dto.base.Tree;
import com.lianyg.framework.constant.GlobalConstant;
import com.lianyg.manager.CompanyService;
import com.lianyg.web.base.BaseController;

@Controller
@RequestMapping("/company")
@SessionAttributes("sessionInfo")
public class CompanyController extends BaseController {

	@Resource(name = "companyService")
	private CompanyService companyService;

	@RequestMapping(value = "/companylist", method = RequestMethod.GET)
	public String list(Model model) throws Exception {
		return "company/companylist";
	}

	/**
	 * Get company list
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/companyQuery", method = RequestMethod.POST)
	@ResponseBody
	public Grid getCompanyList(HttpServletRequest request, PageFilter ph, CompanyDto company) throws Exception {
		SessionInfo session = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		company.setCompanyId(session.getCompanyid());
		Grid grid = new Grid();
		grid.setRows(companyService.dataGrid(company, ph));
		grid.setTotal(companyService.count(company, ph));

		return grid;

	}

	@RequestMapping(value = "/companySave")
	public String popWindowSave() throws Exception {
		return "company/companySave";
	}

	/**
	 * Go to company pop up window
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/companyUpdate")
	public String popWindowUpdate(HttpServletRequest request, Long id) throws Exception {
		CompanyDto companyDto = companyService.get(id);
		request.setAttribute("companyDto", companyDto);
		return "company/companyUpdate";
	}

	@RequestMapping(value = "/companyAudit")
	public String popWindowAudit(HttpServletRequest request, Long id) throws Exception {
		CompanyDto companyDto = companyService.get(id);
		request.setAttribute("companyDto", companyDto);
		return "company/companyAudit";
	}

	@RequestMapping(value = "/companyUpload")
	public String popWindowUpload(HttpServletRequest request, Long id) throws Exception {
		CompanyDto companyDto = new CompanyDto();
		companyDto.setId(id);
		request.setAttribute("companyDto", companyDto);
		return "company/companyUpload";
	}

	/**
	 * 取行企业下拉框值
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/companySelect")
	public void getCompanyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String resultList = companyService.getCompanyList();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		if (!StringUtils.isBlank(resultList)) {
			// 向前台传递转成json格式的行政区域信息
			out.write(resultList);
		} else {
			out.write("[]");
		}
	}

	/**
	 * Add company
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Json add(CompanyDto companyDto) throws Exception {
		Json j = new Json();
		String companyId = companyDto.getCompanyId();
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		String dateString = format.format(currentDate);
		companyDto.setUpdatetime(dateString);
		try {
			if (companyService.count(companyId) > 0) {
				j.setSuccess(false);
				j.setMsg("企业ID已经存在！");
			} else {
				companyService.add(companyDto);
				j.setSuccess(true);
				j.setMsg("添加成功！");
			}

		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * Add company
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Json update(CompanyDto companyDto) throws Exception {
		Json j = new Json();
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		String dateString = format.format(currentDate);
		companyDto.setUpdatetime(dateString);
		try {
			companyService.edit(companyDto);
			j.setSuccess(true);
			j.setMsg("企业信息更新成功！");

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
			companyService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> tree(HttpServletRequest request) {
		return companyService.tree();
	}

}
