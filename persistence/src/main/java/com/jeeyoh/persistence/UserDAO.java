package com.jeeyoh.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.Profiletype;
import com.jeeyoh.persistence.domain.Topdealssuggestion;
import com.jeeyoh.persistence.domain.Topeventsuggestion;
import com.jeeyoh.persistence.domain.Topnondealsuggestion;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
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
		System.out.println("inside getUsers");
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
		String hqlQuery = "select a from User a, Usercontacts b where b.userByUserId.userId = :userId and a.userId = b.userByContactId.userId";
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
	public List<Usercontacts> getAllUserContacts(int userId) {
		logger.debug("getAllUserContacts userId::  "+userId);
		List<Usercontacts> contactList = null;
		String hqlQuery = "from Usercontacts b where b.userByUserId.userId = :userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			contactList = (List<Usercontacts>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contactList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Page> getUserCommunities(int userId) {
		logger.debug("pageList => ");
		List<Page> pageList = null;
		String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c where a.userId = :userId and c.user.userId = a.userId and b.pageId = c.page.pageId";
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
	public List<Jeeyohgroup> getUserGroups(int userId) {
		List<Jeeyohgroup> groupList = null;
		String hqlQuery = "select b from User a, Jeeyohgroup b, Groupusermap c where a.userId = :userId and c.user.userId = a.userId and b.groupId = c.jeeyohgroup.groupId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			groupList = (List<Jeeyohgroup>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupList;
	}

	@Override
	public List<Jeeyohgroup> getUserContactGroups(int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pageuserlikes> getUserPageProperties(int userId, int pageId) {
		List<Pageuserlikes> pageProperties = null;
		String hqlQuery = "select c from User a, Page b, Pageuserlikes c where a.userId = :userId and c.user.userId = a.userId and b.pageId = c.page.pageId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			pageProperties = (List<Pageuserlikes>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageProperties;
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
	public List<User> getUserById(int userId) {
		System.out.println("inside getUsers");
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
		return userList;
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
	public User getUsersById(String id) {
		List<User> userList = null;
		logger.debug("Email id ::: = "+id);
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		String hqlQuery = "from User a where a.emailId = :emailId";
		try{
			tx = session.beginTransaction();
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("emailId", id);
			userList = (List<User>)query.list();
			//tx.commit();
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
			//session.close();
		}
		logger.debug("user:: "+userList);
		return userList != null && !userList.isEmpty() ? userList.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Eventuserlikes> getUserEventProperties(int userId, int eventId) {
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
		return eventProperties;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usernondealsuggestion> isNonDealSuggestionExists(int userId,
			int businessId) {

		logger.debug("UserId ==> "+userId +" businessid ==> "+businessId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usernondealsuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("business.id", businessId));

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
		String hqlQuery = "select b from User a, UserCategory b, UserCategoryLikes c where a.userId = :userId and b.itemCategory = :category and c.user.userId = a.userId and b.userCategoryId = c.userCategory.userCategoryId";
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
	public List<Page> getUserCommunitiesByPageType(int userId, String pageType) {
		logger.debug("pageList => ");
		List<Page> pageList = null;
		String hqlQuery = "select distinct b from User a, Page b, Pageuserlikes c, Pagetype d where a.userId = :userId and d.pageType = :pageType and d.pageTypeId = b.pagetype.pageTypeId and c.user.userId = a.userId and b.pageId = c.page.pageId";
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

	@Override
	public int userCategoryLikeCount(Integer userCategoryId) {
		logger.debug("userCategoryLikeCount => ");
		int rowCount = 0;
		String hqlQuery = "select count(*) from UserCategoryLikes a where a.userCategory.userCategoryId = :userCategoryId";
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
	public List<Events> getUserLikesEvents(int userId) {
		logger.debug("getUserLikesEvents => ");
		List<Events> eventsList = null;
		String hqlQuery = "select b from User a, Events b, Eventuserlikes c where a.userId = :userId and c.user.userId = a.userId and b.eventId = c.event.eventId and b.endAt >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
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
	public List<Events> getUserLikesEventsByType(int userId, String pageType) {
		logger.debug("getUserLikesEventsByType => ");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Events.class);
		criteria.add(Restrictions.eq("eventuserlikes.user.userId", userId));
		criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));
		criteria.createAlias("page", "page");
		criteria.createAlias("page.pagetype", "pagetype");  
		criteria.add(Restrictions.eq("pagetype.pageType", pageType));  
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usereventsuggestion> isEventSuggestionExists(int userId,
			int eventId) {

		logger.debug("UserId ==> "+userId +" eventId ==> "+eventId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usereventsuggestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.add(Restrictions.eq("events.eventId", eventId));

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
		List<Userdealssuggestion> userdealsuggestions = criteria.list();
		return userdealsuggestions;


	}

	@Override
	public void registerUser(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);   
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<Dealsusage> getUserDealUsageByType(Integer userId,
			String groupType) {
		// TODO Auto-generated method stub
		Set<Dealsusage> dealUsage = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Dealsusage.class);
		criteria.add(Restrictions.eq("user.userId", userId));
		criteria.createAlias("deals", "deals");
		criteria.createAlias("deals.business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		criteria.add(Restrictions.eq("businesstype.businessType", groupType));
		List<Dealsusage> dealUsageList = criteria.list();	
		if(dealUsage != null)
		{
			dealUsage = new HashSet<Dealsusage>(dealUsageList);
		}
		return dealUsage ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User loginUser(UserModel user) {
		// TODO Auto-generated method stub
		String emailId  = user.getEmailId();
		String password = user.getPassword();
		
		List<User> users = null;
		String hqlQuery = "from User where emailId=:emailId and password=:password and isActive=:isActive ";
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("emailId", emailId);
			query.setParameter("password", password);
			query.setParameter("isActive",true);
			users = query.list();
			
		}catch(HibernateException e)
		{
			
		}
		return users != null && !users.isEmpty() ? users.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getCommunityAllEvents(int pageId) {
		logger.debug("getUserCommunityEvents => ");
		List<Events> eventsList = null;
		String hqlQuery = "select a from Events a where a.page.pageId = :pageId and a.event_date >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("pageId", pageId);
			query.setParameter("currentDate", Utils.getCurrentDate());
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
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().update(user);
		
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

	@Override
	public Pageuserlikes isPageExistInUserProfile(int userId,  int pageId) {
		List<Pageuserlikes> list = null;
		String queryString = "from Pageuserlikes where user.userId =: userId and page.pageId =: pageId";
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
		}
		return list != null && list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public void saveUserCommunity(Pageuserlikes pageUserLike) {
<<<<<<< HEAD
		sessionFactory.getCurrentSession().save(pageUserLike);
=======
		// TODO Auto-generated method stub
		try{
		sessionFactory.getCurrentSession().saveOrUpdate(pageUserLike);
		}
		catch(Exception e)
		{
			logger.error(e.toString());
		}
>>>>>>> 4b9e97ef57978452620b1a89ecf6542ec5a03bc7
		
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
			int userId) {
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usernondealsuggestion.class,"usernondealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("usernondealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));
		
		List<Usernondealsuggestion> userNonDealSuggestions = criteria.list();*/
		
		List<Usernondealsuggestion> userNonDealSuggestions = null;
		String hqlQuery = "select a from Usernondealsuggestion a, Pageuserlikes b, Page c, User d where d.userId = :userId and a.user.userId = d.userId and c.business.id = a.business.id and b.page.pageId = c.pageId and b.user.userId = d.userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			userNonDealSuggestions = (List<Usernondealsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userNonDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForFriends(int userId, int contactId) {
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Usereventsuggestion.class,"usereventsuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("usereventsuggestion.user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));
		

		List<Usereventsuggestion> usereventsuggestions = criteria.list();*/
		
		List<Usereventsuggestion> usereventsuggestions = null;
		String hqlQuery = "select a from Usereventsuggestion a, Eventuserlikes b, User d where d.userId = :userId and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and b.event.eventId = a.events.eventId and b.user.userId = :contactId and a.events.event_date > :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("contactId", contactId);
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForFriends(int userId, int contactId) {
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Userdealssuggestion.class,"userdealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("userdealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));
		
		List<Userdealssuggestion> userDealSuggestions = criteria.list();*/
		List<Userdealssuggestion> userDealSuggestions = null;
		//String hqlQuery = "select count(*) from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userid and a.userid = d.userid and b.deals.id = a.deals.id and b.user.userId = d.userId and a.deals.endAt > :currentDate";
		String hqlQuery = "select a from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userId and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and b.deals.id = a.deals.id and b.user.userId = :contactId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("contactId", contactId);
			//query.setParameter("currentDate", Utils.getCurrentDate());
			userDealSuggestions = (List<Userdealssuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userDealSuggestions;
	}

	@Override
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForJeeyoh(
			int userId) {
		List<Userdealssuggestion> userDealSuggestions = null;
		//String hqlQuery = "select count(*) from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userid and a.userid = d.userid and b.deals.id = a.deals.id and b.user.userId = d.userId and a.deals.endAt > :currentDate";
		String hqlQuery = "select a from Userdealssuggestion a, Dealsusage b, Deals c, User d where d.userId = :userId and a.user.userId = d.userId and c.id = a.deals.id and c.dealSource = 'jeeyoh' and b.deals.id = a.deals.id and b.user.userId = d.userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			//query.setParameter("currentDate", Utils.getCurrentDate());
			userDealSuggestions = (List<Userdealssuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForJeeyoh(
			int userId) {
		

		List<Usereventsuggestion> usereventsuggestions = null;
		String hqlQuery = "select a from Usereventsuggestion a, Eventuserlikes b, User d where d.userId = :userId and a.user.userId = d.userId and c.eventId = a.events.eventId and c.eventSource = 'jeeyoh' and b.event.eventId = a.events.eventId and b.user.userId = :contactId and a.events.event_date > :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions;
	}

	@Override
	public List<Usernondealsuggestion> getUserNonDealsSuggestionByUserIdForJeeyoh(
			int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForCommunity(
			int userId) {
		List<Userdealssuggestion> userDealSuggestions = null;
		//String hqlQuery = "select count(*) from Userdealssuggestion a, Dealsusage b, User d where d.userId = :userid and a.userid = d.userid and b.deals.id = a.deals.id and b.user.userId = d.userId and a.deals.endAt > :currentDate";
		String hqlQuery = "select a from Userdealssuggestion a, Page b, Pageuserlikes c, User d, Deals e where d.userId = :userId and a.user.userId = d.userId and e.id = a.deals.id and e.business.id = b.business.id and c.page.pageId = b.pageId and c.user.userId = d.userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			//query.setParameter("currentDate", Utils.getCurrentDate());
			userDealSuggestions = (List<Userdealssuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForCommunity(
			int userId) {
		List<Usereventsuggestion> usereventsuggestions = null;
		String hqlQuery = "select a from Usereventsuggestion a, Page b, Pageuserlikes c, User d, Events e where d.userId = :userId and a.user.userId = d.userId and e.eventId = a.events.eventId and e.page.id = b.pageId and c.page.id = b.pageId and c.user.userId = d.userId and a.events.event_date > :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			usereventsuggestions = (List<Usereventsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usereventsuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usernondealsuggestion> getUserNonDealsSuggestionByUserIdForFriends(
			int userId, int contactId) {
		logger.debug("getUserNonDealsSuggestionByUserIdForFriends"+userId +" : "+contactId);
		List<Usernondealsuggestion> userNonDealSuggestions = null;
		String hqlQuery = "select a from Usernondealsuggestion a, Pageuserlikes b, Page c, User d where d.userId = :userId and a.user.userId = d.userId and (a.suggestionType like '%Friend%' or a.suggestionType like '%Group%') and c.business.id = a.business.id and b.page.pageId = c.pageId and b.user.userId = :contactId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("contactId", contactId);
			userNonDealSuggestions = (List<Usernondealsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userNonDealSuggestions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> userDealSuggestionCount(String dealIdsStr) {
		List<Object[]> rows = null;
		String hqlQuery = "select count(*) as likes ,a.deals.id from Dealsusage a where (a.isLike is true or a.isFavorite is true)  and a.deals.id in("+ dealIdsStr +") group by a.deals.id order by likes desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			
			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> userEventSuggestionCount(String eventIdsStr) {
		List<Object[]> rows = null;
		String hqlQuery = "select count(*) as likes ,a.event.eventId from Eventuserlikes a where (a.isLike is true or a.isFavorite is true or a.isVisited is true)  and a.event.eventId in("+ eventIdsStr +") group by a.event.eventId order by likes desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			
			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> userNonDealSuggestionCount(String pageIdsStr) {
		List<Object[]> rows = null;
		String hqlQuery = "select count(*) as likes ,a.page.pageId from Pageuserlikes a where (a.isLike is true or a.isFavorite is true or a.isVisited is true)  and a.page.pageId in("+ pageIdsStr +") group by a.page.pageId order by likes desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			
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
}
