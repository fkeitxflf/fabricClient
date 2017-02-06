package com.lianyg.manager;

import java.util.List;

import com.lianyg.dto.base.SessionInfo;
import com.lianyg.dto.base.Tree;
import com.lianyg.dto.sys.ResourceDto;

public interface ResourceServiceI {

	public List<ResourceDto> treeGrid();

	public void add(ResourceDto resource);

	public void delete(Long id);

	public void edit(ResourceDto resource);

	public ResourceDto get(Long id);

	public List<Tree> tree(SessionInfo sessionInfo);

	public List<Tree> listAllTree(boolean flag);

	public List<String> listAllResource();

}
