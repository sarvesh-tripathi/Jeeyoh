package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Dealredemptionlocation;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.utils.Utils;


@Repository("dealsDAO")
public class DealsDAO implements IDealsDAO {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Deals> getDeals() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByCategory(String category) {
		List<Deals> dealsList = null;
		String hqlQuery = "from Deals a where a.businessTypeId = :category";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("businessTypeId", category);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList;
	}



	@Override
	public List<Deals> getDealsByCategory(String category, String location,
			boolean isZipCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deals> getDealsByLocation(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deals> getDealsByZipCode(String zipCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveDeal(Deals deals, int batch_size) 
	{
		logger.debug("saveDeal ==>");
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.saveOrUpdate(deals);	

			if( batch_size % 20 == 0 ) {
				session.flush();
				session.clear();
			}

			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			logger.error("EXCEPTION IN saveDeal==>  "+e);
		}
		finally
		{
			session.close();
		}
	}
	
	
	@Override
	public void saveDeal(List<Deals> dealList, int batch_size) 
	{
		logger.debug("saveDeal ==>");
		int batch_size1 = 0;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			for(Deals deals : dealList)
			{
				batch_size1++;
				session.saveOrUpdate(deals);	

				if( batch_size1 % 20 == 0 ) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			logger.error("EXCEPTION IN saveDeal==>  "+e);
		}
		finally
		{
			session.close();
		}
	}

	@Override
	public void saveDeal(Deals deals)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(deals);        
	}

	@Override
	public void saveFilterdDeal(Deals deal) {

		//sessionFactory.getCurrentSession().saveOrUpdate(deal);
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(deal);
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}
		finally
		{
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByBusinessId(String businessId) {
		List<Deals> dealsList = null;
		String hqlQuery = "from Deals a where a.businessId = :businessId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("businessId", businessId);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList;
	}

	@Override
	public void saveSuggestions(Userdealssuggestion dealSuggestion) {

		sessionFactory.getCurrentSession().save(dealSuggestion);
		/*Session session = sessionFactory.openSession();
    		Transaction tx = null;
    		try
    		{
    			tx = session.beginTransaction();
    			session.save(dealSuggestion);
    			tx.commit();
    		}
    		catch (HibernateException e) {
    			if (tx!=null) tx.rollback();
    			e.printStackTrace(); 
    		}
    		finally
    		{
    			session.close();
    		}*/

	}

	@Override
	public int getDealsLikeCounts(Integer id) {
		// TODO Auto-generated method stub
		int rowCount = 0;
		String hqlQuery = "select count(a.dealUsageId) from Dealsusage a where a.deals.id = :id";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("id", id);
			rowCount = ((Number)query.uniqueResult()).intValue();						

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowCount;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByKeywords(String keyword, String category,
			String location) {
		logger.error("getDealsByKeywords ==== > "+location);
		logger.error("getDealsByKeywords ==== > "+location.isEmpty());
		// TODO Auto-generated method stub

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class);
		//criteria.setProjection(Projections.distinct(Projections.property("dealId")));
		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		//criteria.createAlias("tags", "tags");
		if(category != null && category.isEmpty() == false)
		{
			logger.debug("IN CATEGORY CHECKING ::: ");
			criteria.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			logger.debug("IN LOCATION CHECKING ::: ");
			criteria.add(Restrictions.eq("business.postalCode", location.trim()));
		}
		if(keyword != null && keyword.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("title", "%" + keyword.trim() + "%"))
					.add(Restrictions.like("dealUrl", "%" + keyword.trim() + "%"))
					.add(Restrictions.like("dealId", "%" + keyword.trim() + "%")));
			//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));

		}

		List<Deals> dealsList = criteria.list();
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getDealsSuggestion(int id)
	{
		logger.debug("IN user deal suggestion ::: "+id);
		List<Userdealssuggestion> dealSuggestionList = null;
		String hqlQuery = "from Userdealssuggestion a where a.user.userId = :id";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("id", id);
			dealSuggestionList = query.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		return dealSuggestionList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getUserDeals(Integer userId) {
		// TODO Auto-generated method stub

		List<Deals> deals= null;
		String hqlQuery = "from Deals where id in(select d.deals.id from Dealsusage d where d.user.userId =:userId)";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("userId", userId);
			deals = query.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		logger.debug("Value === "+deals);
		return deals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByBusinessId(Integer id) {
		// TODO Auto-generated method stub
		List<Deals> dealsList = null;
		String hqlQuery = "from Deals a where a.business.id = :id and a.endAt >= :currentDate group by a.title";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("id", id);
			query.setParameter("currentDate", Utils.getCurrentDate());
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByUserCategory(String itemCategory,
			String itemType,String providerName, double latitude, double longitude) {
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class);
		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		//criteria.createAlias("tags", "tags");
		if(providerName != null)
		{
			criteria.add(Restrictions.eq("business.name", providerName));
		}
		if(itemCategory != null)
		{
			criteria.add(Restrictions.eq("businesstype.businessType", itemCategory));
		}

		if(itemType != null)
		{
			criteria.add(Restrictions.like("title", "%" + itemType + "%"));
			criteria.add(Restrictions.like("dealUrl", "%" + itemType + "%"));
			criteria.add(Restrictions.like("dealId", "%" + itemType + "%"));
			//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));

		}

		criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));

		List<Deals> dealsList = criteria.list();*/
		List<Deals> dealsList = null;
		String hqlQuery = "select a from Deals a, Business b, Businesstype c where c.businessType = :category and (a.endAt >= :currentDate and a.endAt > :weekendDate) and a.business.id = b.id and b.businesstype.businessTypeId = c.businessTypeId and (a.title like '%"+ itemType + "%' or a.dealUrl like '%" + itemType + "%' or a.dealId like '%" + itemType + "%') and (((acos(sin(((:latitude)*pi()/180)) * sin((b.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((b.lattitude*pi()/180)) * cos((((:longitude)- b.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50 group by a.title";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", itemCategory);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestThursday());
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return dealsList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByUserEmail(String userEmail) {
		List<Deals> dealsList = null;
		String hqlQuery = "select distinct a from Deals a, Dealsusage b, User c where c.emailId = :emailId and c.userId = b.user.userId and a.id = b.deals.id";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("emailId", userEmail);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> userDealsSuggestedByJeeyoh(String keyword, String category,
			String location, String emailId) {
		logger.error("getDealsByKeywords ==== > "+location);

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Userdealssuggestion.class,"userdealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("deals", "deals");			
		criteria.createAlias("deals.business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		criteria.createAlias("userdealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", emailId));
		//criteria.createAlias("tags", "tags");
		if(category != null && category.isEmpty() == false)
		{
			logger.debug("IN CATEGORY CHECKING ::: ");
			criteria.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			logger.debug("IN LOCATION CHECKING ::: ");
			criteria.add(Restrictions.eq("business.postalCode", location.trim()));
		}
		if(keyword != null && keyword.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("deals.title", "%" + keyword.trim() + "%"))
					.add(Restrictions.like("deals.dealUrl", "%" + keyword.trim() + "%"))
					.add(Restrictions.like("deals.dealId", "%" + keyword.trim() + "%")));
			//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));

		}

		List<Userdealssuggestion> dealsList = criteria.list();
		return dealsList;
	}

	@Override
	public int userCategoryLikeCount(Integer userCategoryId) {
		int rowCount = 0;
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "select count(a.userCategoryLikesId) from UserCategoryLikes a where a.userCategory.userCategoryId=:userCategoryId";
		try
		{
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("userCategoryId", userCategoryId);
			rowCount = ((Number)query.uniqueResult()).intValue();	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating, int minPrice, int maxPrice) {
		logger.error("getDealsByLikeSearchKeyword ==== > "+searchText);

		List<Deals> dealsList = null;
		String hqlQuery = "";
		String fromStr = "";
		String whereStr = "";

		fromStr = "select a from Deals a inner join a.business b inner join b.businesstype c ";

		whereStr = "where c.businessType=:category";

		if(location != null && !location.trim().equals(""))
		{
			whereStr = whereStr + " and (b.postalCode = :location or b.city like '%" +location+ "%' or b.state like '%" +location+ "%' or b.stateCode like '%" +location+ "%' or b.displayAddress like '%" +location+ "%' or b.businessId like '%" +location+ "%')";
		}

		if(searchText != null && !searchText.trim().equals(""))
		{
			//hqlQuery = hqlQuery + " and (a.description like '%" + likekeyword  +"%' or a.title like '%" + likekeyword  +"%' or a.ancestorGenreDescriptions like '%" + likekeyword  +"%' or a.urlpath like '%" + likekeyword  +"%')
			hqlQuery = hqlQuery + " and ((a.title like '%" +searchText+ "%' or a.dealUrl like '%" +searchText+ "%' or a.dealId like '%" +searchText+ "%') and (a.title != :searchText or a.dealUrl != :searchText or a.dealId != :searchText))";

		}
		whereStr += " and (a.endAt >= :currentDate and a.endAt > :weekendDate)";

		if(rating != 0)
			whereStr = whereStr + " and b.rating >= :rating";

		if(lat != 0 && lon != 0)
		{
			whereStr = whereStr + " and (((acos(sin(((:latitude)*pi()/180)) * sin((b.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((b.lattitude*pi()/180)) * cos((((:longitude)- b.longitude)*pi()/180))))*180/pi())*60*1.1515) <=:distance";
		}

		if(minPrice != 0 && maxPrice != 0)
		{
			fromStr += " inner join a.dealoptions d ";
			whereStr += " and (d.originalPrice >= :minPrice and d.originalPrice <= :maxPrice) group by a.id";
		}

		try {
			hqlQuery = fromStr + whereStr;
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", category);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestThursday());
			if(searchText != null && !searchText.trim().equals(""))
				query.setParameter("searchText", searchText);
			if(location != null && !location.trim().equals(""))
				query.setParameter("location", location);
			if(lat != 0 && lon != 0)
			{
				query.setDouble("latitude", lat);
				query.setDouble("longitude", lon);
				query.setInteger("distance",distance);
			}
			if(rating != 0)
				query.setDouble("rating", rating);

			query.setFirstResult(offset);
			query.setMaxResults(limit);
			logger.debug("Query::  "+query);

			dealsList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//criteria.createAlias("business", "business");
		//criteria.createAlias("business.businesstype", "businesstype");
		Criteria addr = criteria.createCriteria("business"); 
		addr.createAlias("businesstype", "businesstype");

		if(category != null && category.isEmpty() == false)
		{
			addr.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			addr.add(Restrictions.disjunction().add(Restrictions.like("city", "%" + location + "%"))
					.add(Restrictions.like("state", "%" + location + "%"))
					.add(Restrictions.like("stateCode", "%" + location + "%"))
					.add(Restrictions.like("displayAddress", "%" + location + "%"))
					.add(Restrictions.like("businessId", "%" + location + "%"))
					.add(Restrictions.eq("postalCode", location)));
		}

		if(searchText != null && searchText.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("title", "%" + searchText + "%"))
					.add(Restrictions.like("dealUrl", "%" + searchText + "%"))
					.add(Restrictions.like("dealId", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("title", searchText))
							.add(Restrictions.ne("dealUrl", searchText))
							.add(Restrictions.ne("dealId", searchText)));

		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("endAt", Utils.getNearestThursday())));

		if(rating != 0)
			addr.add(Restrictions.ge("rating", rating));

		if(minPrice != 0 && maxPrice != 0)
		{
			criteria.createAlias("dealoptions", "dealoptions");
			criteria.add(Restrictions.conjunction().add(Restrictions.ge("dealoptions.originalPrice", Long.parseLong(Integer.toString(minPrice))))
					.add(Restrictions.le("dealoptions.originalPrice", Long.parseLong(Integer.toString(maxPrice)))));
		}

		if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin(({alias}.lattitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos(({alias}.lattitude*pi()/180)) * cos((((" + lon + ")- {alias}.longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			addr.add(Restrictions.sqlRestriction(sql)); 
		}

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Deals> dealsList = criteria.list();*/
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsBySearchKeyword(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating, int minPrice, int maxPrice, boolean forExactMatch){
		/*
		 * select distinct this_.id from jeeyoh.deals this_ inner join jeeyoh.business business1_ on this_.businessId=business1_.id inner join jeeyoh.businesstype businessty2_ on business1_.businessTypeId=businessty2_.businessTypeId inner join jeeyoh.dealoption dealoption3_ on this_.id=dealoption3_.dealId where businessty2_.businessType="Sport" and (this_.endAt>=now() and this_.endAt>"2014-07-17 23:59:59:59") and (dealoption3_.originalPrice>=50 and dealoption3_.originalPrice<=1000) and (((acos(sin(((42.3589)*pi()/180)) * sin((business1_.lattitude*pi()/180))+cos(((42.3589)*pi()/180)) * cos((business1_.lattitude*pi()/180)) * cos((((-71.0578)- business1_.longitude)*pi()/180))))*180/pi())*60*1.1515)<=2000 limit 8;
		 */
		logger.error("getDealsBySearchKeyword ==== > "+searchText + " forExactMatch: " + forExactMatch);
		List<Deals> dealsList = null;
		String hqlQuery = "";
		String fromStr = "";
		String whereStr = "";

		fromStr = "select a from Deals a inner join a.business b inner join b.businesstype c ";

		whereStr = "where c.businessType=:category";

		if(location != null && !location.trim().equals(""))
		{
			whereStr = whereStr + " and (b.postalCode = :location or b.city like '%" +location+ "%' or b.state like '%" +location+ "%' or b.stateCode like '%" +location+ "%' or b.displayAddress like '%" +location+ "%' or b.businessId like '%" +location+ "%')";
		}

		if(searchText != null && !searchText.trim().equals(""))
		{
			//hqlQuery = hqlQuery + " and (a.description like '%" + likekeyword  +"%' or a.title like '%" + likekeyword  +"%' or a.ancestorGenreDescriptions like '%" + likekeyword  +"%' or a.urlpath like '%" + likekeyword  +"%')
			if(forExactMatch)
				whereStr = whereStr + " and (a.title = :searchText or a.dealUrl = :searchText or a.dealId = :searchText)";
			else
				whereStr = whereStr + " and ((a.title like '%" +searchText+ "%' or a.dealUrl like '%" +searchText+ "%' or a.dealId like '%" +searchText+ "%') and (a.title != :searchText or a.dealUrl != :searchText or a.dealId != :searchText))";
		}

		whereStr += " and (a.endAt >= :currentDate and a.endAt > :weekendDate)";

		if(rating != 0)
			whereStr = whereStr + " and b.rating >= :rating";

		if(lat != 0 && lon != 0)
		{
			whereStr = whereStr + " and (((acos(sin(((:latitude)*pi()/180)) * sin((b.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((b.lattitude*pi()/180)) * cos((((:longitude)- b.longitude)*pi()/180))))*180/pi())*60*1.1515) <=:distance";
		}

		if(minPrice != 0 && maxPrice != 0)
		{
			fromStr += " inner join a.dealoptions d ";
			whereStr += " and (d.originalPrice >= :minPrice and d.originalPrice <= :maxPrice) group by a.title";
		}
		else
			whereStr += " group by a.title";

		try {
			hqlQuery = fromStr + whereStr;
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", category);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestThursday());
			if(searchText != null && !searchText.trim().equals(""))
				query.setParameter("searchText", searchText);
			if(location != null && !location.trim().equals(""))
				query.setParameter("location", location);
			if(lat != 0 && lon != 0)
			{
				query.setDouble("latitude", lat);
				query.setDouble("longitude", lon);
				query.setInteger("distance",distance);
			}
			if(rating != 0)
				query.setDouble("rating", rating);
			if(minPrice != 0 && maxPrice != 0)
			{
				query.setDouble("minPrice", minPrice);
				query.setDouble("maxPrice", maxPrice);
			}

			query.setFirstResult(offset);
			query.setMaxResults(limit);
			logger.debug("Query::  "+query);

			dealsList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}


		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		//criteria.createAlias("business", "business");
		//criteria.createAlias("business.businesstype", "businesstype");
		Criteria addr = criteria.createCriteria("business"); 
		addr.createAlias("businesstype", "businesstype");

		if(category != null && category.isEmpty() == false)
		{
			addr.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			addr.add(Restrictions.disjunction().add(Restrictions.like("city", "%" + location + "%"))
					.add(Restrictions.like("state", "%" + location + "%"))
					.add(Restrictions.like("stateCode", "%" + location + "%"))
					.add(Restrictions.like("displayAddress", "%" + location + "%"))
					.add(Restrictions.like("businessId", "%" + location + "%"))
					.add(Restrictions.eq("postalCode", location)));
		}


		if(searchText != null && searchText.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			if(forExactMatch)
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("title", searchText))
						.add(Restrictions.eq("dealUrl", searchText))
						.add(Restrictions.eq("dealId", searchText)));
			}
			else
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.like("title", "%" + searchText + "%"))
						.add(Restrictions.like("dealUrl", "%" + searchText + "%"))
						.add(Restrictions.like("dealId", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("title", searchText))
								.add(Restrictions.ne("dealUrl", searchText))
								.add(Restrictions.ne("dealId", searchText)));
			}


		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("endAt", Utils.getNearestThursday())));

		if(rating != 0)
			addr.add(Restrictions.ge("rating", rating));

		if(minPrice != 0 && maxPrice != 0)
		{
			criteria.createAlias("dealoptions", "dealoptions");
			criteria.add(Restrictions.conjunction().add(Restrictions.ge("dealoptions.originalPrice", Long.parseLong(Integer.toString(minPrice))))
					.add(Restrictions.le("dealoptions.originalPrice", Long.parseLong(Integer.toString(maxPrice)))));
		}

		if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin(({alias}.lattitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos(({alias}.lattitude*pi()/180)) * cos((((" + lon + ")- {alias}.longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			addr.add(Restrictions.sqlRestriction(sql)); 
		}

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Deals> dealsList = criteria.list();*/
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getUserDealSuggestions(String userEmail, int offset,
			int limit, String category, String suggestionType, double lat, double lon, int distance, double rating, int minPrice, int maxPrice) {
		logger.error("getUserDealSuggestions ==== > "+userEmail +" : "+offset +" : "+limit);
		List<Userdealssuggestion> dealsList = null;
		String hqlQuery = "";
		String fromStr = "";
		String whereStr = "";

		fromStr = "select a from Userdealssuggestion a inner join a.deals b inner join b.business c inner join c.businesstype d ";

		whereStr = "where a.user.emailId = :emailId";

		if(category != null && !category.trim().equals(""))
			whereStr += " and d.businessType=:category";

		whereStr += " and (b.endAt >= :currentDate and b.endAt > :weekendDate) and (a.suggestedTime is null or a.suggestedTime >= :suggestedTime)";

		if(suggestionType != null)
		{
			if(suggestionType.equalsIgnoreCase("Friend Suggestion"))
			{
				hqlQuery += " and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%' or a.suggestionType ='Wall Feed Suggestion' or a.suggestionType ='Direct Suggestion')";
			}
			else if(suggestionType.equalsIgnoreCase("Community Suggestion"))
			{
				hqlQuery += " and (a.suggestionType like '%Community%')";
			}
			else if(suggestionType.equalsIgnoreCase("Jeeyoh Suggestion"))
			{
				hqlQuery += " and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%' or a.suggestionType like '%User%')";
			}
		}
		
		if(rating != 0)
			whereStr = whereStr + " and c.rating >= :rating";

		if(lat != 0 && lon != 0)
		{
			whereStr = whereStr + " and (((acos(sin(((:latitude)*pi()/180)) * sin((c.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((c.lattitude*pi()/180)) * cos((((:longitude)- c.longitude)*pi()/180))))*180/pi())*60*1.1515) <=:distance";
		}

		if(minPrice != 0 && maxPrice != 0)
		{
			fromStr += " inner join b.dealoptions e ";
			whereStr += " and (e.originalPrice >= :minPrice and e.originalPrice <= :maxPrice) group by a.id";
		}

		try {
			hqlQuery = fromStr + whereStr;
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("emailId", userEmail);
			if(category != null && !category.trim().equals(""))
				query.setParameter("category", category);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("suggestedTime", Utils.getCurrentDateForEvent());
			query.setParameter("weekendDate", Utils.getNearestThursday());

			if(lat != 0 && lon != 0)
			{
				query.setDouble("latitude", lat);
				query.setDouble("longitude", lon);
				query.setInteger("distance",distance);
			}
			if(rating != 0)
				query.setDouble("rating", rating);
			if(minPrice != 0 && maxPrice != 0)
			{
				query.setDouble("minPrice", minPrice);
				query.setDouble("maxPrice", maxPrice);
			}

			query.setFirstResult(offset*10);
			query.setMaxResults(limit);
			logger.debug("Query::  "+query);

			dealsList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}

		/*	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Userdealssuggestion.class,"userdealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("userdealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", userEmail));
		criteria.createAlias("userdealssuggestion.deals", "deals");
		criteria.add(Restrictions.conjunction().add(Restrictions.ge("deals.endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("deals.endAt", Utils.getNearestThursday())));
		criteria.add(Restrictions.disjunction().add(Restrictions.isNull("userdealssuggestion.suggestedTime"))
				.add(Restrictions.ge("userdealssuggestion.suggestedTime", Utils.getCurrentDate())));
		criteria.setFirstResult(offset*10)
		.setMaxResults(limit);

		List<Userdealssuggestion> dealsList = criteria.list();*/
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTopDealsByRating(String idsStr) {
		logger.debug("getTopDealsByRating: "+idsStr);
		List<Object[]> rows = null;
		String hqlQuery = "select distinct a.id,b.rating from Deals a, Business b where a.id in ("+idsStr+") and a.business.id = b.id and b.rating is not null order by b.rating desc";
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
	public List<Object[]> getDealLikeCountByPage(String idsStr) {
		logger.debug("getDealLikeCountByPage => ");
		List<Object[]> rows = null;
		String hqlQuery = "select distinct b.id, count(b.id) as num from Page a, Deals b, Pageuserlikes c where a.pageId = c.page.pageId and b.business.id = a.business.id and b.id in ("+idsStr+") group by  a.about, b.id, b.title order by num desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			rows = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Deals getDealById(int dealId) {
		List<Deals> dealsList = null;
		String hqlQuery = "from Deals a where a.id = :dealId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("dealId", dealId);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList != null && !dealsList.isEmpty() ? dealsList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int isDealExists(String dealId) {
		logger.error("isDealExists ==== > ");
		List<Integer> dealList = null;
		/*Connection conn = null;
		   Statement stmt = null;
		   ResultSet rs = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jeeyoh?zeroDateTimeBehavior=convertToNull", "root", "");

		      //STEP 4: Execute a query
		      System.out.println("Creating database...");
		     // stmt = conn.createStatement();
		      long startTime = System.currentTimeMillis();
				logger.debug("DealsDAO ==> isDealExists ==> startTime ==> " + startTime);
		      String sql = "select id from deals where dealId = ?";
		      PreparedStatement st = conn.prepareStatement(sql);
		      st.setString(1, dealId);
		      rs = st.executeQuery();
		      rs.next();
		      long endTime = System.currentTimeMillis();
		      logger.debug("DealId:::  "+rs.getInt(1));
			logger.debug("DealsDAO ==> isDealExists ==> startTime ==> " + endTime + " total time ==> " + (endTime-startTime));

		   }catch(SQLException se){
			   logger.debug("Error1:::  "+se.getMessage());
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
			   logger.debug("Error2:::  "+e.getMessage());
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		 */		Session session = sessionFactory.getCurrentSession();
		 String hqlquery = "select id from deals a where a.dealId = :dealId";
		 // String hqlQuery = "select id from Deals a where a.dealId = :dealId";
		 try{
			 Query query = session.createSQLQuery(hqlquery);



			 long startTime = System.currentTimeMillis();
			 logger.debug("DealsDAO ==> isDealExists ==> startTime ==> " + startTime);
			 //Query query = session.createQuery(
			 //hqlQuery);
			 query.setParameter("dealId", dealId);
			 dealList = (List<Integer>)query.list();
			 long endTime = System.currentTimeMillis();
			 logger.debug("DealsDAO ==> isDealExists ==> startTime ==> " + endTime + " total time ==> " + (endTime-startTime));

		 }
		 catch (HibernateException e) {
			 e.printStackTrace();
		 }
		 catch (Exception e) {
			 e.printStackTrace();
		 }
		 /*try {
			return rs != null && !rs.next() ? rs.getInt(1) : 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}*/
		 return dealList != null && !dealList.isEmpty() ? dealList.get(0) : 0;
	}

	@Override
	public int getTotalDealsBySearchKeyWord(String searchText, String category,
			String location, double lat, double lon, int distance, double rating, int minPrice, int maxPrice) {
		logger.error("getTotalDealsBySearchKeyWord ==== > "+searchText + " "+minPrice);


		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.property("id")).setProjection(Projections.groupProperty("title"));
		//criteria.createAlias("business", "business");
		Criteria addr = criteria.createCriteria("business"); 
		addr.createAlias("businesstype", "businesstype");

		if(category != null && category.isEmpty() == false)
		{
			addr.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			addr.add(Restrictions.disjunction().add(Restrictions.like("city", "%" + location + "%"))
					.add(Restrictions.like("state", "%" + location + "%"))
					.add(Restrictions.like("stateCode", "%" + location + "%"))
					.add(Restrictions.like("displayAddress", "%" + location + "%"))
					.add(Restrictions.like("businessId", "%" + location + "%"))
					.add(Restrictions.eq("postalCode", location)));
		}

		if(searchText != null && searchText.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("title", "%" + searchText + "%"))
					.add(Restrictions.like("dealUrl", "%" + searchText + "%"))
					.add(Restrictions.like("dealId", "%" + searchText + "%")));

		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("endAt", Utils.getNearestThursday())));

		if(rating != 0)
			addr.add(Restrictions.ge("rating", rating));

		if(minPrice != 0 && maxPrice != 0)
		{
			logger.error("dealoptions ==== > "+maxPrice + " "+minPrice);
			criteria.createAlias("dealoptions", "dealoptions");
			criteria.add(Restrictions.conjunction().add(Restrictions.ge("dealoptions.originalPrice", Long.parseLong(Integer.toString(minPrice))))
					.add(Restrictions.le("dealoptions.originalPrice", Long.parseLong(Integer.toString(maxPrice)))));
		}

		if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin(({alias}.lattitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos(({alias}.lattitude*pi()/180)) * cos((((" + lon + ")- {alias}.longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			addr.add(Restrictions.sqlRestriction(sql)); 
		}
		//criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));

		int rowCount = criteria.list().size();
		//int rowCount = Integer.parseInt(criteria.uniqueResult().toString());
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByUserCategory(String itemCategory,
			String itemType, String providerName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class);
		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		//criteria.createAlias("tags", "tags");
		if(providerName != null)
		{
			criteria.add(Restrictions.eq("business.name", providerName));
		}
		if(itemCategory != null)
		{
			criteria.add(Restrictions.eq("businesstype.businessType", itemCategory));
		}

		if(itemType != null)
		{
			criteria.add(Restrictions.like("title", "%" + itemType + "%"));
			criteria.add(Restrictions.like("dealUrl", "%" + itemType + "%"));
			criteria.add(Restrictions.like("dealId", "%" + itemType + "%"));
			//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));

		}

		criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));

		List<Deals> dealsList = criteria.list();
		return dealsList;
	}

	@Override
	public boolean  saveDealUserLikes(Dealsusage dealsusage) {
		try{
			sessionFactory.getCurrentSession().save(dealsusage);        
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean updateDealUserLikes(Dealsusage dealsusage) {
		try{
			sessionFactory.getCurrentSession().update(dealsusage);        
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Dealoption getDealOptionByDealId(int dealId, int minPrice, int maxPrice) {
		List<Dealoption> dealsList = null;
		String hqlQuery = "from Dealoption a where a.deals.id = :dealId";
		if(minPrice != 0 && maxPrice != 0)
			hqlQuery += " and (originalPrice >=:minPrice and originalPrice <= :maxPrice)";
		
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("dealId", dealId);
			if(minPrice != 0 && maxPrice != 0)
			{
				query.setDouble("minPrice", minPrice);
				query.setDouble("maxPrice", maxPrice);
			}
			dealsList = (List<Dealoption>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList != null && !dealsList.isEmpty() ? dealsList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Dealredemptionlocation getRedemptionLocationByDealOption(
			int dealOptionId) {
		List<Dealredemptionlocation> locationList = null;
		String hqlQuery = "from Dealredemptionlocation a where a.dealoption.id = :dealOptionId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("dealOptionId", dealOptionId);
			locationList = (List<Dealredemptionlocation>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locationList != null && !locationList.isEmpty() ? locationList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getBookedDealList(int userId, String category) {
		logger.debug("getBookedDealList => ");
		List<Deals> dealList = null;
		logger.debug("get next weekend =>"+Utils.getNearestWeekend(null));
		//String hqlQuery = "select a from Deals a where a.id in (select b.deals.id from Dealsusage b where b.user.userId=:userId and b.isBooked=1) and endAt>=:currentDate and endAt<=:nearestFriday";
		String hqlQuery = "select b from Dealsusage a, Deals b, Business c, Businesstype d where a.user.userId = :userId and a.isBooked is true and a.deals.id = b.id and (b.endAt >= :currentDate and  b.endAt <= :nearestFriday) and d.businessType = :category and b.business.businessId = c.id and c.businesstype.businessTypeId = d.businessTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("nearestFriday", Utils.getNearestFriday());
			query.setParameter("category", category);
			dealList = (List<Deals>) query.list();
			logger.debug("getBookedDealList dealList => "+dealList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return dealList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByuserLikesForCurrentWeekend(String itemCategory,
			String itemType,String providerName, double latitude, double longitude) {
		List<Deals> dealsList = null;
		String hqlQuery = "select a from Deals a, Business b, Businesstype c where c.businessType = :category and (a.endAt >= :currentDate and a.endAt <= :weekendDate) and a.business.id = b.id and b.businesstype.businessTypeId = c.businessTypeId and (a.title like '%"+ itemType + "%' or a.dealUrl like '%" + itemType + "%' or a.dealId like '%" + itemType + "%') and (((acos(sin(((:latitude)*pi()/180)) * sin((b.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((b.lattitude*pi()/180)) * cos((((:longitude)- b.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50 group by a.dealId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", itemCategory);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestWeekend(null));
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return dealsList;
	}

	@Override
	public void updateDeal(Deals deals) {
		try {
			sessionFactory.getCurrentSession().update(deals);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
	}

}
