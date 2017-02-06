package com.lianyg.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hyperledger.fabric.sdk.Chain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianyg.dto.base.Json;
import com.lianyg.dto.base.SessionInfo;
import com.lianyg.dto.sys.UserDto;
import com.lianyg.framework.constant.GlobalConstant;
import com.lianyg.manager.ResourceServiceI;
import com.lianyg.manager.UserServiceI;
import com.lianyg.util.CommonUtil;

/**
 * @author hayuan
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

	Chain testChain = null;

	@Resource(name = "userService")
	private UserServiceI userService;

	@Resource(name = "resourceService")
	private ResourceServiceI resourceService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		if ((sessionInfo != null) && (sessionInfo.getId() != null)) {
			return "/main";
		}
		return "/login";
	}

	@RequestMapping("/register")
	public String register(HttpServletRequest request) {
		return "admin/index";
	}

	@ResponseBody
	@RequestMapping("/login")
	public Json login(UserDto user, HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		testChain = new Chain("chain1");

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		Json j = new Json();

		// TODO:Set the password temporary
		if ("admin".equals(password) && "admin".equals(userName)) {
			password = "Xurw3yU9zI0l";
		} else {
			// TODO:
			j.setSuccess(false);
			j.setMsg("用户校验失败");
			return j;
		}
		// TODO:
		// try {
		// testChain.setMemberServicesUrl("grpc://114.215.169.63:7054", null);
		// testChain.setKeyValStore(new
		// FileKeyValStore(System.getProperty("D:/test.properties")));
		// testChain.addPeer("grpc://114.215.169.63:7051", null);
		// Member registrar = testChain.getMember(userName);
		// if (!registrar.isEnrolled()) {
		// registrar = testChain.enroll(userName, password);
		// }
		// testChain.setRegistrar(registrar);
		// } catch (CertificateException | EnrollmentException cex) {
		// j.setSuccess(false);
		// // j.setMsg(cex.getMessage());
		// j.setMsg("用户没有在区块链上校验,验证失败");
		// return j;
		// }
		//
		j.setSuccess(true);
		// j.setMsg("用户在成员管理模块注册通过,可以登录!");

		UserDto user1 = userService.getUserbyUid(userName);

		if (null != user1) {
			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setId(user1.getId());
			sessionInfo.setLoginname(userName);
			sessionInfo.setPassword(password);
			sessionInfo.setCompanyid(user1.getCompanyId());
			sessionInfo.setRoleid(user1.getRoleIds());
			sessionInfo.setName(user1.getName());
			sessionInfo.setResourceList(userService.listResource(user1.getId()));
			sessionInfo.setResourceAllList(resourceService.listAllResource());
			session.setAttribute(GlobalConstant.SESSION_INFO, sessionInfo);
		}

		return j;

	}

	@ResponseBody
	@RequestMapping("/logout")
	public Json logout(HttpSession session) {
		Json j = new Json();
		if (session != null) {
			session.invalidate();
		}
		j.setSuccess(true);
		j.setMsg("注销成功！");
		return j;
	}

	@RequestMapping(value = "/download")
	@ResponseBody
	public String download(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + "K1_Setup.exe");

		try {
			String path = CommonUtil.readProperties("download.path");
			String filepath = path + "K1_Setup.exe";
			InputStream inputStream = new FileInputStream(new File(filepath));

			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}

			// 这里主要关闭。
			os.close();

			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}