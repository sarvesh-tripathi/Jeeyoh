package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;
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
	public List<Page> getCommunityBySearchKeyword(String searchText,String category, String location) {
		logger.debug("getCommunityBySearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.eq("page.about", searchText))
					.add(Restrictions.eq("page.pageUrl", searchText)));
		}

		List<Page> pageList = criteria.list();
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getCommunityByLikeSearchKeyword(String searchText,String category, String location) {
		logger.debug("getCommunityByLikeSearchKeyword ==> "+searchText);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("page.pagetype", "pagetypes");
			criteria.add(Restrictions.eq("pagetypes.pageType", category));
		}
		/*if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("events.zip", location))
					.add(Restrictions.like("events.city", "%" + location + "%")));
		}*/

		if(searchText != null && !searchText.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("page.about", "%" + searchText + "%"))
					.add(Restrictions.like("page.pageUrl", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("page.about", searchText))
							.add(Restrictions.ne("page.pageUrl", searchText)));

		}

		List<Page> pageList = criteria.list();
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsBySearchKeyword(String searchText,String category, String location) {
		logger.debug("getEventsByCriteria ==> "+searchText);
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

		List<Events> eventsList = criteria.list();
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getEventsByLikeSearchKeyword(String searchText,String category, String location) {
		logger.debug("getEventsByCriteria ==> "+searchText);
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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class, "page").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("page.pageId", pageId));
		List<Page> pageList = criteria.list();
		logger.debug("page list => "+pageList);
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

}
