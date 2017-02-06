package com.lianyg.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.lianyg.entity.base.IdEntity;

@Entity
@Table(name = "t_roleinfo")
public class Trole extends IdEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleid;
	private String name; // 角色名称
	private String description; // 备注
	private Set<Tresource> resources = new HashSet<Tresource>(0);
	private Set<Tuser> users = new HashSet<Tuser>(0);

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_roleresource", joinColumns = {
			@JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "resource_id", nullable = false, updatable = false) })
	@OrderBy("id ASC")
	public Set<Tresource> getResources() {
		return resources;
	}

	public void setResources(Set<Tresource> resources) {
		this.resources = resources;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_userrole", joinColumns = {
			@JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", nullable = false, updatable = false) })
	@OrderBy("id ASC")
	public Set<Tuser> getUsers() {
		return users;
	}

	public void setUsers(Set<Tuser> users) {
		this.users = users;
	}

}
