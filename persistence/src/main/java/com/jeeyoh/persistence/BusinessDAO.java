package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Ybusiness;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Businesstype> getBusinesstypeByTypeArray(String[] type) {
		
		List<Businesstype> businessTypeList = null;
		/*Session session = sessionFactory.openSession();
		Transaction tx = null;
		
		//String[] type1 = {'RESTAURANT','SPA','SPORT'};
		//String hqlQuery = "from Businesstype a where a.businessType = :type";
		String hqlQuery = "from Businesstype a where a.businessType in {:type}";
		try
		{
			tx = session.beginTransaction();
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("type", type1);
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
		logger.debug("SIZE "+businessTypeList.size());*/
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Businesstype.class);
		criteria.add(Restrictions.in("businessType", type));
		businessTypeList = (List<Businesstype>)criteria.list();
		logger.debug("SIZE "+businessTypeList.size());
		return  businessTypeList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Businesstype getBusinesstypeByType(String type) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Businesstype> businessTypeList = null;
		//String[] type1 = {"RESTAURANT","SPA","SPORT"};
		//String hqlQuery = "from Businesstype a where a.businessType = :type";
		String hqlQuery = "from Businesstype a where a.businessType :type";
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


	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessByCriteria(String userEmail, String searchText,
			String category, String location) {
		
		logger.debug("getBusinessByCriteria ==> "+searchText +" ==> "+category+" ==> "+location);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Business.class, "business").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		criteria.createAlias("business.businesstype", "businessTypes");
		if(userEmail != null && !userEmail.trim().equals(""))
		{
			criteria.createAlias("business.usernondealsuggestions", "usernondealsuggestion");
			criteria.createAlias("usernondealsuggestion.user", "user");
			criteria.add(Restrictions.eq("user.emailId", userEmail));
		}
		
		if(category != null && !category.trim().equals(""))
		{
			criteria.add(Restrictions.eq("businessTypes.businessType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location))
					.add(Restrictions.like("business.city", "%" + location + "%")));
		}
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.businessId", "%" + searchText + "%"))
					.add(Restrictions.like("business.websiteUrl", "%" + searchText + "%"))
					.add(Restrictions.like("business.city", "%" + searchText + "%"))
					.add(Restrictions.like("business.displayAddress", "%" + searchText + "%"))
					.add(Restrictions.like("business.name", "%" + searchText + "%")));
		}
		
		List<Business> businessList = criteria.list();
	
		return businessList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessByuserLikes(String likekeyword, String itemCategory) {
		logger.debug("getBusinessByuserLikes ==> "+likekeyword);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Business.class, "business").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		criteria.createAlias("business.businesstype", "businessTypes");
		if(likekeyword != null && !likekeyword.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.displayAddress", "%" + likekeyword + "%"))
					.add(Restrictions.like("business.businessId", "%" + likekeyword + "%"))
					.add(Restrictions.eq("business.postalCode", likekeyword))
					.add(Restrictions.like("business.name", "%" + likekeyword + "%"))
					.add(Restrictions.like("business.websiteUrl", "%" + likekeyword + "%"))
					.add(Restrictions.like("business.city", "%" + likekeyword + "%")));
		}
		if(itemCategory != null && !itemCategory.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.businessId", "%" + itemCategory + "%"))
					.add(Restrictions.eq("businessTypes.businessType", itemCategory))
					.add(Restrictions.like("business.name", "%" + itemCategory + "%")));
		}
		
		List<Business> businessList = criteria.list();
	
		return businessList;
	}
}
