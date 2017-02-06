package com.lianyg.manager;

import java.util.List;

import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.SessionInfo;
import com.lianyg.dto.sys.UserDto;

public interface UserServiceI {

	public List<UserDto> dataGrid(UserDto user, PageFilter ph);

	public Long count(UserDto user, PageFilter ph);

	public void add(UserDto user);

	public void delete(Long id);

	public void edit(UserDto user);

	public UserDto get(Long id);

	public UserDto login(UserDto user);

	public List<String> listResource(Long id);

	public boolean editUserPwd(SessionInfo sessionInfo, String oldPwd, String pwd);

	public UserDto getByLoginName(UserDto user);

	public List<UserDto> getUserListByUserType();

	public String[] getUserListNameByUserType();

	public UserDto getUserbyUid(String username);

	public UserDto loginbyUid(String uid, String Random, String ClientDigest);

}
