package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Gcategory;
import com.jeeyoh.persistence.domain.Lcategory;

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
	public Business getBusinessById(int id) {
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
		return businessList != null && !businessList.isEmpty() ? businessList.get(0) : null;
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
		if(count % 20 == 0)
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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Businesstype.class);
		criteria.add(Restrictions.in("businessType", type));
		businessTypeList = (List<Businesstype>)criteria.list();
		logger.debug("SIZE "+businessTypeList.size());
		return  businessTypeList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Businesstype getBusinesstypeByType(String type) {

		logger.debug("Business By Type :: "+type);
		List<Businesstype> businessTypeList = null;
		String hqlQuery = "from Businesstype a where a.businessType =:type";
		try
		{

			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("type", type);
			businessTypeList = (List<Businesstype>) query.list();

		}
		catch (HibernateException e) {
			e.printStackTrace();
		}

		return  businessTypeList != null && !businessTypeList.isEmpty() ? businessTypeList.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessByCriteria(String userEmail, String searchText,
			String category, String location, String rating) {

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
		if(rating != null && !rating.trim().equals(""))
		{
			criteria.add(Restrictions.eq("business.rating", Long.parseLong(rating)));
		}

		List<Business> businessList = criteria.list();

		return businessList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessByuserLikes(String likekeyword,
			String itemCategory, String providerName, double latitude, double longitude) {
		logger.debug("getBusinessByuserLikes ==> "+likekeyword +" : "+providerName+ " : "+itemCategory);
		/*	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Business.class, "business").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

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
		if(providerName != null && !providerName.trim().equals(""))
		{
			criteria.add(Restrictions.eq("business.name", providerName.trim()));
		}
		if(itemCategory != null && !itemCategory.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.businessId", "%" + itemCategory + "%"))
					.add(Restrictions.eq("businessTypes.businessType", itemCategory))
					.add(Restrictions.like("business.name", "%" + itemCategory + "%")));
		}


		List<Business> businessList = criteria.list();*/
		List<Business> businessList = null;
		String hqlQuery = "select a from Business a, Businesstype b where b.businessType = :category and a.businesstype.businessTypeId = b.businessTypeId and (a.displayAddress like '%" + likekeyword  +"%' or a.businessId like '%" + likekeyword  +"%' or a.name like '%" + likekeyword  +"%' or a.websiteUrl like '%" + likekeyword  +"%' or a.city like '%" + likekeyword  +"%') and (((acos(sin(((:latitude)*pi()/180)) * sin((a.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((a.lattitude*pi()/180)) * cos((((:longitude)- a.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50 group by a.businessId";
		try
		{

			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", itemCategory);
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			businessList = (List<Business>) query.list();

		}
		catch (HibernateException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}

		return businessList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Gcategory getBusinessCategory(String name) {
		logger.debug("getBusinessCategory ::: "+name);
		List<Gcategory> gCategory = null;
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "from Gcategory where category =:name)";
		try
		{
			Query query = session.createQuery(hqlQuery);
			query.setParameter("name", name);
			gCategory = (List<Gcategory>) query.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		return gCategory != null && !gCategory.isEmpty() ? gCategory.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessBySearchKeyword(String searchText,String category, String location,int offset, int limit) {
		logger.debug("getBusinessBySearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Business.class, "business").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("business.businesstype", "businessTypes");

		criteria.add(Restrictions.isNotNull("business.websiteUrl"));
		
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.eq("business.businessId", searchText))
					.add(Restrictions.eq("business.websiteUrl", searchText))
					.add(Restrictions.eq("business.city", searchText))
					.add(Restrictions.eq("business.displayAddress", searchText))
					.add(Restrictions.eq("business.name", searchText)));
		}

		if(category != null && !category.trim().equals(""))
		{
			criteria.add(Restrictions.eq("businessTypes.businessType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%"))
					.add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location)));
		}
		/*if(rating != null && !rating.trim().equals(""))
		{
			criteria.add(Restrictions.eq("business.rating", Long.parseLong(rating)));
		}*/

		criteria.setFirstResult(offset)
		.setMaxResults(limit);

		List<Business> businessList = criteria.list();

		return businessList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessByLikeSearchKeyword(String searchText,String category, String location,int offset, int limit) {
		logger.debug("getBusinessByLikeSearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Business.class, "business").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("business.businesstype", "businessTypes");

		criteria.add(Restrictions.isNotNull("business.websiteUrl"));
		
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.businessId", "%" + searchText + "%"))
					.add(Restrictions.like("business.websiteUrl", "%" + searchText + "%"))
					.add(Restrictions.like("business.city", "%" + searchText + "%"))
					.add(Restrictions.like("business.displayAddress", "%" + searchText + "%"))
					.add(Restrictions.like("business.name", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("business.name", searchText)));
		}

		if(category != null && !category.trim().equals(""))
		{
			criteria.add(Restrictions.eq("businessTypes.businessType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%"))
					.add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location)));
		}

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		/*if(rating != null && !rating.trim().equals(""))
		{
			criteria.add(Restrictions.eq("business.rating", Long.parseLong(rating)));
		}*/

		List<Business> businessList = criteria.list();

		return businessList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessByCriteriaWithoutRating(String userEmail,
			String searchText, String category, String location, String rating) {
		logger.debug("getBusinessByCriteriaWithoutRating ==> "+searchText +" ==> "+category+" ==> "+location);
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
		if(rating != null && !rating.trim().equals(""))
		{
			criteria.add(Restrictions.isNull("business.rating"));
		}

		List<Business> businessList = criteria.list();

		return businessList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getUserNonDealSuggestions(String userEmail,
			int offset, int limit) {
		logger.debug("getUserNonDealSuggestions ==> "+userEmail +" ==> "+offset+" ==> "+limit);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Business.class, "business").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(userEmail != null && !userEmail.trim().equals(""))
		{
			criteria.createAlias("business.usernondealsuggestions", "usernondealsuggestion");
			criteria.createAlias("usernondealsuggestion.user", "user");
			criteria.add(Restrictions.eq("user.emailId", userEmail));
		}



		criteria.setFirstResult(offset)
		.setMaxResults(limit);

		List<Business> businessList = criteria.list();

		return businessList;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTopBusinessByRating(String idsArray) {
		logger.debug("getTopBusinessByRating: "+idsArray);
		List<Object[]> rows = null;
		//String hqlQuery = "select distinct a.id,a.rating from Business a, Page b where b.pageId in ("+idsArray+") and a.id = b.business.id order by a.rating desc limit 3";
		String hqlQuery = "select distinct a.id,a.rating from Business a where a.id in ("+idsArray+") order by a.rating desc limit 3";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);

			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			logger.debug("Error: "+e.getMessage());
			e.printStackTrace();
		}
		return rows;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getNonDealLikeCountByPage(String idsStr) {
		logger.debug("getNonDealLikeCountByPage: "+idsStr);
		List<Object[]> rows = null;
		//String hqlQuery = "select distinct count(b.id) as num, b.id from Page a, Business b, Pageuserlikes c where a.pageId = c.page.pageId and b.id = a.business.id and b.id in ("+idsStr+") group by  a.about, b.id, b.name order by num desc limit 10";
		String hqlQuery = "select distinct count(c.page.pageId) as num, b.id from Business b left Join b.pages a left join a.pageuserlikeses c where b.id in ("+ idsStr +") group by  a.about, b.id, b.name order by num desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);

			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			logger.debug("Error: "+e.getMessage());
			e.printStackTrace();
		}
		return rows;
	}


	@Override
	public int getTotalBusinessBySearchKeyWord(String searchText,
			String category, String location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Business.class, "business").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setProjection(Projections.property("business.id"));

		criteria.createAlias("business.businesstype", "businessTypes");
		
		criteria.add(Restrictions.isNotNull("business.websiteUrl"));
		
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.businessId", "%" + searchText + "%"))
					.add(Restrictions.like("business.websiteUrl", "%" + searchText + "%"))
					.add(Restrictions.like("business.city", "%" + searchText + "%"))
					.add(Restrictions.like("business.displayAddress", "%" + searchText + "%"))
					.add(Restrictions.like("business.name", "%" + searchText + "%")));
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
					.add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%")));
		}
		
		int rowCount = criteria.list().size();
		return rowCount;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Lcategory getLivingSocialBusinessCategory(String name) {
		logger.debug("getBusinessCategory ::: "+name);
		List<Lcategory> lCategory = null;
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "from Lcategory where category =:name";
		try
		{
			Query query = session.createQuery(hqlQuery);
			query.setParameter("name", name);
			lCategory = (List<Lcategory>) query.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		return lCategory != null && !lCategory.isEmpty() ? lCategory.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinessByIdForLivingSocial(String businessId) {
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
	public List<Business> getBusinessByNameForLivingSocial(String merchantName) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<Business> businessList = null;
		String hqlQuery = "from Business a where a.name = :merchantName";
		try
		{
			tx = session.beginTransaction();
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("merchantName", merchantName);
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
	public List<Businesstype> getBusinesstypes() {
		//logger.debug("in getBusinesstypes ===>");
		List<Businesstype> businessTypeList=null;
		String hqlQuery = "from Businesstype";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);			
			businessTypeList = (List<Businesstype>) query.list();
			logger.debug("businessTypeList => " + businessTypeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessTypeList;
	}


}
