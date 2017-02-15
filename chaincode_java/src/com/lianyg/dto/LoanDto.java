package com.lianyg.dto;

/**
 * 
 * @author hayuan
 */
public class LoanDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String coreCpId;

	private String coreCpIdName;

	private String loanCpId;

	private String loanCpName;

	private String bank;

	private String amount;

	private String status;

	private String updatetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoreCpId() {
		return coreCpId;
	}

	public void setCoreCpId(String coreCpId) {
		this.coreCpId = coreCpId;
	}

	public String getCoreCpIdName() {
		return coreCpIdName;
	}

	public void setCoreCpIdName(String coreCpIdName) {
		this.coreCpIdName = coreCpIdName;
	}

	public String getLoanCpId() {
		return loanCpId;
	}

	public void setLoanCpId(String loanCpId) {
		this.loanCpId = loanCpId;
	}

	public String getLoanCpName() {
		return loanCpName;
	}

	public void setLoanCpName(String loanCpName) {
		this.loanCpName = loanCpName;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}