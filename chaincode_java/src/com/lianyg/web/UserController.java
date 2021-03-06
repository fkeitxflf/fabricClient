package com.lianyg.web;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.service.spi.ServiceException;
import org.hyperledger.fabric.sdk.Chain;
import org.hyperledger.fabric.sdk.Member;
import org.hyperledger.fabric.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianyg.dto.base.Grid;
import com.lianyg.dto.base.Json;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.SessionInfo;
import com.lianyg.dto.sys.UserDto;
import com.lianyg.framework.constant.GlobalConstant;
import com.lianyg.manager.UserServiceI;
import com.lianyg.web.base.BaseController;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private UserServiceI userService;

	@RequestMapping("/manager")
	public String manager() {
		return "user/user";
	}

	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(UserDto user, PageFilter ph) {
		Grid grid = new Grid();
		grid.setRows(userService.dataGrid(user, ph));
		grid.setTotal(userService.count(user, ph));
		return grid;
	}

	@RequestMapping("/editPwdPage")
	public String editPwdPage() {
		return "user/userEditPwd";
	}

	@RequestMapping("/editUserPwd")
	@ResponseBody
	public Json editUserPwd(HttpServletRequest request, String oldPwd, String pwd) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		try {
			userService.editUserPwd(sessionInfo, oldPwd, pwd);
			j.setSuccess(true);
			j.setMsg("密码修改成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/addPage")
	public String addPage() {
		return "user/userAdd";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Json add(HttpServletRequest request, UserDto user) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		UserDto u = userService.getByLoginName(user);
		if (u != null) {
			j.setMsg("用户名已存在!");
		} else {
			try {
				// 用户向区块链上注册
				Chain testChain = sessionInfo.getChain();
				Member member = testChain.getMember(user.getLoginname());
				if (!member.isEnrolled()) {
					RegistrationRequest req = createRegistrationRequest(member.getName(), "bank_a");
					member.register(req);
					member.enroll(member.getEnrollmentSecret());

				} else {
					j.setSuccess(false);
					j.setMsg("此用户已经在区块链上登记过,不能重复登记");
				}
				user.setPassword(member.getEnrollmentSecret());
				userService.add(user);
				j.setSuccess(true);
				j.setMsg("添加成功！");
			} catch (Exception e) {
				j.setMsg(e.getMessage());
			}

		}
		return j;
	}

	private RegistrationRequest createRegistrationRequest(String enrollmentId, String affiliationId) {
		RegistrationRequest req = new RegistrationRequest();
		req.setAffiliation(affiliationId);
		req.setEnrollmentID(enrollmentId);
		return req;
	}

	@RequestMapping("/get")
	@ResponseBody
	public UserDto get(Long id) {
		return userService.get(id);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(Long id) {
		Json j = new Json();
		try {
			userService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, Long id) {
		UserDto u = userService.get(id);
		request.setAttribute("user", u);
		return "user/userEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(UserDto user) {
		Json j = new Json();
		try {
			userService.edit(user);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (ServiceException e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

}
