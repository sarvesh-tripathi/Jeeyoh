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

import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Page;
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
			e.printStackTrace(); 
		}
		 */
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
					.add(Restrictions.like("business.city", "%" + location + "%")));
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
					.add(Restrictions.like("business.city", "%" + location + "%")));
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
					.add(Restrictions.like("events.city", "%" + location + "%")));
		}

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.eq("events.description", searchText))
					.add(Restrictions.eq("events.channel", "%" + searchText + "%"))
					.add(Restrictions.eq("events.ancestorGenreDescriptions", searchText))
					.add(Restrictions.eq("events.venue_name", searchText)));
		}

		logger.debug("Date: "+Utils.getNearestWeekend(null) +" : "+Utils.getCurrentDate());
		criteria.add(Restrictions.conjunction().add(Restrictions.ge("events.event_date", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date", Utils.getNearestWeekend(null))));

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
					.add(Restrictions.like("events.city", "%" + location + "%")));
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
				.add(Restrictions.le("events.event_date", Utils.getNearestWeekend(null))));

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
	public List<Events> getCurrentEvents(int pageId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.conjunction().add(Restrictions.eq("events.page.pageId", pageId))
				.add(Restrictions.ge("events.event_date", Utils.getCurrentDate()))
				.add(Restrictions.le("events.event_date", Utils.getNearestWeekend(null))));

		List<Events> eventsList = criteria.list();
		logger.debug("current event list => "+eventsList.size());
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getUpcomingEvents(int pageId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.conjunction().add(Restrictions.eq("events.page.pageId", pageId))
				.add(Restrictions.gt("events.event_date", Utils.getNearestWeekend(null))));

		List<Events> eventsList = criteria.list();
		logger.debug("upcoming event list => "+eventsList.size());
		return eventsList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getPastEvents(int pageId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.conjunction().add(Restrictions.eq("events.page.pageId", pageId))
				.add(Restrictions.lt("events.event_date", Utils.getCurrentDate())));

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
	   String itemCategory, String providerName) {
	  logger.debug("getEventsByuserLikes ==> "+likekeyword +" : "+providerName+ " : "+itemCategory);
	  Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	  
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
	  List<Events> eventList = criteria.list();
	 
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

}
