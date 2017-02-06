package com.lianyg.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.lianyg.entity.base.IdEntity;

/**
 * 
 * @author hayuan
 */
@Entity
@Table(name = "t_company")
public class Company extends IdEntity implements java.io.Serializable

{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String companyId;

	private String companyName;

	private String companyAddress;

	private String manager;

	private String managerTel;

	private String type;

	private String note;

	private String icon;

	private String updatetime;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManagerTel() {
		return managerTel;
	}

	public void setManagerTel(String managerTel) {
		this.managerTel = managerTel;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}