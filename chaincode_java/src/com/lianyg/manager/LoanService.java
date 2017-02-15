package com.lianyg.manager;

import java.util.List;

import com.lianyg.dto.LoanDto;
import com.lianyg.dto.base.PageFilter;

public interface LoanService {

	public List<LoanDto> dataGrid(LoanDto loan, PageFilter ph) throws Exception;

	public Long count(String coreCpId) throws Exception;

	public Long count(LoanDto loan, PageFilter ph);

	public void add(LoanDto loan) throws Exception;

	public void edit(LoanDto loan) throws Exception;

	public void delete(Long id);

	public LoanDto get(Long id);

}
