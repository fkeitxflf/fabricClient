package com.lianyg.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.lianyg.dao.BaseDaoI;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.SessionInfo;
import com.lianyg.dto.sys.UserDto;
import com.lianyg.entity.Tresource;
import com.lianyg.entity.Trole;
import com.lianyg.entity.Tuser;
import com.lianyg.manager.UserServiceI;
import com.lianyg.util.CommonUtil;
import com.lianyg.util.MD5Util;

@Service
public class UserServiceImpl implements UserServiceI {

	private BaseDaoI<Tuser> userDao;

	private BaseDaoI<Trole> roleDao;

	public BaseDaoI<Tuser> getUserDao() {
		return userDao;
	}

	public void setUserDao(BaseDaoI<Tuser> userDao) {
		this.userDao = userDao;
	}

	public BaseDaoI<Trole> getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(BaseDaoI<Trole> roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public UserDto getUserbyUid(String username) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String fullQuery = "select role.roleid,user.id,user.loginname from t_userinfo user,t_roleinfo role, t_userrole userrole "
				+ "where user.id = userrole.user_id and role.id = userrole.role_id and user.loginname= :loginname";
		paramMap.put("loginname", username);
		List<Object[]> list = userDao.findBySql(fullQuery, paramMap);
		UserDto userDto = null;
		if (null != list && list.size() > 0) {
			Object userResult[] = (Object[]) list.get(0);
			userDto = new UserDto();
			userDto.setRoleIds(CommonUtil.isNullString(userResult[0]));
			userDto.setId(Long.parseLong(CommonUtil.isNullString(userResult[1])));
			userDto.setLoginname(CommonUtil.isNullString(userResult[2]));
		}
		return userDto;
	}

	@Override
	public void add(UserDto u) {
		Tuser t = new Tuser();
		BeanUtils.copyProperties(u, t);

		List<Trole> roles = new ArrayList<Trole>();
		if (u.getRoleIds() != null) {
			for (String roleId : u.getRoleIds().split(",")) {
				roles.add(roleDao.get(Trole.class, Long.valueOf(roleId)));
			}
		}
		t.setRoles(new HashSet<Trole>(roles));

		// t.setState(GlobalConstant.ENABLE);
		// t.setIsdefault(GlobalConstant.DEFAULT);
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		String dateString = format.format(currentDate);
		t.setCreatedatetime(dateString);
		userDao.save(t);
	}

	@Override
	public void delete(Long id) {
		Tuser t = userDao.get(Tuser.class, id);
		del(t);
	}

	private void del(Tuser t) {
		userDao.delete(t);
	}

	@Override
	public void edit(UserDto user) {
		Tuser t = userDao.get(Tuser.class, user.getId());
		t.setLoginname(user.getLoginname());
		t.setName(user.getName());
		List<Trole> roles = new ArrayList<Trole>();
		if (user.getRoleIds() != null) {
			for (String roleId : user.getRoleIds().split(",")) {
				roles.add(roleDao.get(Trole.class, Long.valueOf(roleId)));
			}
		}
		t.setRoles(new HashSet<Trole>(roles));
		t.setCompanyId(user.getCompanyId());
		t.setTelephone(user.getTelephone());
		userDao.update(t);
	}

	@Override
	public UserDto get(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tuser t = userDao.get("from Tuser t  left join fetch t.roles role where t.id = :id", params);
		UserDto u = new UserDto();
		BeanUtils.copyProperties(t, u);

		if ((t.getRoles() != null) && !t.getRoles().isEmpty()) {
			String roleIds = "";
			String roleNames = "";
			boolean b = false;
			for (Trole role : t.getRoles()) {
				if (b) {
					roleIds += ",";
					roleNames += ",";
				} else {
					b = true;
				}
				roleIds += role.getId();
				roleNames += role.getName();
			}
			u.setRoleIds(roleIds);
			u.setRoleNames(roleNames);
		}
		return u;
	}

	@Override
	public UserDto login(UserDto user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", user.getLoginname());
		params.put("password", MD5Util.md5(user.getPassword()));
		Tuser t = userDao.get("from Tuser t where t.loginname = :loginname and t.password = :password", params);
		if (t != null) {
			UserDto u = new UserDto();
			BeanUtils.copyProperties(t, u);
			return u;
		}
		return null;
	}

