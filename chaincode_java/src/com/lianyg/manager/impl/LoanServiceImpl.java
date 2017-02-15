package com.lianyg.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.lianyg.dao.BaseDaoI;
import com.lianyg.dto.LoanDto;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.entity.Loan;
import com.lianyg.manager.LoanService;

/**
 * loan service
 * 
 * @author hayuan
 */
public class LoanServiceImpl implements LoanService {

	private BaseDaoI<Loan> loanDao;

	public BaseDaoI<Loan> getLoanDao() {
		return loanDao;
	}

	public void setLoanDao(BaseDaoI<Loan> loanDao) {
		this.loanDao = loanDao;
	}

	@Override
	public List<LoanDto> dataGrid(LoanDto loandto, PageFilter ph) {
		List<LoanDto> resultList = new ArrayList<LoanDto>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Loan t ";
		List<Loan> loan = loanDao.find(hql + whereHql(loandto, params), params, ph.getPage(), ph.getRows());
		for (Loan loand : loan) {
			LoanDto cd = new LoanDto();
			BeanUtils.copyProperties(loand, cd);
			resultList.add(cd);
		}
		return resultList;

	}

	@Override
	public Long count(LoanDto loan, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Loan t ";
		return loanDao.count("select count(*) " + hql + whereHql(loan, params), params);
	}

	@Override
	public Long count(String loanId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from loan t ";
		if (loanId != null) {
			hql += " where 1=1 ";
			if (StringUtils.isNotBlank(loanId)) {
				hql += " and t.coreCpId = :loanId";
				params.put("loanId", loanId);
			}
		}
		return loanDao.count("select count(*) " + hql, params);
	}

	@Override
	public void add(LoanDto loandto) {
		Loan loan = new Loan();
		BeanUtils.copyProperties(loandto, loan);
		loanDao.save(loan);

	}

	private String whereHql(LoanDto loandto, Map<String, Object> params) {
		String hql = "";
		if (loandto != null) {
			hql += " where 1=1 ";

			if (StringUtils.isNotBlank(loandto.getLoanCpId())) {
				hql += " and t.loanCpId=:loanCpId";
				params.put("loanCpId", loandto.getLoanCpId());
			}

		}
		return hql;
	}

	@Override
	public void edit(LoanDto loan) throws Exception {
		Loan t = loanDao.get(Loan.class, loan.getId());
		t.setLoanCpId(loan.getLoanCpId());
		t.setLoanCpName(loan.getLoanCpName());
		t.setStatus(loan.getStatus());
		t.setBank(loan.getBank());
		t.setAmount(loan.getAmount());
		loanDao.update(t);

	}

	@Override
	public void delete(Long id) {
		Loan t = loanDao.get(Loan.class, id);
		loanDao.delete(t);

	}

	@Override
	public LoanDto get(Long id) {
		Loan loan = loanDao.get(Loan.class, id);
		LoanDto r = new LoanDto();
		r.setId(loan.getId());
		r.setCoreCpId(loan.getCoreCpId());
		r.setAmount(loan.getAmount());
		r.setLoanCpId(loan.getLoanCpId());
		r.setStatus(loan.getStatus());
		r.setBank(loan.getBank());
		r.setAmount(loan.getAmount());
		return r;
	}

}
