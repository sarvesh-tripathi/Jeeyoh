package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;

@Repository("businessDAO")
public class BusinessDAO implements IBusinessDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessById(String businessId) {
		List<Business> businessList = null;
		String hqlQuery = "from Business a where a.businessId = :businessId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("businessId", businessId);
			businessList = (List<Business>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessList;
	}

	@Override
	public List<Business> getBusinessByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> getBusinesses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveBusiness(Business business) {
		sessionFactory.getCurrentSession().saveOrUpdate(business);	
	}

	@Override
	public Businesstype getBusinesstypeByType(String type) {
		List<Businesstype> businessTypeList = null;
		String hqlQuery = "from Businesstype a where a.businessType = :type";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("type", type);
			businessTypeList = (List<Businesstype>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  businessTypeList != null && !businessTypeList.isEmpty() ? businessTypeList.get(0) : null;
	}

}