	@Override
	public List<String> listResource(Long id) {
		List<String> resourceList = new ArrayList<String>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tuser t = userDao.get(
				"from Tuser t join fetch t.roles role join fetch role.resources resource where t.id = :id", params);
		if (t != null) {
			Set<Trole> roles = t.getRoles();
			if ((roles != null) && !roles.isEmpty()) {
				for (Trole role : roles) {
					Set<Tresource> resources = role.getResources();
					if ((resources != null) && !resources.isEmpty()) {
						for (Tresource resource : resources) {
							if ((resource != null) && (resource.getUrl() != null)) {
								resourceList.add(resource.getUrl());
							}
						}
					}
				}
			}
		}
		return resourceList;
	}

	@Override
	public List<UserDto> dataGrid(UserDto user, PageFilter ph) {
		List<UserDto> resultList = new ArrayList<UserDto>();
		Map<String, Object> params = new HashMap<String, Object>();
		String fullQuery = "select user.loginname,user.password,user.companyId,role.name as roleName,"
				+ "company.companyName, user.id, user.name, user.telephone from t_userinfo user left join t_company company on "
				+ "user.companyId = company.companyId, t_roleinfo role, t_userrole userrole where user.id = userrole.user_id and role.id = userrole.role_id ";
		List<Object[]> list = userDao.findBySql(fullQuery + whereHql(user, params), params, ph.getPage(), ph.getRows());
		UserDto userDto = null;
		for (int i = 0; i < list.size(); i++) {
			if (null != list) {
				Object userResult[] = (Object[]) list.get(i);
				userDto = new UserDto();
				userDto.setLoginname(CommonUtil.isNullString(userResult[0]));
				userDto.setPassword(CommonUtil.isNullString(userResult[1]));
				userDto.setCompanyId(CommonUtil.isNullString(userResult[2]));
				userDto.setRoleNames(CommonUtil.isNullString(userResult[3]));
				userDto.setCompanyName(CommonUtil.isNullString(userResult[4]));
				userDto.setId(Long.parseLong(CommonUtil.isNullString(userResult[5])));
				userDto.setName(CommonUtil.isNullString(userResult[6]));
				userDto.setTelephone(CommonUtil.isNullString(userResult[7]));

				resultList.add(userDto);
			}
		}
		return resultList;
	}

	@Override
	public Long count(UserDto user, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Tuser user where 1=1 ";
		return userDao.count("select count(*) " + hql + whereHql(user, params), params);
	}

	private String whereHql(UserDto user, Map<String, Object> params) {
		String sql = "";
		if (user != null) {
			if (StringUtils.isNotBlank(user.getName())) {
				sql += " and user.name like :name";
				params.put("name", "%" + user.getName() + "%");
			}

			if (StringUtils.isNotBlank(user.getCompanyId())) {
				sql += " and user.companyId = :companyId";
				params.put("companyId", user.getCompanyId());
			}
		}
		return sql;
	}

	@Override
	public boolean editUserPwd(SessionInfo sessionInfo, String oldPwd, String pwd) {
		Tuser u = userDao.get(Tuser.class, sessionInfo.getId());
		if (u.getPassword().equalsIgnoreCase(MD5Util.md5(oldPwd))) {// 说明原密码输入正确
			u.setPassword(MD5Util.md5(pwd));
			return true;
		}
		return false;
	}

	@Override
	public UserDto getByLoginName(UserDto user) {
		Tuser t = userDao.get("from Tuser t  where t.loginname = '" + user.getLoginname() + "'");
		UserDto u = new UserDto();
		if (t != null) {
			BeanUtils.copyProperties(t, u);
		} else {
			return null;
		}
		return u;
	}

	@Override
	public List<UserDto> getUserListByUserType() {
		List<Tuser> list = userDao.find("from Tuser t where t.usertype=1 and t.state=0");
		List<UserDto> users = new ArrayList<UserDto>();
		for (int i = 0; i < list.size(); i++) {
			UserDto u = new UserDto();
			BeanUtils.copyProperties(list.get(i), u);
			users.add(u);
		}
		return users;
	}

	@Override
	public String[] getUserListNameByUserType() {
		List<Tuser> list = userDao.find("from Tuser t where t.usertype=1 and t.state=0");
		String[] users = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			users[i] = list.get(i).getName();
		}
		return users;
	}

	@Override
	public UserDto loginbyUid(String uid, String Random, String ClientDigest) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("serveruid", uid);
		Tuser t = userDao.get("from Tuser t where t.serveruid = :serveruid", params);
		if (t != null) {
			UserDto u = new UserDto();
			BeanUtils.copyProperties(t, u);
			return u;
		}
		return null;
	}

}
