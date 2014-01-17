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

import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;

@Repository("userDAO")
public class UserDAO implements IUserDAO {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

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
	@Override
	public List<Usercontacts> getAllUserContacts(int userId) {
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
	@Override
	public List<Page> getUserCommunities(int userId) {
		logger.debug("pageList => ");
		List<Page> pageList = null;
		String hqlQuery = "select b from User a, Page b, Pageuserlikes c where a.userId = :userId and c.user.userId = a.userId and b.pageId = c.page.pageId";
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

	@Override
	public List<Events> getUserCommunityEvents(int userId, int pageId) {
		logger.debug("pageList => ");
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
        logger.debug("EEEEEEEEEEE :: "+userList.get(0));
        return userList.get(0);
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
		/*List<Usernondealsuggestion> usernondealsuggestions = null;
		String hqlQuery = "select a from Usernondealsuggestion a where a.user.userId = :userId and a.business.id = :businessId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("businessId", businessId);
			usernondealsuggestions = (List<Usernondealsuggestion>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 */		return usernondealsuggestions;
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
	  logger.debug("pageList => ");
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
}
