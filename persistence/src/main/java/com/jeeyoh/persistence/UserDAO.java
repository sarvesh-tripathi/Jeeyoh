package com.jeeyoh.persistence;

import java.util.ArrayList;
import java.util.List;

import javassist.bytecode.analysis.Util;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.Profiletype;
import com.jeeyoh.persistence.domain.Topcommunitysuggestion;
import com.jeeyoh.persistence.domain.Topdealssuggestion;
import com.jeeyoh.persistence.domain.Topeventsuggestion;
import com.jeeyoh.persistence.domain.Topnondealsuggestion;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.UserSession;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.utils.Utils;

@Repository("userDAO")
public class UserDAO implements IUserDAO {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers() {
		logger.debug("userList => ");
		List<User> userList = null;
		String hqlQuery = "from User";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);			
			userList = (List<User>) query.list();
			logger.debug("userList => " + userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserContacts(int userId) {
		List<User> contactList = null;
		//String hqlQuery = "select a from User a, Usercontacts b where b.userByUserId.userId = :userId and a.userId = b.userByContactId.userId and b.isApproved is true";
		String hqlQuery = "select a from User a, Usercontacts b where ((b.userByUserId.userId = :userId and a.userId = b.userByContactId.userId) or (b.userByContactId.userId = :userId and a.userId = b.userByUserId.userId)) and b.isApproved is true group by a.userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			contactList = (List<User>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contactList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllUserContacts(int userId) {
		logger.debug("getAllUserContacts userId::  "+userId);
		List<Object[]> contactList = null;
		//String hqlQuery = "from Usercontacts b where b.userByUserId.userId = :userId and b.isApproved is true";
		String hqlQuery = "select a,b from User a, Usercontacts b where (b.userByUserId.userId = :userId and a.userId = b.userByContactId.userId) or (b.userByContactId.userId = :userId and a.userId = b.userByUserId.userId) and b.isApproved is true and (a.isShareProfileWithFriend is true or a.isShareCommunity is true) group by a.userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			contactList =  (List<Object[]>)query.list();
		} catch (Exception e) {
			logger.debug("ERROR: "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return contactList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getUserCommunities(int userId, double latitude, double longitude) {
		logger.debug("pageList => ");
		List<Page> pageList = null;
		//String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c where a.userId = :userId and c.user.userId = a.userId and b.pageId = c.page.pageId and b.business.id is not null";
		String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c, Business d where a.userId = :userId and c.user.userId = a.userId and b.pageId = c.page.pageId and b.business.id = d.id and b.business.id is not null and (((acos(sin(((:latitude)*pi()/180)) * sin((d.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((d.lattitude*pi()/180)) * cos((((:longitude)- d.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			pageList = (List<Page>) query.list();
			//logger.debug("pageList => " + pageList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pagetype> getCommunityType(int pageId) {
		logger.debug("pageList CommunityType => ");
		List<Pagetype> pageList = null;
		String hqlQuery = "select b from Page a, Pagetype b where a.pageId = :pageId and a.pagetype.pageTypeId = b.pageTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			pageList = (List<Pagetype>) query.list();
			//logger.debug("pageList => " + pageList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return pageList;
	}

	@Override
	public List<Page> getUserContactsCommunities(int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pageuserlikes getUserPageProperties(int userId, int pageId) {
		List<Pageuserlikes> pageProperties = null;
		String hqlQuery = "select c from User a, Page b, Pageuserlikes c where a.userId = :userId and b.pageId = :pageId and c.user.userId = a.userId and b.pageId = c.page.pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("pageId", pageId);
			pageProperties = (List<Pageuserlikes>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageProperties != null && !pageProperties.isEmpty() ? pageProperties.get(0) : null;
	}

	@Override
	public void saveNonDealSuggestions(Usernondealsuggestion suggestion, int batch_size) {
		logger.debug("saveNonDealSuggestions => ");
		/* Session session  = sessionFactory.openSession();
		  Transaction tx = session.beginTransaction()*/;
		  Session session =  sessionFactory.getCurrentSession();
		  try
		  {
			  session.save(suggestion);
			  if(batch_size % 20 == 0)
			  {
				  session.flush();
				  session.clear();

			  }
			  /*session.save(suggestion);    
		   tx.commit();
		   session.close();*/
		  }
		  catch(HibernateException e)
		  {
			  e.printStackTrace();
			  logger.error("ERROR IN DAO :: = > "+e);
		  }

	}

	@SuppressWarnings("unchecked")
	@Override
	public User getUserById(int userId) {
		logger.debug("userList => ");
		List<User> userList = null;
		String hqlQuery = "from User a where a.userId = :userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("userId", userId);
			userList = (List<User>) query.list();
			logger.debug("userList => " + userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList != null && !userList.isEmpty() ? userList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getUserCommunityEvents(int userId, int pageId) {
		logger.debug("getUserCommunityEvents => ");
		List<Events> eventsList = null;
		String hqlQuery = "select b from User a, Events b, Eventuserlikes c where a.userId = :userId and b.page.pageId = :pageId and c.user.userId = a.userId and b.eventId = c.event.eventId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
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
	public User getUserByEmailId(String emailId) {
		List<User> userList = null;
		logger.debug("Email id ::: = "+emailId);
		Session session = sessionFactory.getCurrentSession();
		//Transaction tx = null;
		String hqlQuery = "from User a where a.emailId = :emailId";
		try{
			//tx = session.beginTransaction();
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("emailId", emailId);
			userList = (List<User>)query.list();
			//tx.commit();
		}
		catch (HibernateException e) {
			//if (tx!=null) tx.rollback();
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			//session.close();
		}
		logger.debug("user:: "+userList);
		return userList != null && !userList.isEmpty() ? userList.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Eventuserlikes getUserEventProperties(int userId, int eventId) {
		List<Eventuserlikes> eventProperties = null;
		String hqlQuery = "select c from User a, Events b, Eventuserlikes c where a.userId = :userId and c.event.eventId = :eventId and c.user.userId = a.userId and b.eventId = c.event.eventId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("eventId", eventId);
			eventProperties = (List<Eventuserlikes>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventProperties != null && !eventProperties.isEmpty() ? eventProperties.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usernondealsuggestion> isNonDealSuggestionExists(int userId,
			int businessId) {

		logger.debug("UserId ==> "+userId +" businessid ==> "+businessId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usernondealsuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("business.id", businessId));
		criteria.add(Restrictions.ne("suggestionType", "Direct Suggestion"));

		List<Usernondealsuggestion> usernondealsuggestions = criteria.list();
		return usernondealsuggestions;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getUserCommunitiesByEmailId(String emailId) {

		List<Page> pageList = null;
		String hqlQuery = "select b from User a, Page b, Pageuserlikes c where a.emailId = :emailId and c.user.userId = a.userId and b.pageId = c.page.pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("emailId",emailId);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return pageList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<UserCategory> getUserCategoryLikesById(int userId) {
		logger.debug("getUserCategoryLikesById => ");
		List<UserCategory> userCategoryList = null;
		String hqlQuery = "select b from User a, UserCategory b, UserCategoryLikes c where a.userId = :userId and c.user.userId = a.userId and b.userCategoryId = c.userCategory.userCategoryId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			userCategoryList = (List<UserCategory>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return userCategoryList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usernondealsuggestion> getuserNonDealSuggestionsByEmailId(
			String emailId) {
		List<Usernondealsuggestion> usernondealsuggestions = null;
		String hqlQuery = "select a from Usernondealsuggestion a, User b where b.emailId = :emailId and a.user.userId = b.userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("emailId", emailId);
			usernondealsuggestions = (List<Usernondealsuggestion>) query.list();
		} catch (Exception e) {
			logger.debug("ERROR::::  "+e.getMessage());
			e.printStackTrace();
		}
		return usernondealsuggestions;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<UserCategory> getUserCategoryLikesByType(int userId,
			String category) {
		logger.debug("getUserCategoryLikesByType => ");
		List<UserCategory> userCategoryList = null;
		String hqlQuery = "select b from User a, UserCategory b, UserCategoryLikes c where a.userId = :userId and";
		
		if(category != null && !category.trim().equals(""))
		{
			if(category.equalsIgnoreCase("NIGHTLIFE"))
				hqlQuery = hqlQuery + " (b.itemCategory = :category or b.itemCategory = 'CONCERT' or b.itemCategory = 'THEATER') and";
			else
				hqlQuery = hqlQuery + " b.itemCategory =:category and";	
		}
		hqlQuery += "c.user.userId = a.userId and b.userCategoryId = c.userCategory.userCategoryId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("category", category);
			userCategoryList = (List<UserCategory>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return userCategoryList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getUserCommunitiesByPageType(int userId, String pageType, double latitude, double longitude) {
		logger.debug("pageList => ");
		List<Page> pageList = null;
		//String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c, Pagetype d where a.userId = :userId and d.pageType = :pageType and d.pageTypeId = b.pagetype.pageTypeId and c.user.userId = a.userId and b.pageId = c.page.pageId and b.business.id is not null";
		String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c, Pagetype d, Business e where a.userId = :userId and d.pageType = :pageType and d.pageTypeId = b.pagetype.pageTypeId and c.user.userId = a.userId and b.pageId = c.page.pageId  and b.business.id = e.id and b.business.id is not null and (((acos(sin(((:latitude)*pi()/180)) * sin((e.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((e.lattitude*pi()/180)) * cos((((:longitude)- e.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("pageType", pageType);
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return pageList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getUserBusinessByPageType(int userId, String pageType, double latitude, double longitude) {
		logger.debug("pageList => ");
		List<Business> businessList = null;
		//String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c, Pagetype d where a.userId = :userId and d.pageType = :pageType and d.pageTypeId = b.pagetype.pageTypeId and c.user.userId = a.userId and b.pageId = c.page.pageId and b.business.id is not null";
		String hqlQuery = "select distinct e from User a, Page b, Pageuserlikes c, Pagetype d, Business e where a.userId = :userId and d.pageType = :pageType and d.pageTypeId = b.pagetype.pageTypeId and c.user.userId = a.userId and b.pageId = c.page.pageId  and b.business.id = e.id and b.business.id is not null and (((acos(sin(((:latitude)*pi()/180)) * sin((e.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((e.lattitude*pi()/180)) * cos((((:longitude)- e.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("pageType", pageType);
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			businessList = (List<Business>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return businessList;
	}

	@Override
	public int userCategoryLikeCount(Integer userCategoryId) {
		logger.debug("userCategoryLikeCount => ");
		int rowCount = 0;
		String hqlQuery = "select count(a.userCategoryLikesId) from UserCategoryLikes a where a.userCategory.userCategoryId = :userCategoryId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userCategoryId", userCategoryId);

			rowCount = ((Number)query.uniqueResult()).intValue();

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return rowCount;
	}

	@Override
	public void saveEventsSuggestions(Usereventsuggestion suggestion,
			int batch_size) {
		logger.debug("saveEventsSuggestions => ");
		/* Session session  = sessionFactory.openSession();
		  Transaction tx = session.beginTransaction()*/;
		  Session session =  sessionFactory.getCurrentSession();
		  try
		  {
			  session.save(suggestion);
			  if(batch_size % 20 == 0)
			  {
				  session.flush();
				  session.clear();

			  }
			  /*session.save(suggestion);    
		   tx.commit();
		   session.close();*/
		  }
		  catch(HibernateException e)
		  {
			  e.printStackTrace();
			  logger.error("ERROR IN DAO :: = > "+e);
		  }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getUserLikesEvents(int userId, double latitud, double longitude) {
		logger.debug("getUserLikesEvents => ");
		List<Events> eventsList = null;
		String hqlQuery = "select b from User a, Events b, Eventuserlikes c where a.userId = :userId and c.user.userId = a.userId and b.eventId = c.event.eventId and (b.event_date_time_local >= :currentDate and b.event_date_time_local <= :weekendDate) and (((acos(sin(((:latitude)*pi()/180)) * sin((b.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((b.latitude*pi()/180)) * cos((((:longitude)- b.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestWeekend(null));
			query.setDouble("latitude", latitud);
			query.setDouble("longitude", longitude);
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
	public List<Events> getUserLikesEventsByType(int userId, String pageType, double latitud, double longitude) {
		logger.debug("getUserLikesEventsByType => ");
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class);
		criteria.add(Restrictions.eq("eventuserlikes.user.userId", userId));
		criteria.add(Restrictions.ge("event_date", Utils.getCurrentDate()));
		criteria.createAlias("page", "page");
		criteria.createAlias("page.pagetype", "pagetype");  
		criteria.add(Restrictions.eq("pagetype.pageType", pageType));  
		return criteria.list();*/

		List<Events> eventsList = null;
		String hqlQuery = "select b from User a, Events b, Eventuserlikes c where a.userId = :userId and c.user.userId = a.userId and b.page.pagetype.pageType = :pageType and b.eventId = c.event.eventId and (b.event_date_time_local >= :currentDate and b.event_date_time_local <= :weekendDate) and (((acos(sin(((:latitude)*pi()/180)) * sin((b.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((b.latitude*pi()/180)) * cos((((:longitude)- b.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestWeekend(null));
			query.setDouble("latitude", latitud);
			query.setDouble("longitude", longitude);
			query.setParameter("pageType", pageType);
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
	public List<Usereventsuggestion> isEventSuggestionExists(int userId,
			int eventId) {

		logger.debug("UserId ==> "+userId +" eventId ==> "+eventId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usereventsuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("events.eventId", eventId));
		criteria.add(Restrictions.ne("suggestionType", "Direct Suggestion"));

		List<Usereventsuggestion> usereventsuggestions = criteria.list();
		return usereventsuggestions;

	}

	@SuppressWarnings("unchecked")
	public List<Userdealssuggestion> isDealSuggestionExists(Integer userId,
			Integer dealId) {
		logger.debug("UserId ==> "+userId +" dealId ==> "+dealId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Userdealssuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("deals.id", dealId));
		criteria.add(Restrictions.ne("suggestionType", "Direct Suggestion"));
		List<Userdealssuggestion> userdealsuggestions = criteria.list();
		return userdealsuggestions;
	}

	@Override
	public void registerUser(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);   
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dealsusage> getUserDealUsageByType(Integer userId,
			String groupType, double latitude, double longitude) {
		// TODO Auto-generated method stub
		/*Set<Dealsusage> dealUsage = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Dealsusage.class);
		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.createAlias("deals", "deals");
		criteria.createAlias("deals.business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		criteria.add(Restrictions.eq("businesstype.businessType", groupType));
		criteria.add(Restrictions.ge("deals.endAt", Utils.getCurrentDate()));
		List<Dealsusage> dealUsageList = criteria.list();	
		if(dealUsage != null)
		{
			dealUsage = new HashSet<Dealsusage>(dealUsageList);
		}
		return dealUsage ;*/
		List<Dealsusage> dealUsageList = null;
		//String hqlQuery = "select a from Events a where a.page.pageId = :pageId and a.event_date >= :currentDate";
		String hqlQuery = "select a from Dealsusage a, Deals b, Business c, Businesstype d where a.user.userId = :userId and d.businessType = :category and b.endAt >= :currentDate and a.deals.id = b.id and b.business.id = c.id and c.businesstype.businessTypeId = d.businessTypeId and (((acos(sin(((:latitude)*pi()/180)) * sin((c.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((c.lattitude*pi()/180)) * cos((((:longitude)- c.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("category", groupType);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			dealUsageList = (List<Dealsusage>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return dealUsageList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getUserDealByType(Integer userId,
			String groupType, double latitude, double longitude) {
		
		List<Deals> dealList = null;
		String hqlQuery = "select b from Dealsusage a, Deals b, Business c, Businesstype d where a.user.userId = :userId and d.businessType = :category and b.endAt >= :currentDate and a.deals.id = b.id and b.business.id = c.id and c.businesstype.businessTypeId = d.businessTypeId and (((acos(sin(((:latitude)*pi()/180)) * sin((c.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((c.lattitude*pi()/180)) * cos((((:longitude)- c.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("category", groupType);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			dealList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return dealList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User loginUser(UserModel user) {
		// TODO Auto-generated method stub
		String emailId  = user.getEmailId();
		String password = user.getPassword();
		logger.debug("user email :: --> "+emailId);
		logger.debug("user password :: --> "+password);
		List<User> users = null;
		String hqlQuery = "from User where emailId=:emailId and password=:password and isActive=:isActive ";
		//String hqlQuery = "from User a where a.emailId=:emailId and a.password=:password";
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("emailId", emailId.trim());
			query.setParameter("password", password.trim());
			query.setParameter("isActive",true);
			users = query.list();

		}catch(HibernateException e)
		{
			logger.error(e.toString());
		}
		logger.debug("User in login "+users);
		return users != null && !users.isEmpty() ? users.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getCommunityAllEvents(int pageId, double latitud, double longitude) {
		logger.debug("getUserCommunityEvents => ");
		List<Events> eventsList = null;
		//String hqlQuery = "select a from Events a where a.page.pageId = :pageId and a.event_date >= :currentDate";
		String hqlQuery = "select a from Events a where a.page.pageId = :pageId and (a.event_date_time_local >= :currentDate and a.event_date_time_local <= :weekendDate) and (((acos(sin(((:latitude)*pi()/180)) * sin((a.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((a.latitude*pi()/180)) * cos((((:longitude)- a.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50 ";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestWeekend(null));
			query.setDouble("latitude", latitud);
			query.setDouble("longitude", longitude);
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
	public Privacy getUserPrivacyType(String string) {

		List<Privacy> privacy = null;
		String hqlQuery = "from Privacy where privacyType =:string";
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("string", string);
			privacy = query.list();

		}catch(HibernateException e)
		{

		}
		return privacy != null && !privacy.isEmpty() ? privacy.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Profiletype getUserprofileType(String string) {

		List<Profiletype> privacy = null;
		String hqlQuery = "from Profiletype where userType =:string";
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("string", string);
			privacy = query.list();

		}catch(HibernateException e)
		{

		}
		return privacy != null && !privacy.isEmpty() ? privacy.get(0) : null;
	}

	@Override
	public void updateUser(User user) {
		try
		{
			logger.debug("updateUser:::: ");
			sessionFactory.getCurrentSession().update(user);
		}
		catch(HibernateException e)
		{
			logger.debug("ERROR:::: "+e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void confirmUser(String confirmationCode) {
		String hqlQuery = "update User set isActive=:isActive where confirmationId =:confirmationCode";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("isActive",true);
			query.setParameter("confirmationCode", confirmationCode);
			int result = query.executeUpdate();

		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pageuserlikes isPageExistInUserProfile(int userId,  int pageId) {
		List<Pageuserlikes> list = null;
		logger.debug("PAGE Id ::: "+pageId + " userId "+userId);
		String queryString = "from Pageuserlikes where user.userId =:userId and page.pageId =:pageId";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(queryString);
			query.setParameter("userId", userId);
			query.setParameter("pageId", pageId);
			list = query.list();

		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.debug(e.toString());
		}

		logger.debug("PageUser Like " + list);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public void saveUserCommunity(Pageuserlikes pageUserLike) {
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(pageUserLike);
		}
		catch(Exception e)
		{
			logger.error(e.toString());
		}
	}

	@Override
	public void updateUserCommunity(Pageuserlikes pageuserlikes) {
		sessionFactory.getCurrentSession().update(pageuserlikes);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Notificationpermission getDafaultNotification() {
		List<Notificationpermission> list = null;
		String queryString = "from Notificationpermission";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(queryString);
			list = query.list();

		}
		catch(HibernateException e)
		{
			logger.debug("In ERROR "+e.toString());
		}
		logger.debug("Notification DAO:::: "+ list);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public void deleteUserFavourity(int id, int userId) {
		String queryString = "delete from Pageuserlikes where page.pageId =:id and user.userId =:userId";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(queryString);
			query.setParameter("id", id);
			query.setParameter("userId", userId);
			query.executeUpdate();

		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Usernondealsuggestion> getUserNonDealsSuggestionByUserIdForCommunity(
			int userId, String category) {
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usernondealsuggestion.class,"usernondealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("usernondealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));

		List<Usernondealsuggestion> userNonDealSuggestions = criteria.list();*/

		List<Usernondealsuggestion> userNonDealSuggestions = null;
		//String hqlQuery = "select a from Usernondealsuggestion a, Pageuserlikes b, Page c, User d where d.userId = :userId and a.user.userId = d.userId and c.business.id = a.business.id and b.page.pageId = c.pageId and b.user.userId = d.userId";
		//String hqlQuery = "select a from Usernondealsuggestion a, Pageuserlikes b, Page c, User d, Pagetype e where d.userId = :userId and e.pageType = :category and a.user.userId = d.userId and c.business.id = a.business.id and b.page.pageId = c.pageId and b.user.userId = d.userId and c.pagetype.pageTypeId = e.pageTypeId";
		String hqlQuery = "select a from Usernondealsuggestion a, Business b, Businesstype c, User d where d.userId = :userId and c.businessType = :category and a.user.userId = d.userId and (a.suggestionType like '%user%') and b.id = a.business.id and b.businesstype.businessTypeId = c.businessTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("category", category);
			userNonDealSuggestions = (List<Usernondealsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userNonDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForFriends(int userId, int contactId, String category) {
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usereventsuggestion.class,"usereventsuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("usereventsuggestion.user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));


		List<Usereventsuggestion> usereventsuggestions = criteria.list();*/

		List<Usereventsuggestion> usereventsuggestions = null;
		//String hqlQuery = "select a from Usereventsuggestion a, Eventuserlikes b, User d where d.userId = :userId and a.events.channel like '%"+ category+ "%' and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and b.event.eventId = a.events.eventId and b.user.userId = :contactId and a.events.event_date >= :currentDate";
		String hqlQuery = "select a from Usereventsuggestion a,  Events c, User d where d.userId = :userId and c.channel like '%"+ category+ "%' and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and c.eventId = a.events.eventId and c.event_date_time_local >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			//query.setParameter("contactId", contactId);
			//query.setParameter("category", category);
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForFriends(int userId, int contactId, String category) {
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Userdealssuggestion.class,"userdealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("userdealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));

		List<Userdealssuggestion> userDealSuggestions = criteria.list();*/
		List<Userdealssuggestion> userDealSuggestions = null;
		//String hqlQuery = "select count(*) from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userid and a.userid = d.userid and b.deals.id = a.deals.id and b.user.userId = d.userId and a.deals.endAt > :currentDate";
		//String hqlQuery = "select a from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userId and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and b.deals.id = a.deals.id and b.user.userId = :contactId";
		//String hqlQuery = "select a from Userdealssuggestion a, Dealsusage b, User d, Business c, Businesstype e, Deals f where d.userId = :userId and e.businessType = :category and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and b.deals.id = a.deals.id and b.user.userId = :contactId and f.id = a.deals.id and c.id = f.business.id and c.businesstype.businessTypeId = e.businessTypeId";
		String hqlQuery = "select a from Userdealssuggestion a,User d, Business c, Businesstype e, Deals f where d.userId = :userId and e.businessType = :category and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and f.id = a.deals.id and f.endAt >= :currentDate and c.id = f.business.id and c.businesstype.businessTypeId = e.businessTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			//query.setParameter("contactId", contactId);
			query.setParameter("category", category);
			query.setParameter("currentDate", Utils.getCurrentDate());
			userDealSuggestions = (List<Userdealssuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForJeeyoh(
			int userId, String category) {
		List<Userdealssuggestion> userDealSuggestions = null;
		//String hqlQuery = "select count(*) from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userid and a.userid = d.userid and b.deals.id = a.deals.id and b.user.userId = d.userId and a.deals.endAt > :currentDate";
		//String hqlQuery = "select a from Userdealssuggestion a, Dealsusage b, Deals c, User d where d.userId = :userId and a.user.userId = d.userId and c.id = a.deals.id and b.deals.id = a.deals.id and b.user.userId = d.userId";
		//String hqlQuery = "select a from Userdealssuggestion a, Dealsusage b, Deals c, User d, Business e, Businesstype f where d.userId = :userId  and f.businessType = :category and a.user.userId = d.userId and c.id = a.deals.id and b.deals.id = a.deals.id and b.user.userId = d.userId and e.id = c.business.id and e.businesstype.businessTypeId = f.businessTypeId";
		String hqlQuery = "select a from Userdealssuggestion a, Deals c, User d, Business e, Businesstype f where d.userId = :userId and f.businessType = :category and a.user.userId = d.userId and (a.suggestionType like '%user%') and c.id = a.deals.id and c.endAt >= :currentDate and e.id = c.business.id and e.businesstype.businessTypeId = f.businessTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("category", category);
			query.setParameter("currentDate", Utils.getCurrentDate());
			userDealSuggestions = (List<Userdealssuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForJeeyoh(
			int userId, String category) {


		List<Usereventsuggestion> usereventsuggestions = null;
		//String hqlQuery = "select a from Usereventsuggestion a, Eventuserlikes b, User d where d.userId = :userId and a.events.channel like '%"+category+"%' and a.user.userId = d.userId and b.event.eventId = a.events.eventId and b.user.userId = d.userId and a.events.event_date >= :currentDate";
		String hqlQuery = "select a from Usereventsuggestion a, User d, Events c where d.userId = :userId and c.channel like '%"+category+"%' and a.user.userId = d.userId and (a.suggestionType like '%user%') and c.eventId = a.events.eventId and c.event_date_time_local >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			//query.setParameter("category", category);
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions;
	}

	@Override
	public List<Usernondealsuggestion> getUserNonDealsSuggestionByUserIdForJeeyoh(
			int userId, String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForCommunity(
			int userId, String category) {
		List<Userdealssuggestion> userDealSuggestions = null;
		//String hqlQuery = "select count(*) from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userid and a.userid = d.userid and b.deals.id = a.deals.id and b.user.userId = d.userId and a.deals.endAt > :currentDate";
		String hqlQuery = "select a from Userdealssuggestion a, Page b, Pageuserlikes c, User d, Deals e where d.userId = :userId and a.user.userId = d.userId  and a.suggestionType like '%Community%' and e.id = a.deals.id and e.endAt >= :currentDate and e.business.id = b.business.id and c.page.pageId = b.pageId and c.user.userId = d.userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			userDealSuggestions = (List<Userdealssuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForCommunity(
			int userId, String category) {
		List<Usereventsuggestion> usereventsuggestions = null;
		String hqlQuery = "select a from Usereventsuggestion a, Page b, Pageuserlikes c, User d, Events e where d.userId = :userId and e.channel like '%"+category+"%' and a.user.userId = d.userId and e.eventId = a.events.eventId and e.page.id = b.pageId and c.page.id = b.pageId and c.user.userId = d.userId and a.events.event_date_time_local > :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			//query.setParameter("category", category);
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usernondealsuggestion> getUserNonDealsSuggestionByUserIdForFriends(
			int userId, int contactId, String category) {
		logger.debug("getUserNonDealsSuggestionByUserIdForFriends"+userId +" : "+contactId);
		List<Usernondealsuggestion> userNonDealSuggestions = null;
		//String hqlQuery = "select a from Usernondealsuggestion a, Pageuserlikes b, Page c, User d where d.userId = :userId and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and c.business.id = a.business.id and b.page.pageId = c.pageId and b.user.userId = :contactId";
		//String hqlQuery ="select a from Usernondealsuggestion a, Pageuserlikes b, Page c, User d, Pagetype e where d.userId = :userId and e.pageType = :category and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and c.business.id = a.business.id and b.page.pageId = c.pageId and b.user.userId = :contactId and c.pagetype.pageTypeId = e.pageTypeId";
		String hqlQuery = "select a from Usernondealsuggestion a, Business b, User d, Businesstype e where d.userId = :userId and e.businessType = :category and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and b.id = a.business.id and b.businesstype.businessTypeId = e.businessTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			//query.setParameter("contactId", contactId);
			query.setParameter("category", category);
			userNonDealSuggestions = (List<Usernondealsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userNonDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> userDealSuggestionCount(String dealIdsStr, int limit) {
		List<Object[]> rows = null;

		//String hqlQuery = "select count(*) as likes ,a.deals.id from Dealsusage a where (a.isLike is true or a.isFavorite is true)  and ("+ dealIdsStr +") group by a.deals.id order by likes desc";
		//String hqlQuery = "select count(a.deals.id) as likes ,b.id from Deals as b left join b.dealsusages a where ("+ dealIdsStr +") group by a.deals.id order by likes desc";
		String hqlQuery = "select count(a.deals.id) as likes ,b.id from Deals as b left join b.dealsusages a where b.id in ("+ dealIdsStr +") group by b.id order by likes desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			//query.setInteger("limit", limit);
			rows = (List<Object[]>) query.list();
			logger.debug("Query: "+hqlQuery);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ERROR::  "+e.getLocalizedMessage());
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> userEventSuggestionCount(String eventIdsStr, int limit) {
		List<Object[]> rows = null;

		//String hqlQuery = "select count(*) as likes ,a.event.eventId from Eventuserlikes a where (a.isLike is true or a.isFavorite is true or a.isVisited is true)  and a.event.eventId in("+ eventIdsStr +") group by a.event.eventId order by likes desc";
		//String hqlQuery = "select count(a.event.eventId) as likes ,b.eventId from Events b left Join Eventuserlikes a where (" + eventIdsStr + ") group by a.event.eventId order by likes desc";
		String hqlQuery = "select count(a.event.eventId) as likes ,b.eventId from Events b left Join b.eventuserlikes a where b.eventId in (" + eventIdsStr + ") group by b.eventId order by likes desc";

		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			//query.setInteger("limit", limit);
			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ERROR::  "+e.getLocalizedMessage());
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> userNonDealSuggestionCount(String pageIdsStr, int limit) {
		logger.debug("userNonDealSuggestionCount::::  "+pageIdsStr);
		List<Object[]> rows = null;
		String hqlQuery = "select count(*) as likes ,a.page.pageId from Pageuserlikes a where (a.isLike is true or a.isFavorite is true or a.isVisited is true)  and a.page.pageId in("+ pageIdsStr +") group by a.page.pageId order by likes desc limit 10";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			//query.setInteger("limit", limit);
			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public void saveTopDealSuggestions(Topdealssuggestion topdealssuggestion) {
		logger.debug("saveTopDealSuggestions => ");
		/* Session session  = sessionFactory.openSession();
		  Transaction tx = session.beginTransaction()*/;
		  Session session =  sessionFactory.getCurrentSession();
		  try
		  {
			  session.save(topdealssuggestion);
			  /*session.save(suggestion);    
		   tx.commit();
		   session.close();*/
		  }
		  catch(HibernateException e)
		  {
			  e.printStackTrace();
			  logger.error("ERROR IN DAO :: = > "+e);
		  }

	}

	@Override
	public void saveTopEventSuggestions(Topeventsuggestion topeventsuggestion) {
		logger.debug("saveTopEventSuggestions => ");
		/* Session session  = sessionFactory.openSession();
		  Transaction tx = session.beginTransaction()*/;
		  Session session =  sessionFactory.getCurrentSession();
		  try
		  {
			  session.save(topeventsuggestion);

			  /*session.save(suggestion);    
		   tx.commit();
		   session.close();*/
		  }
		  catch(HibernateException e)
		  {
			  e.printStackTrace();
			  logger.error("ERROR IN DAO :: = > "+e);
		  }
	}

	@Override
	public void saveTopNonDealSuggestions(Topnondealsuggestion topnondealsuggestion) {
		logger.debug("saveTopNonDealSuggestions => ");
		/* Session session  = sessionFactory.openSession();
		  Transaction tx = session.beginTransaction()*/;
		  Session session =  sessionFactory.getCurrentSession();
		  try
		  {
			  session.save(topnondealsuggestion);

			  /*session.save(suggestion);    
		   tx.commit();
		   session.close();*/
		  }
		  catch(HibernateException e)
		  {
			  e.printStackTrace();
			  logger.error("ERROR IN DAO :: = > "+e);
		  }
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean isUserActive(String emailId) {
		// TODO Auto-generated method stub
		List<Boolean> user = null;
		String queryString = "select isActive from User where emailId = :emailId";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(queryString);
			query.setParameter("emailId", emailId);
			user = query.list();

		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		logger.debug("match boolean here :: "+user.get(0));
		boolean  isActive = user.get(0);
		return isActive;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topdealssuggestion> isTopDealSuggestionExists(int userId,
			int dealId,String suggestionType) {
		logger.debug("UserId ==> "+userId +" businessid ==> "+dealId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topdealssuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("deals.id", dealId));
		if(suggestionType != null)
			criteria.add(Restrictions.eq("suggestionType", suggestionType));
		List<Topdealssuggestion> topdealsuggestions = criteria.list();
		return topdealsuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topeventsuggestion> isTopEventSuggestionExists(int userId,
			int eventId,String suggestionType) {
		logger.debug("UserId ==> "+userId +" businessid ==> "+eventId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topeventsuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("events.eventId", eventId));
		if(suggestionType != null)
			criteria.add(Restrictions.eq("suggestionType", suggestionType));
		List<Topeventsuggestion> topeventsuggestions = criteria.list();
		return topeventsuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topnondealsuggestion> isTopNonDealSuggestionExists(int userId,
			int businessId,String suggestionType) {
		logger.debug("UserId ==> "+userId +" businessid ==> "+businessId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topnondealsuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("business.id", businessId));
		if(suggestionType != null)
			criteria.add(Restrictions.eq("suggestionType", suggestionType));
		List<Topnondealsuggestion> topnondealsuggestions = criteria.list();
		return topnondealsuggestions;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Topcommunitysuggestion> isTopCommunitySuggestionExists(int userId, int pageId) {
		logger.debug("UserId ==> "+userId +" pageId ==> "+pageId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topcommunitysuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("page.pageId", pageId));

		List<Topcommunitysuggestion> topcommunitysuggestions = criteria.list();
		return topcommunitysuggestions;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getuserCommunitySuggestionsByLikesCount(int userId, String category, double latitude, double longitude) {
		List<Object[]> rows = null;
		//String hqlQuery = "select count(*) as likes ,a from Pageuserlikes a where (a.isLike is true or a.isFavorite is true or a.isVisited is true or isFollowing is true) and a.page.pageId in (select b.page.pageId from Pageuserlikes b where b.user.userId = :userId and b.page.business.id is null and b.page.pagetype.pageType = :category) group by a.page.pageId order by likes desc";
		//String hqlQuery = "select count(*) as likes ,a from Pageuserlikes a where (a.isLike is true or a.isFavorite is true or a.isVisited is true or isFollowing is true) and a.page.pageId in (select b.page.pageId from Pageuserlikes b where b.user.userId = :userId and b.page.pagetype.pageType = :category) group by a.page.pageId order by likes desc";
		//String  hqlQuery="select count(*) as likes ,a from Pageuserlikes a where (a.isLike is true or a.isFavorite is true or a.isVisited is true or isFollowing is true) and a.page.pageId in (select b.page.pageId from Pageuserlikes b , Events c where b.user.userId = :userId and b.page.pagetype.pageType = :category and b.page.pageId = c.page.pageId and c.event_date >= :currentDate) group by a.page.pageId order by likes desc";
		//String hqlQuery = "select count(*) as likes, (((acos(sin(((:latitude)*pi()/180)) * sin((c.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((c.latitude*pi()/180)) * cos((((:longitude)- c.longitude)*pi()/180))))*180/pi())*60*1.1515) as distance ,a from Pageuserlikes a, Events c where a.user.userId = :userId and a.page.pageId not in (select b.page.pageId from Topcommunitysuggestion b where b.user.userId = :userId) and (a.isLike is true or a.isFavorite is true or a.isVisited is true or isFollowing is true) and a.page.pagetype.pageType = :category and a.page.pageId = c.page.pageId and c.event_date_time_local >= :currentDate group by a.page.pageId order by distance asc,likes desc";
		//String hqlQuery = "select count(*) as likes, (((acos(sin(((:latitude)*pi()/180)) * sin((c.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((c.latitude*pi()/180)) * cos((((:longitude)- c.longitude)*pi()/180))))*180/pi())*60*1.1515) as distance ,a from Pageuserlikes a left join a.page.events c where a.user.userId = :userId and a.page.pageId not in (select b.page.pageId from Topcommunitysuggestion b where b.user.userId = :userId) and (a.isLike is true or a.isFavorite is true or a.isVisited is true or isFollowing is true) and a.page.pagetype.pageType = :category and c.eventId = (select e.eventId from Events e where e.page.pageId = a.page.pageId and e.event_date_time_local >= :currentDate order by e.event_date_time_local asc limit 1) group by a.page.pageId order by distance asc,likes desc";
		String hqlQuery = "select count(*) as likes, (((acos(sin(((:latitude)*pi()/180)) * sin((c.latitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((c.latitude*pi()/180)) * cos((((:longitude)- c.longitude)*pi()/180))))*180/pi())*60*1.1515) as distance ,a.pageId from pageuserlikes a left join page b on a.pageId = b.pageId left join pagetype d on b.pageTypeId = d.pageTypeId left join events c on a.pageId = c.pageId where a.userId = :userId and d.pagetype = :category and a.pageId not in (select pageId from topcommunitysuggestion where userId = :userId) and (a.isLike is true or a.isFavorite is true or a.isVisited is true or isFollowing is true) and c.eventId = (select e.eventId from events e where e.pageId = a.pageId and e.event_date_time_local >= :currentDate order by e.event_date_time_local asc limit 1) group by a.pageId order by distance asc,likes desc";
		try {
			Query query = sessionFactory.getCurrentSession().createSQLQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("category", category);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			query.setMaxResults(10);
			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			logger.debug("Error in community::  "+ e.getMessage());
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public void saveTopCommunitySuggestions(
			Topcommunitysuggestion topcommunitysuggestion) {
		logger.debug("saveTopDealSuggestions => ");
		/* Session session  = sessionFactory.openSession();
		  Transaction tx = session.beginTransaction()*/;
		  Session session =  sessionFactory.getCurrentSession();
		  try
		  {
			  session.save(topcommunitysuggestion);
			  /*session.save(suggestion);    
		   tx.commit();
		   session.close();*/
		  }
		  catch(HibernateException e)
		  {
			  e.printStackTrace();
			  logger.error("ERROR IN DAO :: = > "+e);
		  }
	}
	
	@Override
	public void saveTopCommunitySuggestions(
			List<Topcommunitysuggestion> topcommunitysuggestion) {
		logger.debug("saveTopDealSuggestions => ");
		/* Session session  = sessionFactory.openSession();
		  Transaction tx = session.beginTransaction()*/;
		  Session session =  sessionFactory.getCurrentSession();
		  try
		  {
			  for(Topcommunitysuggestion topcommunitysuggestion2 : topcommunitysuggestion)
			  {
				  session.save(topcommunitysuggestion2);
			  }
			  
			  /*session.save(suggestion);    
		   tx.commit();
		   session.close();*/
		  }
		  catch(HibernateException e)
		  {
			  e.printStackTrace();
			  logger.error("ERROR IN DAO :: = > "+e);
		  }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForFriendsCommunity(
			int userId, int contactId, String category) {
		List<Userdealssuggestion> userDealSuggestions = null;
		//String hqlQuery = "select count(*) from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userid and a.userid = d.userid and b.deals.id = a.deals.id and b.user.userId = d.userId and a.deals.endAt > :currentDate";
		//String hqlQuery = "select a from Userdealssuggestion a, Page b, Pageuserlikes c, User d, Deals e where d.userId = :userId and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%')  and a.suggestionType like '%Community%' and e.id = a.deals.id and e.business.id = b.business.id and c.page.pageId = b.pageId and c.user.userId = :contactId";
		String hqlQuery = "select a from Userdealssuggestion a, Page b, Pageuserlikes c, User d, Deals e, Business f, Businesstype g where d.userId = :userId and g.businessType = :category and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and a.suggestionType like '%Community%' and e.id = a.deals.id and e.endAt >= :currentDate and e.business.id = b.business.id and c.page.pageId = b.pageId and c.user.userId = :contactId and f.id = e.business.id and f.businesstype.businessTypeId = g.businessTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("contactId", contactId);
			query.setParameter("category", category);
			query.setParameter("currentDate", Utils.getCurrentDate());
			userDealSuggestions = (List<Userdealssuggestion>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForFriendsCommunity(
			int userId, int contactId, String category) {
		List<Usereventsuggestion> usereventsuggestions = null;
		String hqlQuery = "select a from Usereventsuggestion a, Page b, Pageuserlikes c, User d, Events e where d.userId = :userId  and e.channel like '%"+category+"%' and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and e.eventId = a.events.eventId and e.page.id = b.pageId and c.page.id = b.pageId and c.user.userId = :contactId and a.events.event_date_time_local > :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("contactId", contactId);
			//query.setParameter("category", category);
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<User> getStarFriends(int userId, Double lattitude,Double longitude) {
		logger.debug("getStarFriends =>");
		List<User> starFriendList = new ArrayList<User>();
		//List<User> starFriendWithinFiftyMilesList = new ArrayList<User>();
		//List<User> userList = new ArrayList<User>();
		//String hqlQuery = "from User a where a.userId in (select b.userByContactId.userId from Usercontacts b where b.userByUserId.userId=:userId and b.isStar=true)";
		String hqlQuery = "select a from User a, Usercontacts b where ((b.userByUserId.userId = :userId and a.userId = b.userByContactId.userId) or (b.userByContactId.userId = :userId and a.userId = b.userByUserId.userId)) and b.isApproved is true and b.isStar=true and (((acos(sin(((:lat)*pi()/180)) * sin((a.lattitude*pi()/180))+cos(((:lat)*pi()/180)) * cos((a.lattitude*pi()/180)) * cos((((:long)- a.longitude)*pi()/180))))*180/pi())*60*1.1515)<=50";
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("userId", userId);
			query.setDouble("lat", lattitude);
			query.setDouble("long", longitude);
			starFriendList = (List<User>) query.list();
			logger.debug("starFriendList =>"+starFriendList);
			/*for(User user:starFriendList)
			{
				String hqlQuery1 = "select a from User a WHERE userId=:userId group by a.emailId HAVING (((acos(sin(((:lat)*pi()/180)) * sin((a.lattitude*pi()/180))+cos(((:lat)*pi()/180)) * cos((a.lattitude*pi()/180)) * cos((((:long)- a.longitude)*pi()/180))))*180/pi())*60*1.1515)<=50";
				try {
					Query query1 = sessionFactory.getCurrentSession().createQuery(hqlQuery1); 
					query1.setParameter("userId", user.getUserId());
					query1.setDouble("lat", lattitude);
					query1.setDouble("long", longitude);
					userList = (List<User>) query1.list();
					starFriendWithinFiftyMilesList.add(userList.get(0));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//logger.debug("userList =>"+starFriendWithinFiftyMilesList);
		return starFriendList;
	}

	@Override
	public long getUserPageFavouriteCount(String pageType, int userId) {
		// TODO Auto-generated method stub
		/*int rowCount = 0;
		String hqlquery = "select count(*) from Pageuserlikes a, Page b where  a.page.pageId = b.pageId and b.pagetype.pageType =:pageType and a.user.userId =:userId and a.isFavorite:isFavorite";
		try{
			Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);

			query.setParameter("pageType", pageType);
			query.setParameter("userId",userId);
			query.setParameter("isFavorite",true);
			rowCount = ((Number)query.uniqueResult()).intValue();

		}
		catch(Exception e)
		{

			logger.error(e.toString());
		}
		 */
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Pageuserlikes.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("page", "page");
		criteria.createAlias("page.pagetype", "pagetype");
		criteria.add(Restrictions.eq("pagetype.pageType", pageType));
		criteria.createAlias("user", "user");        
		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("isFavorite", true));
		criteria.setProjection(Projections.rowCount());
		Long resultCount = (Long)criteria.uniqueResult();
		logger.debug("count :: ==> "+resultCount);
		return resultCount;
	}

	/*@Override
	public List<UserCategory> getUserNonLikeCategories(int userId) {
		// TODO Auto-generated method stub

		    List<UserCategory>  userCategories = null;
		    String hqlQuery = "from UserCategory  a where a.userCategoryId not in (select b.userCategory.userCategoryId from UserCategoryLikes b where b.user.userId =:userId)";

		    try{
		    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		    	query.setParameter("userId",userId);
		    	userCategories = query.list();
		    }
		    catch(HibernateException e)
		    {
		    	logger.error(e.toString());
		    }
		return userCategories;
	}*/
	// if we need to capture based on category
	@SuppressWarnings("unchecked")
	public List<UserCategory> getUserNonLikeCategories(int userId, String categoryType) {
		// TODO Auto-generated method stub

		List<UserCategory>  userCategories = null;
		//String hqlQuery = "from UserCategory  a where a.itemCategory =:category and a.userCategoryId not in (select b.userCategory.userCategoryId from UserCategoryLikes b where b.user.userId =:userId)";
		String hqlQuery = "from UserCategory  a where";
		if(categoryType != null && !categoryType.trim().equals(""))
		{
			if(categoryType.equalsIgnoreCase("NIGHTLIFE"))
				hqlQuery = hqlQuery + " (a.itemCategory = :category or a.itemCategory = 'CONCERT' or a.itemCategory = 'THEATER') and";
			else
				hqlQuery = hqlQuery + " a.itemCategory =:category and";	
		}
			
		hqlQuery = hqlQuery + " a.userCategoryId not in (select b.userCategory.userCategoryId from UserCategoryLikes b where b.user.userId =:userId)";
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("userId",userId);
			if(categoryType != null && !categoryType.trim().equals(""))
				query.setParameter("category",categoryType);
			userCategories = query.list();
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
		return userCategories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserCategory getCategory(Integer userCategoryId) {
		// TODO Auto-gene 
		List<UserCategory>  userCategories = null;
		String hqlQuery = "from UserCategory  a where a.userCategoryId =:userCategoryId";

		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("userCategoryId",userCategoryId);
			userCategories = query.list();
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
		return userCategories != null && !userCategories.isEmpty() ? userCategories.get(0) : null;
	}

	@Override
	public void saveUserCategoryLike(UserCategoryLikes categoryLikes) {
		// TODO Auto-generated method stub
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(categoryLikes);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTopCommunitySuggestions(int userId) {
		logger.debug("getTopCommunitySuggestions ==> "+userId);
		List<Object[]> rows = null;

		String hqlQuery = "select avg(d.rating) as rating, c.categoryType,b.pageId,b.about,b.pageUrl,b.profilePicture,b.source,e.event_date_time_local,e.event_time_local from topcommunitysuggestion c left join page b on c.pageId = b.pageId left Join communityReviewMap a on  b.pageid = a.pageId left join communityReview d on a.reviewId = d.reviewId left join events e on b.pageId = e.pageId where c.userId = :userId and e.eventId = (select e.eventId from events e where e.pageId = b.pageId and e.event_date_time_local >= :currentDate order by e.event_date_time_local asc limit 1) group by b.pageId order by c.createdTime desc,c.rank asc";
		//String hqlQuery = "select avg(d.rating) as rating, c.categoryType,b.pageId,b.about,b.pageUrl,b.profilePicture,b.source,e.event_date_local,e.event_time_local from Topcommunitysuggestion c left join c.page b left Join b.communityReviewMap a left join a.communityReview d left join b.events e where c.user.userId = :userId and e.eventId = (select e.eventId from Events e where e.page.pageId = b.pageId and e.event_date_time_local >= :currentDate order by e.event_date_time_local asc limit 1) group by b.pageId order by c.createdTime desc,c.rank asc";
		//String hqlQuery = "select avg(d.rating) as rating, c from Topcommunitysuggestion c left join c.page b left Join b.communityReviewMap a left join a.communityReview d left join b.events e where c.user.userId = :userId and e.event_date_time_local >= :currentDate group by b.pageId order by c.createdTime desc,c.rank asc";
		//String hqlQuery = "select avg(d.rating) as rating, c.* from topcommunitysuggestion c left join page b on c.pageId = b.pageId left Join communityReviewMap a on b.pageId = a.pageId left join communityReview d on d.reviewId=a.reviewId where c.userId = :userId group by b.pageId order by suggestionId asc";
		//String hqlQuery = "select avg(d.rating) as rating, b.pageId from Page b left Join b.communityReviewMap a left join a.communityReview d group by b.pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlQuery);
			//query.addEntity(Topcommunitysuggestion.class);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			rows = (List<Object[]>) query.list();
			logger.debug("rows:  "+rows.size());
		} catch (Exception e) {
			logger.debug("Error: "+e.getMessage());
			e.printStackTrace();
		}
		/*logger.debug("getTopCommunitySuggestions ==> "+userEmail);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topcommunitysuggestion.class,"topcommunitysuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("topcommunitysuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", userEmail));

		List<Topcommunitysuggestion> pageList = criteria.list();*/
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topcommunitysuggestion> getTopCommunitySuggestions(String userEmail) {

		logger.debug("getTopCommunitySuggestions ==> "+userEmail);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topcommunitysuggestion.class,"topcommunitysuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("topcommunitysuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", userEmail));

		List<Topcommunitysuggestion> pageList = criteria.list();
		return pageList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topdealssuggestion> getTopDealSuggestions(String userEmail, String suggestionType, String category) {
		logger.error("getUserDealSuggestions ==== > "+userEmail);

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topdealssuggestion.class,"topdealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("topdealssuggestion.suggestionType", "%" + suggestionType + "%"));
		criteria.createAlias("topdealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", userEmail));
		criteria.createAlias("topdealssuggestion.deals", "deals");
		criteria.add(Restrictions.conjunction().add(Restrictions.ge("deals.endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("deals.endAt", Utils.getNearestThursday())));
		criteria.add(Restrictions.disjunction().add(Restrictions.isNull("topdealssuggestion.suggestedTime"))
				.add(Restrictions.ge("topdealssuggestion.suggestedTime", Utils.getCurrentDate())));
		criteria.addOrder(Order.desc("topdealssuggestion.createdTime"));
		criteria.addOrder(Order.asc("topdealssuggestion.rank"));
		List<Topdealssuggestion> dealsList = criteria.list();
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topeventsuggestion> getTopEventSuggestions(String userEmail, String suggestionType, String category) {
		logger.debug("getTopEventSuggestions ==> "+userEmail);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topeventsuggestion.class,"topeventsuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("topeventsuggestion.suggestionType", "%" + suggestionType + "%"));
		criteria.createAlias("topeventsuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", userEmail));
		criteria.createAlias("topeventsuggestion.events", "events");
		criteria.add(Restrictions.ge("events.event_date_time_local", Utils.getCurrentDate()));
		criteria.add(Restrictions.disjunction().add(Restrictions.isNull("topeventsuggestion.suggestedTime"))
				.add(Restrictions.ge("topeventsuggestion.suggestedTime", Utils.getCurrentDate())));
		criteria.addOrder(Order.desc("topeventsuggestion.createdTime"));
		criteria.addOrder(Order.asc("topeventsuggestion.rank"));
		List<Topeventsuggestion> eventsList = criteria.list();
		return eventsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Topnondealsuggestion> getTopNonDealSuggestions(String userEmail, String suggestionType, String category) {
		logger.debug("getTopNonDealSuggestions ==> "+userEmail);

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Topnondealsuggestion.class,"topnondealsuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.like("topnondealsuggestion.suggestionType", "%" + suggestionType + "%"));
//		criteria.add(Restrictions.eq("topnondealsuggestion.categoryType", "%" + suggestionType + "%"));
		criteria.createAlias("topnondealsuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", userEmail));
		criteria.add(Restrictions.disjunction().add(Restrictions.isNull("topnondealsuggestion.suggestedTime"))
				.add(Restrictions.ge("topnondealsuggestion.suggestedTime", Utils.getCurrentDate())));
		criteria.addOrder(Order.desc("topnondealsuggestion.createdTime"));
		criteria.addOrder(Order.asc("topnondealsuggestion.rank"));
		List<Topnondealsuggestion> businessList = criteria.list();

		return businessList;
	}

	@Override
	public int getBusinessWightCount(int userId, int itemId) {
		// TODO Auto-generated method stub
		int rowCount = 0;
		String hqlquery = "select count(*) from Pageuserlikes where page.pageId =(select pageId from Page where business.id =:itemId) and userId in (select userByContactId.userId from Usercontacts where userByUserId.userId =:userId)";
		try{
			//Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("itemId", itemId);
			query.setParameter("userId",userId);

			rowCount = ((Number)query.uniqueResult()).intValue();

		}
		catch(Exception e)
		{

			logger.error(e.toString());
		}
		logger.debug("getBusinessWightCount Count"+rowCount);
		return rowCount;

	}

	@Override
	public int getDealWightCount(int userId, int itemId) {

		int rowCount = 0;
		String hqlquery = "select count(*) from Dealsusage where deals.id =:itemId and user.userId in (select userByContactId.userId from Usercontacts where userByUserId.userId =:userId)";
		try{
			//Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("itemId", itemId);
			query.setParameter("userId",userId);

			rowCount = ((Number)query.uniqueResult()).intValue();

		}
		catch(Exception e)
		{

			logger.error(e.toString());
		}
		logger.debug("Total Count"+rowCount);
		return rowCount;

	}

	@Override
	public int getEventWightCount(int userId, int itemId) {
		int rowCount = 0;
		String hqlquery = "select count(*) from Eventuserlikes where event.eventId =:itemId and user.userId in (select userByContactId.userId from Usercontacts where userByUserId.userId =:userId)";
		try{
			//Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("itemId", itemId);
			query.setParameter("userId",userId);

			rowCount = ((Number)query.uniqueResult()).intValue();

		}
		catch(Exception e)
		{

			logger.error(e.toString());
		}
		logger.debug("Total Count"+rowCount);
		return rowCount;
	}

	@Override
	public int getPageWightCount(int userId, int itemId) {
		int rowCount = 0;
		String hqlquery = "select count(*) from Pageuserlikes where page.pageId =:itemId and userId in (select userByContactId.userId from Usercontacts where userByUserId.userId =:userId)";
		try{
			//Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("itemId", itemId);
			query.setParameter("userId",userId);

			rowCount = ((Number)query.uniqueResult()).intValue();

		}
		catch(Exception e)
		{

			logger.error(e.toString());
		}
		logger.debug("getPageWightCount Count"+rowCount);
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAttendingUsersForBusiness(int id, int userId) {
		List<User> userList = null;
		String hqlQuery = "select a from User a, Pageuserlikes b, Page c where c.business.id = :businessId and b.page.pageId = c.pageId and b.user.userId = a.userId and a.userId != :userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("businessId", id);
			query.setParameter("userId", userId);
			userList = (List<User>) query.list();
			logger.debug("userList => " + userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAttendingUsersForDeals(int id, int userId) {
		List<User> userList = null;
		String hqlQuery = "select a from User a, Dealsusage b where b.deals.id = :dealId and b.user.userId = a.userId and a.userId != :userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("dealId", id);
			query.setParameter("userId", userId);
			userList = (List<User>) query.list();
			logger.debug("userList => " + userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAttendingUsersForEvents(int id, int userId) {
		List<User> userList = null;
		String hqlQuery = "select a from User a, Eventuserlikes b where b.event.eventId = :eventId and b.user.userId = a.userId and a.userId != :userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("eventId", id);
			query.setParameter("userId", userId);
			userList = (List<User>) query.list();
			logger.debug("userList => " + userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAttendingUsersForpage(int id, int userId) {
		List<User> userList = null;
		String hqlQuery = "select a from User a, Pageuserlikes b where b.page.pageId = :pageId and b.user.userId = a.userId and a.userId != :userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("pageId", id);
			query.setParameter("userId", userId);
			userList = (List<User>) query.list();
			logger.debug("userList => " + userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public int getTotalUserNonDealSuggestions(Integer userId, String category, String suggestionType, double lat, double lon, int distance, double rating) {

		logger.debug("userNonDealSuggestionCount => ");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Business.class, "business").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.count("business.id"));

		criteria.createAlias("business.usernondealsuggestions", "usernondealsuggestion");
		criteria.createAlias("usernondealsuggestion.user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));


		if(category != null && !category.trim().equals(""))
		{
			criteria.createAlias("businesstype", "businessTypes");
			criteria.add(Restrictions.eq("businessTypes.businessType", category));
		}

		criteria.add(Restrictions.disjunction().add(Restrictions.isNull("usernondealsuggestion.suggestedTime"))
				.add(Restrictions.ge("usernondealsuggestion.suggestedTime", Utils.getCurrentDateForEvent())));
		//criteria.add(Restrictions.eq("usernondealsuggestion.suggestedTime", Utils.getNearestWeekend(null)));

		if(suggestionType != null)
		{
			if(suggestionType.equalsIgnoreCase("Friend Suggestion"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.like("usernondealsuggestion.suggestionType", "%Friend%"))
						.add(Restrictions.like("usernondealsuggestion.suggestionType", "%Group%"))
						.add(Restrictions.eq("usernondealsuggestion.suggestionType", "Wall Feed Suggestion"))
						.add(Restrictions.eq("usernondealsuggestion.suggestionType", "Direct Suggestion")));
			}
			else if(suggestionType.equalsIgnoreCase("Community Suggestion"))
			{
				criteria.add(Restrictions.like("usernondealsuggestion.suggestionType", "%Community%"));
			}
			else if(suggestionType.equalsIgnoreCase("Jeeyoh Suggestion"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.like("usernondealsuggestion.suggestionType", "%User%"))
						.add(Restrictions.like("usernondealsuggestion.suggestionType", "%Friend%"))
						.add(Restrictions.like("usernondealsuggestion.suggestionType", "%Group%")));
			}
		}

		if(rating != 0)
			criteria.add(Restrictions.ge("rating", rating));

		if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin((this_.lattitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos((this_.lattitude*pi()/180)) * cos((((" + lon + ")- this_.longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			criteria.add(Restrictions.sqlRestriction(sql)); 
		}

		int rowCount = Integer.parseInt(criteria.uniqueResult().toString());


		/*logger.debug("userNonDealSuggestionCount => ");
		int rowCount = 0;
		//String hqlQuery = "select count(suggestionId) from Usernondealsuggestion where user.userId = :userId and suggestedTime = :currentWeekend";
		String hqlQuery = "select count(suggestionId) from Usernondealsuggestion where user.userId = :userId and (suggestedTime is null or suggestedTime >= :currentDate)";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			//query.setParameter("currentWeekend", Utils.getNearestWeekend(null));
			query.setParameter("currentDate", Utils.getCurrentDate());
			rowCount = ((Number)query.uniqueResult()).intValue();

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}*/
		return rowCount;
	}

	@Override
	public int getTotalUserDealSuggestions(Integer userId, String category, String suggestionType, double lat, double lon, int distance, double rating, int minPrice, int maxPrice) {
		logger.debug("getTotalUserDealSuggestions => ");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Userdealssuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.property("userDealMapId")).setProjection(Projections.groupProperty("deals.id"));
		criteria.createAlias("deals", "deals");
		//criteria.createAlias("business.businesstype", "businesstype");
		criteria.createAlias("user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));

		Criteria addr = criteria.createCriteria("deals.business"); 
		
		if(category != null && category.isEmpty() == false)
		{
			addr.createAlias("businesstype", "businesstype");
			addr.add(Restrictions.eq("businesstype.businessType", category));
		}

		criteria.add(Restrictions.disjunction().add(Restrictions.isNull("suggestedTime"))
				.add(Restrictions.ge("suggestedTime", Utils.getCurrentDateForEvent())));

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("deals.endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("deals.endAt", Utils.getNearestThursday())));

		if(suggestionType != null)
		{
			if(suggestionType.equalsIgnoreCase("Friend Suggestion"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.like("suggestionType", "%Friend%"))
						.add(Restrictions.like("suggestionType", "%Group%"))
						.add(Restrictions.eq("suggestionType", "Wall Feed Suggestion"))
						.add(Restrictions.eq("suggestionType", "Direct Suggestion")));
			}
			else if(suggestionType.equalsIgnoreCase("Community Suggestion"))
			{
				criteria.add(Restrictions.like("suggestionType", "%Community%"));
			}
			else if(suggestionType.equalsIgnoreCase("Jeeyoh Suggestion"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.like("suggestionType", "%User%"))
						.add(Restrictions.like("suggestionType", "%Friend%"))
						.add(Restrictions.like("suggestionType", "%Group%")));
			}
		}

		if(rating != 0)
			addr.add(Restrictions.ge("rating", rating));

		if(minPrice != 0 && maxPrice != 0)
		{
			criteria.createAlias("deals.dealoptions", "dealoptions");
			criteria.add(Restrictions.conjunction().add(Restrictions.ge("dealoptions.originalPrice", Long.parseLong(Integer.toString(minPrice))))
					.add(Restrictions.le("dealoptions.originalPrice", Long.parseLong(Integer.toString(maxPrice)))));
		}

		if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin(({alias}.lattitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos(({alias}.lattitude*pi()/180)) * cos((((" + lon + ")- {alias}.longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			addr.add(Restrictions.sqlRestriction(sql)); 
		}

		int rowCount = criteria.list().size();
		//int rowCount = Integer.parseInt(criteria.uniqueResult().toString());
		/*int rowCount = 0;
		//String hqlQuery = "select count(a.userDealMapId) from Userdealssuggestion a where a.user.userId = :userId and (a.deals.endAt >= :currentDate and a.deals.endAt > :weekendDate) and a.suggestedTime = :currentWeekend";
		String hqlQuery = "select count(a.userDealMapId) from Userdealssuggestion a where a.user.userId = :userId and (a.deals.endAt >= :currentDate and a.deals.endAt > :weekendDate)  and (a.suggestedTime is null or a.suggestedTime >= :currentDate )";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestThursday());
			query.setParameter("currentWeekend", Utils.getNearestWeekend(null));
			rowCount = ((Number)query.uniqueResult()).intValue();

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}*/
		return rowCount;
	}

	@Override
	public int getTotalUserEventSuggestions(Integer userId, String category, String suggestionType, double lat, double lon, int distance, double rating) {
		logger.debug("getTotalUserEventSuggestions => ");

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class, "events").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.property("events.eventId"));

		criteria.createAlias("events.usereventsuggestions", "usereventsuggestions");
		criteria.createAlias("usereventsuggestions.user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));


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

		criteria.add(Restrictions.ge("usereventsuggestions.suggestedTime", Utils.getCurrentDateForEvent()));
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
			else if(suggestionType.equalsIgnoreCase("Jeeyoh Suggestion"))
			{
				criteria.add(Restrictions.disjunction().add(Restrictions.like("usereventsuggestions.suggestionType", "%User%"))
						.add(Restrictions.like("usereventsuggestions.suggestionType", "%Friend%"))
						.add(Restrictions.like("usereventsuggestions.suggestionType", "%Group%")));
			}
		}

		if(rating != 0)
		{
			criteria.createAlias("page.communityReviewMap", "communityReviewMap");
			//criteria.createAlias("communityReviewMap.communityReview", "communityReview");
			Criteria review = criteria.createCriteria("communityReviewMap.communityReview"); 
			//a.setProjection(Projections.avg("communityReview.rating").as("avgRating"));
			//String groupBy = "this_.pageId having " + "avg({alias}.rating) >= " + rating;
			String groupBy = "this_.eventId having " + "avg({alias}.rating) >= " + rating;
			String[] alias = new String[1]; 
			alias[0] = "this_.eventId"; 
			Type[] types = new Type[1]; 
			types[0] = Hibernate.INTEGER;	
			//criteria.setProjection(Projections.alias(Projections.property("page.pageId"), "pageId1"));
			review.setProjection(Projections.sqlGroupProjection("this_.eventId", groupBy, alias, types));

		}

		if(lat != 0 && lon != 0)
		{
			String sql =  "(((acos(sin(((" + lat + ")*pi()/180)) * sin((this_.latitude*pi()/180))+cos(((" + lat + ")*pi()/180)) * cos((this_.latitude*pi()/180)) * cos((((" + lon + ")- this_.longitude)*pi()/180))))*180/pi())*60*1.1515)<="+distance;     
			criteria.add(Restrictions.sqlRestriction(sql)); 
		}

		int rowCount = criteria.list().size();
		/*int rowCount = 0;
		String hqlQuery = "select count(a.userEventMapId) from Usereventsuggestion a where a.user.userId = :userId and a.events.event_date_time_local >= :currentDate and a.suggestedTime >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			//query.setParameter("currentWeekend", Utils.getNearestWeekend(null));
			rowCount = ((Number)query.uniqueResult()).intValue();

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}*/
		return rowCount;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getUserCommunitiesByPageType(int userId, String pageType) {
		List<Page> pageList = null;
		String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c, Pagetype d where a.userId = :userId and d.pageType = :pageType and d.pageTypeId = b.pagetype.pageTypeId and c.user.userId = a.userId and b.pageId = c.page.pageId and b.business.id is null";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("pageType", pageType);
			pageList = (List<Page>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getUserCommunities(int userId) {
		logger.debug("pageList => ");
		List<Page> pageList = null;
		String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c where a.userId = :userId and c.user.userId = a.userId and b.pageId = c.page.pageId and b.business.id is null";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			pageList = (List<Page>) query.list();
			//logger.debug("pageList => " + pageList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return pageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dealsusage> getUseDealUsage(Integer userId, double latitude,
			double longitude) {
		logger.debug("getUseDealUsage::::");
		List<Dealsusage> dealUsageList = null;
		String hqlQuery = "select a from Dealsusage a, Deals b, Business c where a.user.userId = :userId and b.endAt >= :currentDate and a.deals.id = b.id and b.business.id = c.id and (((acos(sin(((:latitude)*pi()/180)) * sin((c.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((c.lattitude*pi()/180)) * cos((((:longitude)- c.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			dealUsageList = (List<Dealsusage>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return dealUsageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserCategoryLikes getUserCategoryLikes(int userId, int categoryId) {

		logger.debug("getUserCategoryLikes :: --> ");

		List<UserCategoryLikes> userCategoryLikes = null;
		String hqlQuery = "select a from UserCategoryLikes a, User b where b.userId =:userId and b.userId = a.user.userId and a.userCategory.userCategoryId =:categoryId ";
		//String hqlQuery = "from User a where a.emailId=:emailId and a.password=:password";
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("categoryId", categoryId);
			userCategoryLikes = query.list();

		}catch(HibernateException e)
		{
			logger.error(e.toString());
		}
		return userCategoryLikes != null && !userCategoryLikes.isEmpty() ? userCategoryLikes.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Dealsusage getUserLikeDeal(Integer userId,Integer itemId) {
		logger.debug("funboard :: "+itemId+"userId :::"+userId);
		List<Dealsusage>  deals = null;
		String hqlQuery = "from Dealsusage where user.userId=:userId and deals.id=:itemId";

		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("userId",userId);
			query.setParameter("itemId",itemId);
			deals = query.list();
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
		return deals != null && !deals.isEmpty() ? deals.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Eventuserlikes getUserLikeEvent(Integer userId,Integer itemId) {
		List<Eventuserlikes>  events = null;
		String hqlQuery = "from Eventuserlikes a where a.user.userId =:userId and event.eventId=:itemId";

		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("userId",userId);
			query.setParameter("itemId",itemId);
			events = query.list();
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
		return events != null && !events.isEmpty() ? events.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pageuserlikes getUserLikeCommunity(Integer userId,Integer itemId) {
		// TODO Auto-generated method stub
		List<Pageuserlikes>  pages = null;
		String hqlQuery = "from Pageuserlikes a where a.user.userId =:userId and page.pageId=:itemId";

		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("userId",userId);
			query.setParameter("itemId",itemId);
			pages = query.list();
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
		return pages != null && !pages.isEmpty() ? pages.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserByNameAndLocation(String location, String firstName, String lastName, int userId, List<Integer> friendsIds) {
		logger.debug("getUserByNameAndLocation location ::: ===> "+location + " name  :::: "+firstName + " friendsIds: "+friendsIds);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class, "user").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(location != null && !location.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("user.zipcode", "%" + location + "%"))
					.add(Restrictions.like("user.addressline1", "%" + location + "%"))
					.add(Restrictions.like("user.street", "%" + location + "%"))
					.add(Restrictions.like("user.city", "%" + location + "%"))
					.add(Restrictions.like("user.state", "%" + location + "%"))
					.add(Restrictions.like("user.country", "%" + location + "%")));
		}
		if(firstName != null && !firstName.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("user.firstName", "%" + firstName + "%"))
					.add(Restrictions.like("user.emailId", "%" + firstName + "%")));
		}
		if(lastName != null && !lastName.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("user.lastName", "%" + lastName + "%"))
					.add(Restrictions.like("user.emailId", "%" + lastName + "%")));
		}
		criteria.add(Restrictions.eq("user.isActive", true));
		criteria.add(Restrictions.ne("user.userId", userId));
		if(friendsIds != null && !friendsIds.isEmpty())
			criteria.add(Restrictions.not(Restrictions.in("user.userId",friendsIds)));

		List<User> userList = criteria.list();
		return userList;
	}


	@Override
	public void saveUsercontacts(Usercontacts userContacts) {
		logger.debug("saveUsercontacts ==>");
		try{
			sessionFactory.getCurrentSession().saveOrUpdate(userContacts);
		}
		catch(Exception e)
		{
			logger.error(e.toString());
		}

	}

	@Override
	public void updateUsercontacts(Usercontacts userContacts) {
		logger.debug("updateUsercontacts ==>");
		try{
			sessionFactory.getCurrentSession().update(userContacts);
		}
		catch(Exception e)
		{
			logger.error(e.toString());
		}

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findInMutualFriends(String firstName, String lastName,String friendsIds, String alreadyExistingIds) 
	{
		logger.debug("findInMutualFriends ==> name : "+ firstName + " ; friendsIds : "+friendsIds + " ; alreadyExistingIds : " + alreadyExistingIds);
		List<User> list = null;
		String hqlQuery = "select b from Usercontacts a, User b where";  
		if(firstName != null && !firstName.trim().equals(""))
			hqlQuery += "(b.firstName like '%"+firstName+"%' or b.emailId like '%"+firstName+"%')";
		if(lastName != null && !lastName.trim().equals(""))
			hqlQuery += "(b.firstName like '%"+lastName+"%' or b.emailId like '%"+lastName+"%')";
		hqlQuery += "and ((a.userByContactId.userId = b.userId and a.userByUserId.userId in("+friendsIds+") and a.userByContactId.userId not in ("+alreadyExistingIds+")) or (a.userByUserId.userId = b.userId and a.userByContactId.userId in("+friendsIds+") and a.userByUserId.userId not in ("+alreadyExistingIds+"))) group by b.userId";
		logger.debug("findInMutualFriends hqlQuery ===>"+hqlQuery);
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			list = (List<User>) query.list();
			logger.debug("list size"+list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class, "user").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("user.usercontactsesForUserId", "userId");
		criteria.createAlias("user.usercontactsesForContactId", "contactId");
		criteria.createAlias("contactId.userByContactId", "contactUser");
		if(name != null && !name.trim().equals(""))
		{
			//criteria.add(Restrictions.eq("contactId.userByContactId", "user"));
			criteria.add(Restrictions.disjunction().add(Restrictions.like("contactUser.emailId", "%" + name + "%"))
					.add(Restrictions.like("contactUser.firstName", "%" + name + "%"))
					.add(Restrictions.like("contactUser.middleName", "%" + name + "%"))
					.add(Restrictions.like("contactUser.lastName", "%" + name + "%")));
		}

		if(friendsIds!=null && !friendsIds.isEmpty())
		{
			criteria.add(Restrictions.in("userId.userByUserId.userId",friendsIds));
		}
		if(alreadyExistingIds!=null && !alreadyExistingIds.isEmpty())
		{
			criteria.add(Restrictions.not(Restrictions.in("contactUser.userId",alreadyExistingIds)));
		}
		List<User> userList = criteria.list();
		return userList;*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findOtherThanMutualFriends(String firstName, String lastName, List<Integer> alreadyExistingIds) {

		logger.debug("findOtherThanMutualFriends ==> name : "+ firstName + " ; alreadyExistingIds : "+alreadyExistingIds);

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class, "user").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(firstName != null && !firstName.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("user.firstName", "%" + firstName + "%"))
					.add(Restrictions.like("user.emailId", "%" + firstName + "%")));
		}
		if(lastName != null && !lastName.trim().equals(""))
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("user.lastName", "%" + lastName + "%"))
					.add(Restrictions.like("user.emailId", "%" + lastName + "%")));
		}
		if(alreadyExistingIds!=null && !alreadyExistingIds.isEmpty())
		{
			criteria.add(Restrictions.not(Restrictions.in("user.userId",alreadyExistingIds)));
		}

		List<User> userList = criteria.list();
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Usercontacts isUsercontactExists(int userId, int contactId) {
		logger.debug("getUsercontacts ==> userID : "+ userId + " ; contactId : "+contactId);
		List<Usercontacts> usercontacts =  null;
		//String hqlQuery = "select a from Usercontacts a where a.userByUserId.userId =:userId and a.userByContactId.userId =:contactId";
		String hqlQuery = "select a from Usercontacts a where (a.userByUserId.userId = :userId or a.userByContactId.userId = :userId) and (a.userByUserId.userId = :contactId or a.userByContactId.userId = :contactId)";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("contactId", contactId);
			usercontacts = (List<Usercontacts>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usercontacts!=null  && !usercontacts.isEmpty() ? usercontacts.get(0):null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<User> getGroupMembers(int groupId, int userId) {
		List<User> contactList = null;
		//String hqlQuery = "select a from User a, Usercontacts b where b.userByUserId.userId = :userId and a.userId = b.userByContactId.userId and b.isApproved is true";
		String hqlQuery = "select a from User a, Groupusermap b where b.jeeyohgroup.groupId = :groupId and b.user.userId != :userId and a.userId = b.user.userId and (a.isShareProfileWithGroup is true or a.isShareCommunity is true)";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("groupId", groupId);
			contactList = (List<User>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contactList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Usereventsuggestion isEventSuggestionExistsForDirectSuggestion(int userId,
			int eventId, int userContactId) {

		List<Usereventsuggestion> usereventsuggestions = null;
		String hqlQuery = "from Usereventsuggestion a where a.events.eventId = :eventId and a.user.userId =:userId and a.userContact.userId = :userContactId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("eventId", eventId);
			query.setParameter("userId", userId);
			query.setParameter("userContactId", userContactId);
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions != null && !usereventsuggestions.isEmpty() ? usereventsuggestions.get(0) : null;

	}


	@SuppressWarnings("unchecked")
	@Override
	public Usernondealsuggestion isNonDealSuggestionExistsForDirectSuggestion(int userId,
			int businessId, int userContactId) {
		List<Usernondealsuggestion> usernondealsuggestions = null;
		String hqlQuery = "from Usernondealsuggestion a where a.business.id = :businessId and a.user.userId =:userId and a.userContact.userId = :userContactId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("businessId", businessId);
			query.setParameter("userId", userId);
			query.setParameter("userContactId", userContactId);
			usernondealsuggestions = (List<Usernondealsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usernondealsuggestions != null && !usernondealsuggestions.isEmpty() ? usernondealsuggestions.get(0) : null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Userdealssuggestion isDealSuggestionExistsForDirectSuggestion(
			int userId, int dealId, int userContactId) {
		List<Userdealssuggestion> userdealssuggestions = null;
		String hqlQuery = "from Userdealssuggestion a where a.deals.id = :dealId and a.user.userId =:userId and a.userContact.userId = :userContactId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("dealId", dealId);
			query.setParameter("userId", userId);
			query.setParameter("userContactId", userContactId);
			query.setMaxResults(1);
			userdealssuggestions = (List<Userdealssuggestion>) query.list();
		} catch (Exception e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return userdealssuggestions != null && !userdealssuggestions.isEmpty() ? userdealssuggestions.get(0) : null;

	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Usereventsuggestion isEventSuggestionExistsForDirectSuggestion(int userId,
			int eventId) {

		List<Usereventsuggestion> usereventsuggestions = null;
		String hqlQuery = "from Usereventsuggestion a where a.events.eventId = :eventId and a.user.userId =:userId and a.suggestedTime >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("eventId", eventId);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDateForEvent());
			query.setMaxResults(1);
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions != null && !usereventsuggestions.isEmpty() ? usereventsuggestions.get(0) : null;

	}


	@SuppressWarnings("unchecked")
	@Override
	public Usernondealsuggestion isNonDealSuggestionExistsForDirectSuggestion(int userId,
			int businessId) {
		List<Usernondealsuggestion> usernondealsuggestions = null;
		String hqlQuery = "from Usernondealsuggestion a where a.business.id = :businessId and a.user.userId =:userId and a.suggestedTime >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("businessId", businessId);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDateForEvent());
			query.setMaxResults(1);
			usernondealsuggestions = (List<Usernondealsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usernondealsuggestions != null && !usernondealsuggestions.isEmpty() ? usernondealsuggestions.get(0) : null;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Userdealssuggestion isDealSuggestionExistsForDirectSuggestion(
			int userId, int dealId) {
		List<Userdealssuggestion> userdealssuggestions = null;
		String hqlQuery = "from Userdealssuggestion a where a.deals.id = :dealId and a.user.userId =:userId and a.suggestedTime >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("dealId", dealId);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDateForEvent());
			userdealssuggestions = (List<Userdealssuggestion>) query.list();
		} catch (Exception e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return userdealssuggestions != null && !userdealssuggestions.isEmpty() ? userdealssuggestions.get(0) : null;

	}

	@Override
	public void updateUserDealSuggestion(Userdealssuggestion userdealssuggestion) {
		try{
			sessionFactory.getCurrentSession().update(userdealssuggestion);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}

	}

	@Override
	public void updateUserNonDealSuggestion(
			Usernondealsuggestion usernondealsuggestion) {
		try{
			sessionFactory.getCurrentSession().update(usernondealsuggestion);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
	}

	@Override
	public void updateUserEventSuggestion(
			Usereventsuggestion usereventsuggestion) {
		try{
			sessionFactory.getCurrentSession().update(usereventsuggestion);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public Dealsusage getUserDealProperties(int userId, int dealId) {
		List<Dealsusage> dealProperties = null;
		String hqlQuery = "select b from Dealsusage b where b.user.userId = :userId and b.deals.id = :dealId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("dealId", dealId);
			dealProperties = (List<Dealsusage>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealProperties != null && !dealProperties.isEmpty() ? dealProperties.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserFriendRequests(int userId) {
		List<User> contactList = null;
		String hqlQuery = "select a from User a, Usercontacts b where (b.userByContactId.userId = :userId and a.userId = b.userByUserId.userId) and (b.isApproved is false and b.isDeny is false and isBlock is false) group by a.userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			contactList = (List<User>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contactList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserSession isSessionActive(int userId, String sessionId) {
		List<UserSession> userSessions = null;
		String hqlQuery = "select a from UserSession a where a.user.userId = :userId and a.sessionId = :sessionId and a.expirationTime >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("sessionId", sessionId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			userSessions = (List<UserSession>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userSessions != null && !userSessions.isEmpty() ? userSessions.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getGroupMembersWithinFiftyMiles(int groupId, int userId,
			double latitude, double longitude) {
		List<User> contactList = null;

		String hqlQuery = "select a from User a, Groupusermap b where b.jeeyohgroup.groupId = :groupId and b.user.userId != :userId and a.userId = b.user.userId and (a.isShareProfileWithGroup is true or a.isShareCommunity is true) and (((acos(sin(((:lat)*pi()/180)) * sin((a.lattitude*pi()/180))+cos(((:lat)*pi()/180)) * cos((a.lattitude*pi()/180)) * cos((((:long)- a.longitude)*pi()/180))))*180/pi())*60*1.1515)<=50";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("groupId", groupId);
			contactList = (List<User>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contactList;
	}

	@Override
	public long getUserFavouriteCount(String category, int userId) {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(UserCategoryLikes.class)
				.setProjection(Projections.count("userCategoryLikesId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("userCategory", "userCategory");
		criteria.add(Restrictions.eq("userCategory.itemCategory", category));
		criteria.createAlias("user", "user");        
		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.setProjection(Projections.rowCount());
		Long resultCount = (Long)criteria.uniqueResult();
		logger.debug("count :: ==> "+resultCount);
		return resultCount;
	}


}
