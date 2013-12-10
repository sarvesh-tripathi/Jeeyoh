package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;

@Repository("businessDAO")
public class BusinessDAO implements IBusinessDAO {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessById(String businessId) {
		List<Business> businessList = null;
		//Session session  = sessionFactory.openSession();
		//Transaction tx = session.beginTransaction();
		String hqlQuery = "from Business a where a.businessId = :businessId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("businessId", businessId);
			businessList = (List<Business>) query.list();
			//tx.commit();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if(businessList.isEmpty())
		{
			return getBusinessById("25");
		}*/
		return businessList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessByIdForGroupon(String businessId) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Business> businessList = null;
		String hqlQuery = "from Business a where a.businessId = :businessId";
		try
		{
			tx = session.beginTransaction();
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("businessId", businessId);
			businessList = (List<Business>) query.list();
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
		return businessList;
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessById(int id) {
		List<Business> businessList = null;
		String hqlQuery = "from Business a where a.id = :id";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("id", id);
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
	public void saveBusiness(Business business, int count) {
		logger.debug("BusinessDAO ==> saveBusiness ==> business ==> " + business.getBusinessId());
		//sessionFactory.getCurrentSession().save(business);
		Session session  = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(business);
		if(count % 50 == 0)
		{
			session.flush();
			session.clear();
		}
		
		tx.commit();
		session.close();
	}
	
	
	@Override
	public void saveBusiness(Business business) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.saveOrUpdate(business);	
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
		
	}

	@Override
	public Businesstype getBusinesstypeByType(String type) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Businesstype> businessTypeList = null;
		String hqlQuery = "from Businesstype a where a.businessType = :type";
		try
		{
			tx = session.beginTransaction();
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("type", type);
			businessTypeList = (List<Businesstype>) query.list();
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
		return  businessTypeList != null && !businessTypeList.isEmpty() ? businessTypeList.get(0) : null;
	}

}
