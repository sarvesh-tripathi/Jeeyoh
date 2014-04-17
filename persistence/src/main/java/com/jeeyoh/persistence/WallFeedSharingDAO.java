package com.jeeyoh.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.WallFeedSharing;

@Repository("wallFeedSharingDAO")
public class WallFeedSharingDAO implements IWallFeedSharingDAO {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveWallFeedSharing(WallFeedSharing wallFeedSharing) {
		logger.debug("saveFunBoard => ");
		Session session =  sessionFactory.getCurrentSession();
		try
		{
			session.save(wallFeedSharing);
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.error("ERROR IN DAO :: = > "+e);
		}
		
	}
	
}
