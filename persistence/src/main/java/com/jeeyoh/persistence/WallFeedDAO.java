package com.jeeyoh.persistence;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.WallFeed;
import com.jeeyoh.persistence.domain.WallFeedItems;
import com.jeeyoh.persistence.domain.WallFeedUserShareMap;

@Repository("wallFeedSharingDAO")
public class WallFeedDAO implements IWallFeedDAO {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveWallFeedSharing(WallFeed wallFeedS) {
		logger.debug("saveFunBoard => ");
		Session session =  sessionFactory.getCurrentSession();
		try
		{
			session.save(wallFeedS);
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.error("ERROR IN DAO :: = > "+e);
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WallFeed> getWallFeedSharing() {
		// TODO Auto-generated method stub
		
		List<WallFeed> list = null;
		String hqlquery = "from WallFeedSharing";
		try{
			
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			list = query.list();
			
		}catch(HibernateException e)
		{
			logger.debug(e.toString());
		}
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WallFeedItems> getDistinctWallFeedSharing() {
		List<WallFeedItems> list = null;
		String hqlquery = "from WallFeedSharing group by packageName";
		try{
			
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			list = query.list();
			
		}catch(HibernateException e)
		{
			logger.debug(e.toString());
		}
		
		return list;
	}
	
	
	@Override
	public void updateWallFeedSharing(WallFeedItems feedSharing) {
		// TODO Auto-generated method stub
		try
		{
			sessionFactory.getCurrentSession().update(feedSharing);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
		
	}

	@Override
	public void deleteWallFeedSharing(WallFeedItems feedSharing) {
		// TODO Auto-generated method stub
		
		try
		{
			sessionFactory.getCurrentSession().delete(feedSharing);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
	}
	
	@Override
	public double getAVGItemRank(int packageId) {
		double rowCount = 0;
		String hqlquery = "select avg(itemRank) from WallFeedSharing where wallFeed.packageId=:packageId";
		try{
			//Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("packageId", packageId);
					
			rowCount = ((Double)query.uniqueResult()).doubleValue();
			
		}
		catch(Exception e)
		{
			
			logger.error(e.toString());
		}
		logger.debug("getAVGItemRank Count"+rowCount);
		return rowCount;
		
	}
	
	@Override
	public void updatePackageRank(double packageRank, String packageName) {
		// TODO Auto-generated method stub
		
		String hqlquery = "update WallFeedSharing set packageRank=:packageRank where packageName=:packageName";
		try{
			
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("packageRank", packageRank);
			query.setParameter("packageName", packageName);
			 query.executeUpdate();
			
		}catch(HibernateException e)
		{
			logger.debug(e.toString());
		}
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<WallFeedUserShareMap> getMemoryCard(int userId, String searchText,
			Date startDate, Date endDate) {
		List<WallFeedUserShareMap> list = null;
		String hqlquery = "select a from WallFeedUserShareMap a, WallFeedItems b where a.shareWithUser.userId = :userId and a.wallFeed.packageId = b.wallFeed.packageId and b.funboard.tag like '%" + searchText + "%' and a.createdTime >= :startDate and a.createdTime <= :endDate";
		try{
			
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("userId", userId);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			list = query.list();
			
		}catch(HibernateException e)
		{
			logger.debug(e.toString());
		}
		return list;
	}

	
}
