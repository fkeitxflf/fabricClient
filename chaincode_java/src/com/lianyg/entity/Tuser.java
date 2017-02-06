package com.lianyg.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.lianyg.entity.base.IdEntity;

@Entity
@Table(name = "t_userinfo")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Tuser extends IdEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1867623281523381449L;

	private String loginname; // 登录名
	private String password; // 密码
	private String name; // 姓名
	private String createdatetime; // 创建时间
	private String telephone;
	private String companyId;
	private Set<Trole> roles = new HashSet<Trole>(0);

	public Tuser() {
		super();
	}

	public Tuser(String loginname, String password, String name, String companyId, String districtId,
			String createdatetime, Integer usertype, Integer isdefault, Integer state) {
		super();
		this.loginname = loginname;
		this.password = password;
		this.name = name;
		this.createdatetime = createdatetime;
		this.companyId = companyId;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_userrole", joinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", nullable = false, updatable = false) })
	public Set<Trole> getRoles() {
		return roles;
	}

	public void setRoles(Set<Trole> roles) {
		this.roles = roles;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}