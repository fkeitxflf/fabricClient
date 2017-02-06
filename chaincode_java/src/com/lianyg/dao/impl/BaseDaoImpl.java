package com.lianyg.dao.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.lianyg.dao.BaseDaoI;
import com.lianyg.dto.base.Tree;
import com.lianyg.util.CommonUtil;
import com.lianyg.util.Constants;

@Repository
public class BaseDaoImpl<T> implements BaseDaoI<T> {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 获得当前事物的session
	 * 
	 * @return org.hibernate.Session
	 */
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Serializable save(T o) {
		if (o != null) {
			return this.getCurrentSession().save(o);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Class<T> c, Serializable id) {
		return (T) this.getCurrentSession().get(c, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		List<T> l = q.list();
		if ((l != null) && (l.size() > 0)) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public T get(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if ((params != null) && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		@SuppressWarnings("unchecked")
		List<T> l = q.list();
		if ((l != null) && (l.size() > 0)) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public void delete(T o) {
		if (o != null) {
			this.getCurrentSession().delete(o);
		}
	}

	@Override
	public void update(T o) {
		if (o != null) {
			this.getCurrentSession().update(o);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if ((params != null) && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		Query q = this.getCurrentSession().createQuery(hql);
		if ((params != null) && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql, int page, int rows) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public Long count(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return (Long) q.uniqueResult();
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if ((params != null) && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return (Long) q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findBySql(String sql) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findBySql(String sql, int page, int rows) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findBySql(String sql, Map<String, Object> params) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		if ((params != null) && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findBySql(String sql, Map<String, Object> params, int page, int rows) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		if ((params != null) && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public BigInteger countBySql(String sql) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		return (BigInteger) q.uniqueResult();
	}

	@Override
	public BigInteger countBySql(String sql, Map<String, Object> params) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		if ((params != null) && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return (BigInteger) q.uniqueResult();
	}

	@Override
	public List<Tree> findCompanyTree() {
		List<Tree> treeList = new ArrayList<Tree>();
		String districtSql = null;
		Query querydistrictList = null;
		districtSql = "select distinct company.type as type from t_company company order by company.id";
		querydistrictList = sessionFactory.getCurrentSession().createSQLQuery(districtSql);

		String companySql = "select distinct company.companyId as companyId, company.companyName as companyName from t_company company where company.type = ? order by company.id";

		List<?> districtList = querydistrictList.list();
		// 查询所有的父结点
		if ((districtList != null) && (districtList.size() > 0)) {
			for (int i = 0; i < districtList.size(); i++) {

				String districtResult = String.valueOf(districtList.get(i));
				Tree districTtree = new Tree();
				districTtree.setId(districtResult);
				if (Constants.COMPANY_TYPE_CORE_VALUE.equals(districtResult)) {
					districTtree.setText(Constants.COMPANY_TYPE_CORE);
				} else if (Constants.COMPANY_TYPE_RELATE_VALUE.equals(districtResult)) {
					districTtree.setText(String.valueOf(Constants.COMPANY_TYPE_RELATE));
				}

				Query querycompanyList = sessionFactory.getCurrentSession().createSQLQuery(companySql);
				querycompanyList.setString(0, String.valueOf(districtResult));
				List<?> companyList = querycompanyList.list();
				if ((companyList != null) && (companyList.size() > 0)) {
					List<Tree> companyTreeList = new ArrayList<Tree>();
					for (int j = 0; j < companyList.size(); j++) {
						Object companyResult[] = (Object[]) companyList.get(j);
						Tree companyTree = new Tree();
						companyTree.setId(String.valueOf(companyResult[0]));
						companyTree.setText(String.valueOf(companyResult[1]));
						companyTreeList.add(companyTree);
					}
					districTtree.setChildren(companyTreeList);

				}

				treeList.add(districTtree);
			}
		}

		return treeList;
	}

	@Override
	public String findCompany(String districtId) {
		String hql = "select company.companyId as companyId, company.companyName as companyName, company.districtId as districtId from Company company where company.districtId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, districtId);
		String result = null;
		try {
			result = CommonUtil.toJson(query.list());
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
