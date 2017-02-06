package com.lianyg.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.lianyg.dao.BaseDaoI;
import com.lianyg.dto.CompanyDto;
import com.lianyg.dto.base.PageFilter;
import com.lianyg.dto.base.Tree;
import com.lianyg.entity.Company;
import com.lianyg.manager.CompanyService;

/**
 * company service
 * 
 * @author hayuan
 */
public class CompanyServiceImpl implements CompanyService {

	private BaseDaoI<Company> companyDao;

	public BaseDaoI<Company> getCompanyDao() {
		return companyDao;
	}

	public void setCompanyDao(BaseDaoI<Company> companyDao) {
		this.companyDao = companyDao;
	}

	@Override
	public List<CompanyDto> dataGrid(CompanyDto companydto, PageFilter ph) {
		List<CompanyDto> resultList = new ArrayList<CompanyDto>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Company t ";
		List<Company> company = companyDao.find(hql + whereHql(companydto, params), params, ph.getPage(), ph.getRows());
		for (Company companyd : company) {
			CompanyDto cd = new CompanyDto();
			BeanUtils.copyProperties(companyd, cd);
			resultList.add(cd);
		}
		return resultList;

	}

	@Override
	public Long count(CompanyDto company, PageFilter ph) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Company t ";
		return companyDao.count("select count(*) " + hql + whereHql(company, params), params);
	}

	@Override
	public Long count(String companyId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Company t ";
		if (companyId != null) {
			hql += " where 1=1 ";
			if (StringUtils.isNotBlank(companyId)) {
				hql += " and t.companyId = :companyId";
				params.put("companyId", companyId);
			}
		}
		return companyDao.count("select count(*) " + hql, params);
	}

	@Override
	public void add(CompanyDto companydto) {
		Company company = new Company();
		BeanUtils.copyProperties(companydto, company);
		companyDao.save(company);

	}

	@Override
	public List<Tree> tree() {
		return companyDao.findCompanyTree();
	}

	private String whereHql(CompanyDto companydto, Map<String, Object> params) {
		String hql = "";
		if (companydto != null) {
			hql += " where 1=1 ";

			if (StringUtils.isNotBlank(companydto.getCompanyId())) {
				hql += " and t.companyId=:companyId";
				params.put("companyId", companydto.getCompanyId());
			}

			if (StringUtils.isNotBlank(companydto.getCompanyName())) {
				hql += " and t.companyName like :companyName";
				params.put("companyName", "%" + companydto.getCompanyName() + "%");
			}

			if (StringUtils.isNotBlank(companydto.getManager())) {
				hql += " and t.manager like :manager";
				params.put("manager", "%" + companydto.getManager() + "%");
			}

		}
		return hql;
	}

	@Override
	public void edit(CompanyDto company) throws Exception {
		Company t = companyDao.get(Company.class, company.getId());
		t.setCompanyName(company.getCompanyName());
		t.setCompanyAddress(company.getCompanyAddress());
		t.setManager(company.getManager());
		t.setManagerTel(company.getManagerTel());
		t.setIcon("icon-company");
		companyDao.update(t);

	}

	@Override
	public void delete(Long id) {
		Company t = companyDao.get(Company.class, id);
		companyDao.delete(t);

	}

	@Override
	public CompanyDto get(Long id) {
		Company company = companyDao.get(Company.class, id);
		CompanyDto r = new CompanyDto();
		r.setId(company.getId());
		r.setCompanyId(company.getCompanyId());
		r.setCompanyName(company.getCompanyName());
		r.setCompanyAddress(company.getCompanyAddress());
		r.setManager(company.getManager());
		r.setManagerTel(company.getManagerTel());
		r.setType(company.getType());
		r.setNote(company.getNote());
		return r;
	}

	@Override
	public String getCompanyList() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String fullQuery = "select company.companyId as companyId, company.companyName as companyName from t_company company";
		List<Object[]> list = companyDao.findBySql(fullQuery, params);
		StringBuffer result = new StringBuffer();
		if (0 < list.size()) {
			result.append("[");
			for (int i = 0; i < list.size(); i++) {
				Object company[] = (Object[]) list.get(i);
				result.append("{");
				result.append("\"");
				result.append("companyId");
				result.append("\"");
				result.append(":");
				result.append("\"");
				result.append(String.valueOf(company[0]));
				result.append("\"");
				result.append(",");
				result.append("\"");
				result.append("companyName");
				result.append("\"");
				result.append(":");
				result.append("\"");
				result.append(String.valueOf(company[1]));
				result.append("\"");
				result.append("},");
			}
			result.deleteCharAt(result.length() - 1);
			result.append("]");
		}

		return result.toString();
	}

}
