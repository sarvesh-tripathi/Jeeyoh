package com.jeeyoh.persistence.dao.stubhub;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.StubhubEvent;
import com.jeeyoh.utils.Utils;

@Repository("stubhubDAO")
public class StubhubDAO implements IStubhubDAO {

	@Autowired
	private SessionFactory sessionFactory;

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Override
	public void save(StubhubEvent stunhub, int count) {
		Session session  = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try
		{

			session.save(stunhub); 
			if( count % 20 == 0 ) {
				session.flush();
				session.clear();
			}
			tx.commit();

		}
		catch(HibernateException e)
		{
			logger.error("ERROR IN DAO :: = > "+e);
		}
		finally
		{
			session.close();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StubhubEvent> getStubhubEvents() {
		List<StubhubEvent> eventsList = null;
		String hqlQuery = "from StubhubEvent where event_date_time_local >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("currentDate", Utils.getCurrentDate());
			//query.setParameter("currentTime", Utils.getCurrentTimeForEvent().toString());
			eventsList = (List<StubhubEvent>) query.list();
		} catch (Exception e) {
			logger.debug("ERROR::  "+e.getMessage());
			e.printStackTrace();
		}
		return eventsList;
	}

}
