package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.CommunityComments;
import com.jeeyoh.persistence.domain.CommunityReviewMap;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.PopularCommunity;
import com.jeeyoh.utils.Utils;

@Repository("eventsDAO")
public class EventsDAO implements IEventsDAO{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveEvents(Events events, int batch_size) {
		logger.debug("saveEvents ==>");
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.saveOrUpdate(events);	

			if( batch_size % 20 == 0 ) {
				session.flush();
				session.clear();
			}
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
		/*Session session =  sessionFactory.getCurrentSession();
		try
		{
			session.save(events);
			if(batch_size % 20 == 0)
			{
				session.flush();
				session.clear();
			}
		}
		catch (HibernateException e) {
			logger.debug("Error ==> "+e.getLocalizedMessage());
			e.printStackTrace(); 
		}*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsByCriteria(String userEmail,
			String searchText, String category, String location) {
		logger.debug("getEventsByCriteria ==> "+searchText +" ==> "+category+" ==> "+location);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("events.page", "page");
		if(userEmail != null && !userEmail.trim().equals(""))
		{
			criteria.createAlias("events.usereventsuggestions", "usereventsuggestions");
			criteria.createAlias("usereventsuggestions.user", "user");
			criteria.add(Restrictions.eq("user.emailId", userEmail));
		}


		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%")));
		}
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("events.description", "%" + searchText + "%"))
					.add(Restrictions.like("events.city", "%" + searchText + "%"))
					.add(Restrictions.like("events.ancestorGenreDescriptions", "%" + searchText + "%"))
					.add(Restrictions.like("events.venue_name", "%" + searchText + "%")));
		}

		List<Events> eventsList = criteria.list();
		return eventsList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunities() {
		List<Page> pageList = null;
		String hqlQuery = "from Page";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsByCommunityId(int pageId) {
		logger.debug("pageList => ");
		List<Events> eventsList = null;
		String hqlQuery = "select b from Events a, Page b where b.pageid = :pageId and b.pageId = a.page.pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			eventsList = (List<Events>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsByCommunityType(int pageId, int pageType) {
		logger.debug("pageList => ");
		List<Events> eventsList = null;
		String hqlQuery = "select b from Events a, Page b, Pagetype c where b.pageid = :pageId and c.pageType = :pageType  and c.pageTypeId = b.pagetype.pageTypeId and b.pageId = a.page.pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			eventsList = (List<Events>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunityBySearchKeywordForBusiness(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating) {
		logger.debug("getCommunityBySearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.eq("page.about", searchText))
					.add(Restrictions.eq("page.pageUrl", searchText)));
		}

		if(location != null && !location.trim().equals(""))
		{
			criteria.createAlias("page.business", "business");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location))
					.add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%")));
		}

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Page> pageList = criteria.list();
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunityBySearchKeywordForEvents(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating) {
		logger.debug("getCommunityBySearchKeyword ==> "+searchText);

		List<Page> pageList = null;
		String hqlQuery = "";
		if(category.equalsIgnoreCase("NIGHTLIFE"))
		{
			hqlQuery = "select distinct a from Page a,Events b, Pagetype c where (c.pageType=:category or c.pageType='CONCERT' or c.pageType='THEATER') and a.pagetype.pageTypeId = c.pageTypeId and a.pageId = b.page.pageId";
		}
		else
			hqlQuery = "select distinct a from Page a,Events b, Pagetype c where c.pageType=:category and a.pagetype.pageTypeId = c.pageTypeId and a.pageId = b.page.pageId";

		if(searchText != null && !searchText.trim().equals(""))
		{
			hqlQuery = hqlQuery + "and (a.about = :searchText or a.pageUrl = :searchText)";
		}
		if(location != null && !location.trim().equals(""))
		{
			hqlQuery = hqlQuery + " and (b.zip = :location or b.city like '%" +location+ "%' or b.state like '%" +location+ "%' or b.stateCode like '%" +location+ "%')";
		}
		//hqlQuery = hqlQuery + " limit "+offset+","+limit;
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", category);
			if(searchText != null && !searchText.trim().equals(""))
				query.setParameter("searchText", searchText);
			if(location != null && !location.trim().equals(""))
				query.setParameter("location", location);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.eq("page.about", searchText))
					.add(Restrictions.eq("page.pageUrl", searchText)));
		}

		if(location != null && !location.trim().equals(""))
		{
			criteria.createAlias("page.events", "events");
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%"))
					.add(Restrictions.like("events.state", "%" + location + "%"))
					.add(Restrictions.like("events.stateCode", "%" + location + "%")));
		}

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Page> pageList = criteria.list();*/
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunityByLikeSearchKeywordForBusiness(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating) {
		logger.debug("getCommunityByLikeSearchKeyword ==> "+searchText);

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);


		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.createAlias("page.business", "business");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location))
					.add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%")));
		}

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%")).add(Restrictions.like("page.tag", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("page.about", searchText))
							.add(Restrictions.ne("page.pageUrl", searchText)));

		}

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Page> pageList = criteria.list();
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunityByLikeSearchKeywordForEvents(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating) {
		logger.debug("getCommunityByLikeSearchKeyword ==> "+searchText);
		List<Page> pageList = null;
		String hqlQuery = "";
		if(category.equalsIgnoreCase("NIGHTLIFE"))
		{
			hqlQuery = "select distinct a from Page a,Events b, Pagetype c where (c.pageType=:category or c.pageType='CONCERT' or c.pageType='THEATER') and a.pagetype.pageTypeId = c.pageTypeId and a.pageId = b.page.pageId";
		}
		else
			hqlQuery = "select distinct a from Page a,Events b, Pagetype c where c.pageType=:category and a.pagetype.pageTypeId = c.pageTypeId and a.pageId = b.page.pageId";

		if(searchText != null && !searchText.trim().equals(""))
		{
			hqlQuery = hqlQuery + "and ((a.about like '%" +searchText+ "%' or a.pageUrl like '%" +searchText+ "%' or a.tag like '%" +searchText+ "%') and (a.about !=:searchText))";
		}
		if(location != null && !location.trim().equals(""))
		{
			hqlQuery = hqlQuery + " and (b.zip = :location or b.city like '%" +location+ "%' or b.state like '%" +location+ "%' or b.stateCode like '%" +location+ "%')";
		}
		//hqlQuery = hqlQuery + " limit "+offset+","+limit;
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", category);
			if(searchText != null && !searchText.trim().equals(""))
				query.setParameter("searchText", searchText);
			if(location != null && !location.trim().equals(""))
				query.setParameter("location", location);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);


		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}

		if(location != null && !location.trim().equals(""))
		{
			criteria.createAlias("page.events", "events");
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%"))
					.add(Restrictions.like("events.state", "%" + location + "%"))
					.add(Restrictions.like("events.stateCode", "%" + location + "%")));
		}

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("page.about", searchText))
							.add(Restrictions.ne("page.pageUrl", searchText)));

		}

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Page> pageList = criteria.list();*/
		return pageList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsBySearchKeyword(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating) {
		logger.debug("getEventsBySearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("events.page", "page");

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%"))
					.add(Restrictions.like("events.state", "%" + location + "%"))
					.add(Restrictions.like("events.stateCode", "%" + location + "%")));
		}

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.eq("events.description", searchText))
					.add(Restrictions.eq("events.channel", "%" + searchText + "%"))
					.add(Restrictions.eq("events.ancestorGenreDescriptions", searchText))
					.add(Restrictions.eq("events.venue_name", searchText)));
		}

		logger.debug("Date: "+Utils.getNearestWeekendForEvent(null) +" : "+Utils.getCurrentDate());
		criteria.add(Restrictions.conjunction().add(Restrictions.ge("events.event_date_time_local", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date_time_local", Utils.getNearestWeekendForEvent(null))));

		String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin((latitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos((latitude*pi()/180)) * cos((((" + lon + ")- longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
		criteria.add(Restrictions.sqlRestriction(sql)); 

		criteria.addOrder(Order.asc("events.event_date_time_local"));

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Events> eventsList = criteria.list();
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating) {
		logger.debug("getEventsByLikeSearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("events.page", "page");

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%"))
					.add(Restrictions.like("events.state", "%" + location + "%"))
					.add(Restrictions.like("events.stateCode", "%" + location + "%")));
		}
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("events.description", "%" + searchText + "%"))
					.add(Restrictions.like("events.city", "%" + searchText + "%"))
					.add(Restrictions.like("events.ancestorGenreDescriptions", "%" + searchText + "%"))
					.add(Restrictions.like("events.venue_name", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("events.description", searchText))
							.add(Restrictions.ne("events.city", "%" + searchText + "%"))
							.add(Restrictions.ne("events.ancestorGenreDescriptions", searchText))
							.add(Restrictions.ne("events.venue_name", searchText)));
		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("events.event_date_time_local", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date_time_local", Utils.getNearestWeekendForEvent(null))));

		String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin((latitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos((latitude*pi()/180)) * cos((((" + lon + ")- longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
		criteria.add(Restrictions.sqlRestriction(sql)); 

		criteria.addOrder(Order.asc("events.event_date_time_local"));

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Events> eventsList = criteria.list();
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunityPageByCategoryType(String category, int userId) {
		logger.debug("getCommunityPageByCategoryType => "+userId +" => "+category);
		List<Page> pages = null;
		String hqlQuery = "select a from Page a , Pagetype b where a.pagetype.pageTypeId = b.pageTypeId and b.pageType =:category and a.pageId not in (select page.pageId from Pageuserlikes where userId =:userId and isFavorite is true) ";
		try{

			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("category", category);
			query.setParameter("userId", userId);
			pages  = (List<Page>)query.list();
		}catch(HibernateException e)
		{
			logger.debug("Error => "+e.getMessage());
			e.printStackTrace();
		}

		return pages;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getUserFavourites(Integer userId) {
		List<Page> pages = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class);
		criteria.createAlias("pageuserlikeses", "pageuserlikeses");
		criteria.add(Restrictions.eq("pageuserlikeses.user.userId", userId));
		criteria.add(Restrictions.eq("pageuserlikeses.isFavorite", true));
		pages = criteria.list();
		return pages;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getCommunityById(int pageId) {
		// TODO Auto-generated method stub
		List<Page> pages = null; 

		String hqlQuery = "from Page where pageId = :pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("pageId", pageId);
			pages = (List<Page>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug("IN DAO PAGE "+pages.get(0));
		return pages != null && !pages.isEmpty() ? pages.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Page getPageDetailsByID(int pageId)
	{
		List<Page> pageList = null;
		String hqlQuery = "from Page a where a.pageId = :pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageList != null && !pageList.isEmpty() ? pageList.get(0) : null; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getCurrentEvents(int pageId, int offset, int limit) {
		logger.debug("getCurrentEvents =>"+pageId + " => " + offset + " => "+limit);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(pageId!=0)
		{
			criteria.add(Restrictions.conjunction().add(Restrictions.eq("events.page.pageId", pageId))
					.add(Restrictions.ge("events.event_date_time_local", Utils.getCurrentDate()))
					.add(Restrictions.le("events.event_date_time_local", Utils.getNearestWeekend(null))));
			criteria.addOrder(Order.asc("events.event_date_time_local"));
		}
		criteria.setFirstResult(offset).setMaxResults(limit);
		List<Events> eventsList = criteria.list();
		logger.debug("current event list => "+eventsList.size());
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getUpcomingEvents(int pageId, int offset, int limit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(pageId!=0)
		{
			criteria.add(Restrictions.conjunction().add(Restrictions.eq("events.page.pageId", pageId))
					.add(Restrictions.gt("events.event_date_time_local", Utils.getNearestWeekend(null))));
			criteria.addOrder(Order.asc("events.event_date_time_local"));
		}
		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Events> eventsList = criteria.list();
		logger.debug("upcoming event list => "+eventsList.size());
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getPastEvents(int pageId, int offset, int limit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(pageId!=0)
		{
			criteria.add(Restrictions.conjunction().add(Restrictions.eq("events.page.pageId", pageId))
					.add(Restrictions.lt("events.event_date_time_local", Utils.getCurrentDate())));
			criteria.addOrder(Order.desc("events.event_date_time_local"));
		}
		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Events> eventsList = criteria.list();
		logger.debug("past event list => "+eventsList.size());
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getBookedEvents(int userId, String category) {
		logger.debug("getBookedEvents => ");
		List<Events> eventsList = null;
		logger.debug("get next weekend =>"+Utils.getNearestWeekend(null));
		String hqlQuery = "select a from Events a where a.eventId in (select b.event.eventId from Eventuserlikes b where b.user.userId=:userId and b.isBooked=1) and event_date_time_local>=:currentDate and event_date<=:nearestFriday and channel like '%" + category + "%'";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("nearestFriday", Utils.getNearestFriday());
			eventsList = (List<Events>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getUserEventsSuggestions(String userEmail, int offset,
			int limit, String category, String suggestionType, double lat, double lon, int distance, double rating) {
		logger.debug("getUserNonDealSuggestions ==> "+userEmail +" ==> "+offset+" ==> "+limit);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(userEmail != null && !userEmail.trim().equals(""))
		{
			criteria.createAlias("events.usereventsuggestions", "usereventsuggestions");
			criteria.createAlias("usereventsuggestions.user", "user");
			criteria.add(Restrictions.eq("user.emailId", userEmail));
		}

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("events.page", "page");
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}

		criteria.add(Restrictions.ge("usereventsuggestions.suggestedTime", Utils.getCurrentDate()));
		criteria.add(Restrictions.ge("events.event_date_time_local", Utils.getCurrentDate()));


		if(suggestionType != null)
		{
			if(suggestionType.equalsIgnoreCase("Friend Suggestion"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.like("usereventsuggestions.suggestionType", "%Friend%"))
						.add(Restrictions.like("usereventsuggestions.suggestionType", "%Group%"))
						.add(Restrictions.eq("usereventsuggestions.suggestionType", "Wall Feed Suggestion"))
						.add(Restrictions.eq("usereventsuggestions.suggestionType", "Direct Suggestion")));
			}
			else if(suggestionType.equalsIgnoreCase("Community Suggestion"))
			{
				criteria.add(Restrictions.like("usereventsuggestions.suggestionType", "%Community%"));
			}
			else
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.like("usereventsuggestions.suggestionType", "%User%"))
						.add(Restrictions.like("usereventsuggestions.suggestionType", "%Friend%"))
						.add(Restrictions.eq("usereventsuggestions.suggestionType", "Group")));
			}
		}

		/*if(category != null && !category.trim().equals(""))
		{
			criteria.add(Restrictions.eq("channel", "%" + category + "%"));
		}*/

		/*if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin((latitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos((lattitude*pi()/180)) * cos((((" + lon + ")- longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			criteria.add(Restrictions.sqlRestriction(sql)); 
		}*/

		criteria.setFirstResult(offset*10)
		.setMaxResults(limit);

		List<Events> eventsList = criteria.list();
		return eventsList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getEventLikeCountByPage(String idsStr) {
		logger.debug("getEventLikeCountByPage => ");
		List<Object[]> rows = null;
		String hqlQuery = "select distinct b.eventId, count(b.eventId) as num from Page a, Events b, Pageuserlikes c where a.pageId = c.page.pageId and b.page.pageId = a.pageId and b.eventId in ("+idsStr+") group by  a.about, b.eventId, b.title order by num desc";
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
	public List<Events> getEventsByuserLikes(String likekeyword,
			String itemCategory, String providerName, double latitude, double longitude) {
		logger.debug("getEventsByuserLikes ==> "+likekeyword +" : "+providerName+ " : "+itemCategory);
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

	  if(likekeyword != null && !likekeyword.trim().equals(""))
	  {
	   criteria.add(Restrictions.disjunction().add(Restrictions.like("events.description", "%" + likekeyword + "%"))
	     .add(Restrictions.like("events.title", "%" + likekeyword + "%"))
	     .add(Restrictions.eq("events.ancestorGenreDescriptions", likekeyword)));
	  }
	  if(providerName != null && !providerName.trim().equals(""))
	  {
	   criteria.add(Restrictions.eq("events.description", providerName.trim()));
	  }
	  if(itemCategory != null && !itemCategory.trim().equals(""))
	  {
	   criteria.add(Restrictions.disjunction().add(Restrictions.like("events.description", "%" + itemCategory + "%"))
	     .add(Restrictions.eq("events.title", itemCategory))
	     .add(Restrictions.like("events.ancestorGenreDescriptions", "%" + itemCategory + "%")));
	  }

	  criteria.add(Restrictions.ge("events.event_date", Utils.getCurrentDate()));

	  List<Events> eventList = criteria.list();*/
		List<Events> eventList = null;
		String hqlQuery = "select a from Events a, Page b, Pagetype c where c.pageType = :category and (a.event_date_time_local >= :currentDate and a.event_date_time_local <= :weekendDate) and a.page.pageId = b.pageId and b.pagetype.pageTypeId = c.pageTypeId and (a.description like '%" + likekeyword  +"%' or a.title like '%" + likekeyword  +"%' or a.ancestorGenreDescriptions like '%" + likekeyword  +"%' or a.urlpath like '%" + likekeyword  +"%') and (((acos(sin(((:latitude)*pi()/180)) * sin((a.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((a.latitude*pi()/180)) * cos((((:longitude)- a.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", itemCategory);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestWeekend(null));
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			eventList = (List<Events>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}

		return eventList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Events getEventById(int eventId) {
		List<Events> eventList = null;
		String hqlQuery = "from Events a where a.eventId = :eventId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("eventId", eventId);
			eventList = (List<Events>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventList != null && !eventList.isEmpty() ? eventList.get(0) : null;
	}

	@Override
	public int getTotalEventsBySearchKeyWord(String searchText,
			String category, String location, double lat, double lon, int distance, double rating) {
		logger.debug("getEventsByLikeSearchKeyword ==> "+searchText);
		/*	int rowCount = 0;
		String hqlQuery = "";

		if(rating != 0)
		{
			hqlQuery = "select a from Events a, Page b, Pagetype c, CommunityReviewMap d, CommunityReview e  ";
		}
		else
		{
			hqlQuery = "select distinct a from Page a,Events b, Pagetype c ";
		}

		if(category.equalsIgnoreCase("NIGHTLIFE"))
		{
			hqlQuery = "where (c.pageType=:category or c.pageType='CONCERT' or c.pageType='THEATER') and a.pagetype.pageTypeId = c.pageTypeId and a.pageId = b.page.pageId";
		}
		else
			hqlQuery = "where c.pageType=:category and a.pagetype.pageTypeId = c.pageTypeId and a.pageId = b.page.pageId";

		hqlQuery = hqlQuery + " and (a.event_date_time_local >= :currentDate and a.event_date_time_local <= :weekendDate)";

		if(searchText != null && !searchText.trim().equals(""))
		{
			//hqlQuery = hqlQuery + " and (a.description like '%" + likekeyword  +"%' or a.title like '%" + likekeyword  +"%' or a.ancestorGenreDescriptions like '%" + likekeyword  +"%' or a.urlpath like '%" + likekeyword  +"%')
			hqlQuery = hqlQuery + " and (a.description like '%" + searchText  +"%' or a.title like '%" + searchText  +"%' or a.ancestorGenreDescriptions like '%" + searchText  +"%' or a.urlpath like '%" + searchText  +"%')";

		}
		if(location != null && !location.trim().equals(""))
		{
			hqlQuery = hqlQuery + " and (a.zip = :location or a.city like '%" +location+ "%' or a.state like '%" +location+ "%' or a.stateCode like '%" +location+ "%')";
		}
		if(lat != 0 && lon != 0)
		{
			hqlQuery = hqlQuery + "and (((acos(sin(((:latitude)*pi()/180)) * sin((a.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((a.latitude*pi()/180)) * cos((((:longitude)- a.longitude)*pi()/180))))*180/pi())*60*1.1515) <=:distance";
		}
		if(rating != 0)
			hqlQuery = hqlQuery + " and b.pageId = c.pageId and c.reviewId = d.reviewId group by b.pageId having avgrating >= :rating";

		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", category);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestWeekendForEvent(null));
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

			query.setFirstResult(0);
			query.setMaxResults(10000);
			rowCount = ((Number)query.uniqueResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}*/

		/*
		 select a.pageId,avg(d.rating) as avgrating,a.zip,a.event_date_time_local from events a, page b, communityReviewMap c, communityReview d, pagetype e where a.pageId = b.pageId and b.pageId = c.pageId and c.reviewId = d.reviewId and e.pageType = "sport" and b.pageTypeId = e.pageTypeId and (a.event_date_time_local >= now()) and (((acos(sin(((42.3589)*pi()/180)) * sin((a.latitude*pi()/180))+cos(((42.3589)*pi()/180)) * cos((a.latitude*pi()/180)) * cos((((-71.0578)- a.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50000 group by b.pageId having avgrating >= 4;
		 */

		/*	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.count("events.eventId"));


		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%"))
					.add(Restrictions.like("events.state", "%" + location + "%"))
					.add(Restrictions.like("events.stateCode", "%" + location + "%")));
		}
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("events.description", "%" + searchText + "%"))
					.add(Restrictions.like("events.city", "%" + searchText + "%"))
					.add(Restrictions.like("events.ancestorGenreDescriptions", "%" + searchText + "%"))
					.add(Restrictions.like("events.venue_name", "%" + searchText + "%")));
		}

		//criteria.createAlias("events.page", "page").setProjection(Projections.property("page.pageId").as("pagePageId"));

		if(rating != 0)
		{
			criteria.createAlias("page.communityReviewMap", "communityReviewMap");
			criteria.createAlias("communityReviewMap.communityReview", "communityReview");
			criteria.setProjection(Projections.avg("communityReview.rating").as("avgRating"));
			String groupBy = "pageId having " + "avgRating >= " + rating;
			String[] alias = new String[1]; 
			alias[0] = "pagePageId"; 
			Type[] types = new Type[1]; 
			types[0] = Hibernate.INTEGER;	
			criteria.setProjection(Projections.alias(Projections.property("page.pageId"), "pageId1"));
			criteria.setProjection(Projections.sqlGroupProjection("page", groupBy, alias, types));

		}


		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("events.event_date_time_local", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date_time_local", Utils.getNearestWeekendForEvent(null))));

		String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin((latitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos((latitude*pi()/180)) * cos((((" + lon + ")- longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
		criteria.add(Restrictions.sqlRestriction(sql)); 

		criteria.setFirstResult(0)
		.setMaxResults(10000);

		//int rowCount = criteria.list().size();
		int rowCount = Integer.parseInt(criteria.uniqueResult().toString());*/

		logger.debug("getEventsByLikeSearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.count("events.eventId"));

		criteria.createAlias("events.page", "page");

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%"))
					.add(Restrictions.like("events.state", "%" + location + "%"))
					.add(Restrictions.like("events.stateCode", "%" + location + "%")));
		}
		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("events.description", "%" + searchText + "%"))
					.add(Restrictions.like("events.city", "%" + searchText + "%"))
					.add(Restrictions.like("events.ancestorGenreDescriptions", "%" + searchText + "%"))
					.add(Restrictions.like("events.venue_name", "%" + searchText + "%")));
		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("events.event_date_time_local", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date_time_local", Utils.getNearestWeekendForEvent(null))));
		criteria.setFirstResult(0)
		.setMaxResults(10000);

		//int rowCount = criteria.list().size();
		int rowCount = Integer.parseInt(criteria.uniqueResult().toString());

		return rowCount;
	}

	@Override
	public int getTotalCommunityBySearchKeyWordForBusiness(String searchText,
			String category, String location, double lat, double lon, int distance, double rating) {
		logger.debug("getTotalCommunityBySearchKeyWord ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.count("page.pageId"));


		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.createAlias("page.business", "business");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location))
					.add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%")));
		}

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%"))
					.add(Restrictions.like("page.tag", "%" + searchText + "%")));

		}

		/*if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin((lattitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos((lattitude*pi()/180)) * cos((((" + lon + ")- longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			criteria.add(Restrictions.sqlRestriction(sql)); 
		}*/

		//int rowCount = criteria.list().size();
		int rowCount = Integer.parseInt(criteria.uniqueResult().toString());
		return rowCount;
	}


	@Override
	public int getTotalCommunityBySearchKeyWordForEvent(String searchText,
			String category, String location, double lat, double lon, int distance, double rating) {
		logger.debug("getTotalCommunityBySearchKeyWord ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.count("page.pageId"));

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.createAlias("page.events", "events");
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%"))
					.add(Restrictions.like("events.state", "%" + location + "%"))
					.add(Restrictions.like("events.stateCode", "%" + location + "%")));
		}

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%"))
					.add(Restrictions.like("page.tag", "%" + searchText + "%")));

		}

		/*if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin((lattitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos((lattitude*pi()/180)) * cos((((" + lon + ")- longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			criteria.add(Restrictions.sqlRestriction(sql)); 
		}
		 */
		//int rowCount = criteria.list().size();
		int rowCount = Integer.parseInt(criteria.uniqueResult().toString());
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagetype getPageTypeByName(String pageType) {
		logger.debug("getPageTypeByName :: "+pageType);
		List<Pagetype> pageList = null;
		String hqlQuery = "from Pagetype a where a.pageType =:pageType";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageType", pageType);
			pageList = (List<Pagetype>) query.list();
			logger.debug("getPageTypeByName pageList:: "+pageList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageList != null && !pageList.isEmpty() ? pageList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getPageByAbout(String genre_parent_name)
	{
		List<Page> pageList = null;
		String hqlQuery = "from Page a where a.about =:genre_parent_name";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("genre_parent_name", genre_parent_name);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			logger.debug("Error ==> "+e.getMessage());
			e.printStackTrace();
		}
		return pageList != null && !pageList.isEmpty() ? pageList.get(0) : null;
	}

	@Override
	public void savePage(Page page, int batch_size) {
		logger.debug("savePage ==>");
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(page);	
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			logger.debug(e.toString());
		}
		catch (Exception e) {
			logger.debug(e.toString());
		}
		finally
		{
			session.close();
		}

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<CommunityComments> getCommunityCommentsByPageId(int pageId) {
		logger.debug("getCommunityCommentsByPageId =>"+pageId);
		List<CommunityComments> communityCommentstList = null;
		String hqlQuery = "from CommunityComments a where a.page.pageId = :pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			communityCommentstList = (List<CommunityComments>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return communityCommentstList;
	}

	@Override
	public void saveCommunityComments(CommunityComments communityComments) {

		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(communityComments);
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
	public Page getPageByBusinessId(Integer itemId)
	{
		List<Page> pageList = null;
		String hqlQuery = "from Page a where a.business.id =:itemId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("itemId", itemId);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageList != null && !pageList.isEmpty() ? pageList.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Eventuserlikes isEventExistInUserProfile(int userId, int eventId) {
		logger.debug("isEventExistInUserProfile userId : ==>"+userId + "; eventId: "+ eventId);
		List<Eventuserlikes> list = null;
		String queryString = "from Eventuserlikes where user.userId =:userId and event.eventId =:eventId";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(queryString);
			query.setParameter("userId", userId);
			query.setParameter("eventId", eventId);
			list = query.list();

		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.debug(e.toString());
		}

		logger.debug("EventUser Like " + list);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public boolean updateUserEvents(Eventuserlikes eventuserlikes) {
		try{
			sessionFactory.getCurrentSession().update(eventuserlikes);
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			return false;
		}
	}


	@Override
	public boolean updatePageUserLikes(Pageuserlikes pageuserlikes) {
		try{
			sessionFactory.getCurrentSession().update(pageuserlikes);
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			return false;
		}


	}

	@Override
	public boolean saveUserEvents(Eventuserlikes eventuserlikes) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(eventuserlikes);
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean savePageUserLikes(Pageuserlikes pageuserlikes) {
		try{
			sessionFactory.getCurrentSession().save(pageuserlikes);
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			return false;
		}

	}

	@Override
	public void saveCommunityReview(CommunityReviewMap communityReviewMap) {
		try{
			sessionFactory.getCurrentSession().save(communityReviewMap);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	@Override
	public double getCommunityReviewByPageId(int pageId)
	{
		double rating = 0;
		String hqlQuery = "select avg(a.rating) from CommunityReview a, CommunityReviewMap b where b.page.pageId=:pageId and b.communityReview.reviewId=a.reviewId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			rating = ((Number)query.uniqueResult()).doubleValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rating;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getRecentEventDate(int pageId) {
		List<Object[]> eventList = null;
		String hqlQuery = "select b.event_date_local,b.event_time_local from Page a, Events b where a.pageId = :pageId and a.pageId = b.page.pageId and b.event_date_time_local >= :currentDate order by b.event_date_time_local asc limit 1";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			eventList = (List<Object[]>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventList != null && !eventList.isEmpty() ? eventList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getRecentEventDetails(int pageId) {
		List<Object[]> eventList = null;
		String hqlQuery = "select b.event_date_local,b.event_time_local,b.latitude,b.longitude,b.zip from Page a, Events b where a.pageId = :pageId and a.pageId = b.page.pageId and b.event_date_time_local >= :currentDate order by b.event_date_time_local asc limit 1";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			eventList = (List<Object[]>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventList != null && !eventList.isEmpty() ? eventList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsByuserLikesForCurrentWeekend(
			String likekeyword, String itemCategory, String providerName,
			double latitude, double longitude) {
		List<Events> eventList = null;
		String hqlQuery = "select a from Events a, Page b, Pagetype c where c.pageType = :category and (a.event_date_time_local >= :currentDate and a.event_date_time_local <= :weekendDate) and a.page.pageId = b.pageId and b.pagetype.pageTypeId = c.pageTypeId and (a.description like '%" + likekeyword  +"%' or a.title like '%" + likekeyword  +"%' or a.ancestorGenreDescriptions like '%" + likekeyword  +"%' or a.urlpath like '%" + likekeyword  +"%') and (((acos(sin(((:latitude)*pi()/180)) * sin((a.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((a.latitude*pi()/180)) * cos((((:longitude)- a.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", itemCategory);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestWeekend(null));
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			eventList = (List<Events>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}

		return eventList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunityBySearchKeyword(String searchText,
			String category, int offset, int limit, String abbreviation) {
		logger.debug("getCommunityBySearchKeyword =>"+searchText + " => " + category);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(searchText != null && !searchText.trim().equals("") || abbreviation != null && !abbreviation.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%"))
					.add(Restrictions.like("page.tag", "%" + searchText + "%"))
					.add(Restrictions.like("page.about", "%" + abbreviation + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + abbreviation + "%"))
					.add(Restrictions.like("page.tag", "%" + abbreviation + "%")));

		}
		/*if(abbreviation != null && !abbreviation.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + abbreviation + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + abbreviation + "%"))
					.add(Restrictions.like("page.tag", "%" + abbreviation + "%")));
		}*/

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Page> pageList = criteria.list();
		logger.debug("current event list => "+pageList.size());
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getRecentEvent(int pageId) {
		List<Integer> eventList = null;
		String hqlQuery = "select b.eventId from Page a, Events b where a.pageId = :pageId and a.pageId = b.page.pageId and b.event_date_time_local >= :currentDate order by b.event_date_time_local asc limit 1";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			eventList = (List<Integer>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventList != null && !eventList.isEmpty() ? eventList.get(0) : 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getEventsByPgaeId(int pageId) {
		List<Integer> eventList = null;
		String hqlQuery = "select b.eventId from Page a, Events b where a.pageId = :pageId and a.pageId = b.page.pageId and b.event_date_time_local >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			eventList = (List<Integer>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getPagesAvergeRatingAndDetails(int pageId) {
		logger.debug("getTopCommunitySuggestions ==> "+pageId);
		List<Object[]> rows = null;

		String hqlQuery = "select avg(c.rating) as rating, a from Page a left Join a.communityReviewMap b left join b.communityReview c where a.pageId = :pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("pageId", pageId);
			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			logger.debug("Error: "+e.getMessage());
			e.printStackTrace();
		}
		return rows != null && !rows.isEmpty() ? rows.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PopularCommunity> getPopularCommunityList() {
		List<PopularCommunity> communityList = null;
		String hqlQuery = "from PopularCommunity";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			communityList = (List<PopularCommunity>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return communityList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagetype getPopularComunityType(int communityId) {
		logger.debug("pageList CommunityType => ");
		List<Pagetype> pageList = null;
		String hqlQuery = "select b from PopularCommunity a, Pagetype b where a.communityId = :communityId and a.pagetype.pageTypeId = b.pageTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("communityId", communityId);
			pageList = (List<Pagetype>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return pageList != null && !pageList.isEmpty() ? pageList.get(0) : null;
	}


	@Override
	public int getTotalCommunityForCommunitySearch(String searchText,
			String category, String location, String abbreviation) {
		logger.debug("getTotalCommunityBySearchKeyWord ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.count("page.pageId"));


		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			if(category.equalsIgnoreCase("NIGHTLIFE"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("pagetypes.pageType", category))
						.add(Restrictions.eq("pagetypes.pageType", "THEATER"))
						.add(Restrictions.eq("pagetypes.pageType", "CONCERT")));
			}
			else
				criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		if(location != null && !location.trim().equals(""))
		{
			criteria.createAlias("page.events", "events");
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%"))
					.add(Restrictions.like("events.state", "%" + location + "%"))
					.add(Restrictions.like("events.stateCode", "%" + location + "%")));
		}

		if(searchText != null && !searchText.trim().equals("") || abbreviation != null && !abbreviation.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%"))
					.add(Restrictions.like("page.tag", "%" + searchText + "%"))
					.add(Restrictions.like("page.about", "%" + abbreviation + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + abbreviation + "%"))
					.add(Restrictions.like("page.tag", "%" + abbreviation + "%")));

		}
		/*if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%"))
					.add(Restrictions.like("page.tag", "%" + searchText + "%")));
		}
		if(abbreviation != null && !abbreviation.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + abbreviation + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + abbreviation + "%"))
					.add(Restrictions.like("page.tag", "%" + abbreviation + "%")));
		}*/

		int rowCount = Integer.parseInt(criteria.uniqueResult().toString());
		return rowCount;
	}
}
