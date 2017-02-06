package com.lianyg.manager;

import java.util.List;

import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.Tree;
import com.lianyg.dto.sys.RoleDto;

public interface RoleServiceI {

	public List<RoleDto> dataGrid(RoleDto role, PageFilter ph);

	public Long count(RoleDto role, PageFilter ph);

	public RoleDto get(Long id);

	public void grant(RoleDto role);

	public List<Tree> tree();

}
