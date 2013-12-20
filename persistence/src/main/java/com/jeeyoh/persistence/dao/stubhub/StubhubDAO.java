package com.jeeyoh.persistence.dao.stubhub;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.StubhubEvent;

@Repository("stubhubDAO")
public class StubhubDAO implements IStubhubDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Override
	public void save(StubhubEvent stunhub) {
		// TODO Auto-generated method stub
		Session session  = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try
		{
			
			session.save(stunhub);				
			tx.commit();
			session.close();
		}
		catch(HibernateException e)
		{
			logger.error("ERROR IN DAO :: = > "+e);
		}
		
	}

}
