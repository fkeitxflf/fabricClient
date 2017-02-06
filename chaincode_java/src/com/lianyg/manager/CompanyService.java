package com.lianyg.manager;

import java.util.List;

import com.lianyg.dto.CompanyDto;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.Tree;

public interface CompanyService {

	public List<CompanyDto> dataGrid(CompanyDto company, PageFilter ph) throws Exception;

	public Long count(String companyId) throws Exception;

	public Long count(CompanyDto company, PageFilter ph);

	public void add(CompanyDto company) throws Exception;

	public void edit(CompanyDto company) throws Exception;

	public void delete(Long id);

	public CompanyDto get(Long id);

	public List<Tree> tree();

	public String getCompanyList() throws Exception;
}
