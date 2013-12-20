package com.jeeyoh.persistence.dao.fandango;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.FandangoCommingSoon;
import com.jeeyoh.persistence.domain.FandangoNearMe;
import com.jeeyoh.persistence.domain.FandangoOpenning;
import com.jeeyoh.persistence.domain.FandangoTopTen;

@Repository("fandangoDAO")
public class FandangoDAO implements IFandangoDAO {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void saveTopTen(FandangoTopTen fandango) {
		// TODO Auto-generated method stub
		Session session  = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try
		{
			
			session.save(fandango);				
			tx.commit();
			session.close();
		}
		catch(HibernateException e)
		{
			logger.error("ERROR IN DAO :: = > "+e);
		}
		
	}
	@Override
	public void saveCommingSoon(FandangoCommingSoon fandango) {
		// TODO Auto-generated method stub
		Session session  = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try
		{
			
			session.save(fandango);				
			tx.commit();
			session.close();
		}
		catch(HibernateException e)
		{
			logger.error("ERROR IN DAO :: = > "+e);
		}
		
	}
	@Override
	public void saveNewOpning(FandangoOpenning fandango) {
		// TODO Auto-generated method stub
		Session session  = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try
		{
			
			session.save(fandango);				
			tx.commit();
			session.close();
		}
		catch(HibernateException e)
		{
			logger.error("ERROR IN DAO :: = > "+e);
		}
		
	}
	@Override
	public void saveMovieNearMe(FandangoNearMe fandango) {
		// TODO Auto-generated method stub
		Session session  = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try
		{
			
			session.save(fandango);				
			tx.commit();
			session.close();
		}
		catch(HibernateException e)
		{
			logger.error("ERROR IN DAO :: = > "+e);
		}
	}

}
