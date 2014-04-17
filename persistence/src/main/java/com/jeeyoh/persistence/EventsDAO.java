package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.CommunityComments;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.utils.Utils;

@Repository("eventsDAO")
public class EventsDAO implements IEventsDAO{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveEvents(Events events, int batch_size) {
		logger.debug("saveEvents ==>");
		/*Session session = sessionFactory.openSession();
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
		}*/
		Session session =  sessionFactory.getCurrentSession();
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
			e.printStackTrace(); 
		}

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
	public List<Page> getCommunityBySearchKeyword(String searchText,String category, String location, int offset, int limit) {
		logger.debug("getCommunityBySearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("page.business", "business");
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
	public List<Page> getCommunityByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit) {
		logger.debug("getCommunityByLikeSearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("page.business", "business");
		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			criteria.add(Restrictions.eq("pagetypes.pageType", category));
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

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("page.about", searchText))
							.add(Restrictions.ne("page.pageUrl", searchText)));

		}

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Page> pageList = criteria.list();
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsBySearchKeyword(String searchText,String category, String location, int offset, int limit) {
		logger.debug("getEventsBySearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("events.page", "page");

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
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
		criteria.add(Restrictions.conjunction().add(Restrictions.ge("events.event_date", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date", Utils.getNearestWeekendForEvent(null))));

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Events> eventsList = criteria.list();
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit) {
		logger.debug("getEventsByLikeSearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("events.page", "page");

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
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

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("events.event_date", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date", Utils.getNearestWeekendForEvent(null))));

		criteria.addOrder(Order.asc("events.event_date"));

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Events> eventsList = criteria.list();
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunityPageByCategoryType(String category, int userId) {
		// TODO Auto-generated method stub
		List<Page> pages = null;

		String hqlQuery = "select a from Page a , Pagetype b where a.pagetype.pageTypeId = b.pageTypeId and b.pageType =:category and a.pageId not in (select page.pageId from Pageuserlikes where userId =:userId and isFavorite is true) ";
		try{

			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("category", category);
			query.setParameter("userId", userId);
			pages  = (List<Page>)query.list();
		}catch(HibernateException e)
		{
			e.printStackTrace();
		}

		return pages;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getUserFavourites(Integer userId) {
		// TODO Auto-generated method stub
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
					.add(Restrictions.ge("events.event_date", Utils.getCurrentDate()))
					.add(Restrictions.le("events.event_date", Utils.getNearestWeekend(null))));
			criteria.addOrder(Order.asc("events.event_date"));
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
					.add(Restrictions.gt("events.event_date", Utils.getNearestWeekend(null))));
			criteria.addOrder(Order.asc("events.event_date"));
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
					.add(Restrictions.lt("events.event_date", Utils.getCurrentDate())));
			criteria.addOrder(Order.desc("events.event_date"));
		}
		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Events> eventsList = criteria.list();
		logger.debug("past event list => "+eventsList.size());
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getBookedEvents(int userId) {
		logger.debug("getBookedEvents => ");
		List<Events> eventsList = null;
		logger.debug("get next weekend =>"+Utils.getNearestWeekend(null));
		String hqlQuery = "select a from Events a where a.eventId in (select b.event.eventId from Eventuserlikes b where b.user.userId=:userId and b.isBooked=1) and event_date>=:currentDate and event_date<=:nearestFriday";
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
			int limit) {
		logger.debug("getUserNonDealSuggestions ==> "+userEmail +" ==> "+offset+" ==> "+limit);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(userEmail != null && !userEmail.trim().equals(""))
		{
			criteria.createAlias("events.usereventsuggestions", "usereventsuggestions");
			criteria.createAlias("usereventsuggestions.user", "user");
			criteria.add(Restrictions.eq("user.emailId", userEmail));
		}
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
		String hqlQuery = "select a from Events a, Page b, Pagetype c where c.pageType = :category and a.event_date >= :currentDate and a.page.pageId = b.pageId and b.pagetype.pageTypeId = c.pageTypeId and (a.description like '%" + likekeyword  +"%' or a.title like '%" + likekeyword  +"%' or a.ancestorGenreDescriptions like '%" + likekeyword  +"%' or a.urlpath like '%" + likekeyword  +"%') and (((acos(sin(((:latitude)*pi()/180)) * sin((a.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((a.latitude*pi()/180)) * cos((((:longitude)- a.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", itemCategory);
			query.setParameter("currentDate", Utils.getCurrentDate());
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
			String category, String location) {
		logger.debug("getEventsByLikeSearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setProjection(Projections.property("events.eventId"));

		criteria.createAlias("events.page", "page");

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
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

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("events.event_date", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date", Utils.getNearestWeekendForEvent(null))));
		criteria.setFirstResult(0)
		.setMaxResults(10000);
		
		int rowCount = criteria.list().size();
		return rowCount;
	}

	@Override
	public int getTotalCommunityBySearchKeyWord(String searchText,
			String category, String location) {
		logger.debug("getTotalCommunityBySearchKeyWord ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).setProjection(Projections.property("page.pageId"));

		criteria.createAlias("page.business", "business");
		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			criteria.add(Restrictions.eq("pagetypes.pageType", category));
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

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%")));

		}

		int rowCount = criteria.list().size();
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagetype getPageTypeByName(String pageType) {
		List<Pagetype> pageList = null;
		String hqlQuery = "from Pagetype a where a.pageType =:pageType";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageType", pageType);
			pageList = (List<Pagetype>) query.list();
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
	public void updateUserEvents(Eventuserlikes eventuserlikes) {
		sessionFactory.getCurrentSession().update(eventuserlikes);

	}

	@Override
	public void saveUserEvents(Eventuserlikes eventuserlikes) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(eventuserlikes);
		}
		catch(Exception e)
		{
			logger.error(e.toString());
		}
	}

}
