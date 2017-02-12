package com.lianyg.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.lianyg.dao.BaseDaoI;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.Tree;
import com.lianyg.dto.sys.RoleDto;
import com.lianyg.entity.Tresource;
import com.lianyg.entity.Trole;
import com.lianyg.manager.RoleServiceI;

@Service
public class RoleServiceImpl implements RoleServiceI {

	private BaseDaoI<Trole> roleDao;

	private BaseDaoI<Tresource> resourceDao;

	public BaseDaoI<Trole> getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(BaseDaoI<Trole> roleDao) {
		this.roleDao = roleDao;
	}

	public BaseDaoI<Tresource> getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(BaseDaoI<Tresource> resourceDao) {
		this.resourceDao = resourceDao;
	}

	@Override
	public RoleDto get(Long id) {
		Trole t = roleDao.get(Trole.class, id);
		RoleDto r = new RoleDto();
		r.setDescription(t.getDescription());
		r.setId(t.getId());
		r.setName(t.getName());
		Set<Tresource> s = t.getResources();
		if ((s != null) && !s.isEmpty()) {
			boolean b = false;
			String ids = "";
			String names = "";
			for (Tresource tr : s) {
				if (b) {
					ids += ",";
					names += ",";
				} else {
					b = true;
				}
				ids += tr.getId();
				names += tr.getName();
			}
			r.setResourceIds(ids);
			r.setResourceNames(names);
		}
		return r;
	}

	@Override
	public List<RoleDto> dataGrid(RoleDto role, PageFilter ph) {
		List<RoleDto> ul = new ArrayList<RoleDto>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Trole t ";
		List<Trole> l = roleDao.find(hql + whereHql(role, params) + orderHql(ph), params, ph.getPage(), ph.getRows());
		for (Trole t : l) {
			RoleDto u = new RoleDto();
			BeanUtils.copyProperties(t, u);
			ul.add(u);
		}
		return ul;
	}

	@Override
	public Long count(RoleDto role, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Trole t ";
		return roleDao.count("select count(*) " + hql + whereHql(role, params), params);
	}

	private String whereHql(RoleDto role, Map<String, Object> params) {
		String hql = "";
		if (role != null) {
			hql += " where 1=1 ";
			if (role.getName() != null) {
				hql += " and t.name like :name";
				params.put("name", "%%" + role.getName() + "%%");
			}
		}
		return hql;
	}

	private String orderHql(PageFilter ph) {
		String orderString = "";
		if ((ph.getSort() != null) && (ph.getOrder() != null)) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	public void grant(RoleDto role) {
		Trole t = roleDao.get(Trole.class, role.getId());
		if ((role.getResourceIds() != null) && !role.getResourceIds().equalsIgnoreCase("")) {
			String ids = "";
			boolean b = false;
			for (String id : role.getResourceIds().split(",")) {
				if (b) {
					ids += ",";
				} else {
					b = true;
				}
				ids += id;
			}
			t.setResources(new HashSet<Tresource>(
					resourceDao.find("select distinct t from Tresource t where t.id in (" + ids + ")")));
		} else {
			t.setResources(null);
		}
	}

	@Override
	public List<Tree> tree() {
		List<Trole> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		l = roleDao.find("select distinct t from Trole t");

		if ((l != null) && (l.size() > 0)) {
			for (Trole r : l) {
				Tree tree = new Tree();
				tree.setId(r.getId().toString());
				tree.setText(r.getName());
				lt.add(tree);
			}
		}
		return lt;
	}
}
