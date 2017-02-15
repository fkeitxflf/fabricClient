package com.lianyg.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hyperledger.fabric.sdk.Chain;
import org.hyperledger.fabric.sdk.ChainCodeResponse;
import org.hyperledger.fabric.sdk.ChaincodeLanguage;
import org.hyperledger.fabric.sdk.DeployRequest;
import org.hyperledger.fabric.sdk.InvokeRequest;
import org.hyperledger.fabric.sdk.Member;
import org.hyperledger.fabric.sdk.RegistrationRequest;
import org.hyperledger.fabric.sdk.exception.ChainCodeException;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.EnrollmentException;
import org.hyperledger.fabric.sdk.exception.NoAvailableTCertException;
import org.hyperledger.fabric.sdk.exception.RegistrationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lianyg.dto.LoanDto;
import com.lianyg.dto.base.Grid;
import com.lianyg.dto.base.Json;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.SessionInfo;
import com.lianyg.framework.constant.GlobalConstant;
import com.lianyg.manager.LoanService;
import com.lianyg.web.base.BaseController;

@Controller
@RequestMapping("/loan")
@SessionAttributes("sessionInfo")
public class LoanController extends BaseController {

	@Resource(name = "loanService")
	private LoanService loanService;

	static ChainCodeResponse deployResponse = null;
	static ChainCodeResponse javaDeployResponse = null;

	@RequestMapping(value = "/loanlist", method = RequestMethod.GET)
	public String list(Model model) throws Exception {
		return "loan/loanlist";
	}

	/**
	 * Get loan list
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanQuery", method = RequestMethod.POST)
	@ResponseBody
	public Grid getCompanyList(HttpServletRequest request, PageFilter ph, LoanDto loan) throws Exception {
		SessionInfo session = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		loan.setCoreCpId(session.getCompanyid());
		Grid grid = new Grid();
		grid.setRows(loanService.dataGrid(loan, ph));
		grid.setTotal(loanService.count(loan, ph));

		return grid;

	}

	@RequestMapping(value = "/loanSave")
	public String popWindowSave(HttpServletRequest request) throws Exception {
		return "loan/loanSave";
	}

	/**
	 * Go to company pop up window
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanUpdate")
	public String popWindowUpdate(HttpServletRequest request, Long id) throws Exception {
		LoanDto loanDto = loanService.get(id);
		request.setAttribute("loanDto", loanDto);
		return "loan/loanUpdate";
	}

	@RequestMapping(value = "/loanWrite")
	public String popWindowAudit(HttpServletRequest request, Long id) throws Exception {
		LoanDto loanDto = loanService.get(id);
		request.setAttribute("loanDto", loanDto);
		return "company/companyAudit";
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
	public Json add(HttpServletRequest request, LoanDto loanDto) throws Exception {
		SessionInfo session = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Chain testChain = session.getChain();
		testChain.eventHubConnect("grpc://114.215.169.63:7050", null);
		deployResponse = deploy(testChain, session.getLoginname());
		TimeUnit.SECONDS.sleep(10);

		writeApplication(testChain, session.getLoginname());

		Json j = new Json();
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		String dateString = format.format(currentDate);
		loanDto.setCoreCpId(session.getCompanyid());
		loanDto.setUpdatetime(dateString);
		try {

			loanService.add(loanDto);
			j.setSuccess(true);
			j.setMsg("添加成功！");

		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	public static ChainCodeResponse deploy(Chain testChain, String loginname) throws RegistrationException,
			EnrollmentException, ChainCodeException, NoAvailableTCertException, CryptoException, IOException {
		DeployRequest request = new DeployRequest();
		request.setChaincodePath("github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02");
		request.setArgs(new ArrayList<>(Arrays.asList("init", "a", "700", "b", "20000")));
		Member member = getMember(testChain, "test001", "bank_a");
		request.setChaincodeName("mycc");
		request.setChaincodeLanguage(ChaincodeLanguage.GO_LANG);
		request.setConfidential(true);
		return member.deploy(request);

		// DeployRequest request = new DeployRequest();
		// request.setChaincodePath("github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example_financing");
		// request.setArgs(new ArrayList<>(Arrays.asList("init", "This is a
		// financing test start")));
		// Member member = testChain.getMember(loginname);
		// request.setChaincodeName("mycc");
		// request.setChaincodeLanguage(ChaincodeLanguage.GO_LANG);
		// request.setConfidential(true);
		// return member.deploy(request);
	}

	private static Member getMember(Chain testChain, String enrollmentId, String affiliation)
			throws RegistrationException, EnrollmentException {
		Member member = testChain.getMember(enrollmentId);
		if (!member.isRegistered()) {
			RegistrationRequest registrationRequest = new RegistrationRequest();
			registrationRequest.setEnrollmentID(enrollmentId);
			registrationRequest.setAffiliation(affiliation);
			member = testChain.registerAndEnroll(registrationRequest);
		} else if (!member.isEnrolled()) {
			member = testChain.enroll(enrollmentId, member.getEnrollmentSecret());
		}
		return member;
	}

	public void writeApplication(Chain testChain, String loginname) throws RegistrationException, EnrollmentException,
			ChainCodeException, NoAvailableTCertException, CryptoException, IOException {
		InvokeRequest request = new InvokeRequest();
		request.setArgs(new ArrayList<>(Arrays.asList("invoke", "corecpid", "finacpid", "bankid", "500000")));
		request.setFcn("createFinancing");
		request.setChaincodeID(deployResponse.getChainCodeID());
		request.setChaincodeName(deployResponse.getChainCodeID());
		Member member = testChain.getMember(loginname);
		member.invoke(request);
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
	public Json update(LoanDto loanDto) throws Exception {
		Json j = new Json();
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		String dateString = format.format(currentDate);
		loanDto.setUpdatetime(dateString);
		try {
			loanService.edit(loanDto);
			j.setSuccess(true);
			j.setMsg("贷款信息更新成功！");

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
			loanService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

}
