package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.BetaListUser;

@Repository("betaListUserserDAO")
public class BetaListUserDAO implements IBetaListUserDAO{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void registerUser(BetaListUser betaListUser) {
		try{
			sessionFactory.getCurrentSession().save(betaListUser);
		}
		catch(Exception e)
		{
			logger.error(e.toString());
		}
	}

	@Override
	public void confirmUser(BetaListUser betaListUser) {
		try{
			sessionFactory.getCurrentSession().update(betaListUser);
		}
		catch(Exception e)
		{
			logger.error(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public BetaListUser getUser(String emailId, String confirmationId) {
		logger.debug("userList => ");
		List<BetaListUser> userList = null;
		String hqlQuery = "from BetaListUser a where a.emailId = :emailId and a.confirmationId = :confirmationId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("emailId", emailId);
			query.setParameter("confirmationId", confirmationId);
			userList = (List<BetaListUser>) query.list();
			logger.debug("userList => " + userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList != null && !userList.isEmpty() ? userList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BetaListUser isUserExist(String emailId) {
		logger.debug("userList => ");
		List<BetaListUser> userList = null;
		String hqlQuery = "from BetaListUser a where a.emailId = :emailId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("emailId", emailId);
			userList = (List<BetaListUser>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList != null && !userList.isEmpty() ? userList.get(0) : null;
	}
}


