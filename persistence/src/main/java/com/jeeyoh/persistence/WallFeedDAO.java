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
import com.jeeyoh.persistence.domain.WallFeedComments;
import com.jeeyoh.persistence.domain.WallFeedItems;
import com.jeeyoh.persistence.domain.WallFeedUserShareMap;

@Repository("wallFeedDAO")
public class WallFeedDAO implements IWallFeedDAO {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveWallFeed(WallFeed wallFeedS) {
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
		String hqlquery = "from WallFeed";
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
		String hqlquery = " from WallFeedItems group by wallFeed.wallFeedId";
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
	public double getAVGItemRank(int wallFeedId) {
		double rowCount = 0;
		String hqlquery = "select avg(itemRank) from WallFeedItems where wallFeed.wallFeedId=:wallFeedId";
		try{
			//Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("wallFeedId", wallFeedId);
					
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
	public void updatePackageRank(double packageRank, int wallFeedId) {
		String hqlquery = "update WallFeedItems set packageRank=:packageRank where wallFeed.wallFeedId=:wallFeedId";
		try{
			
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("packageRank", packageRank);
			query.setParameter("wallFeedId", wallFeedId);
			 query.executeUpdate();
			
		}catch(HibernateException e)
		{
			logger.debug(e.toString());
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> getMemoryCard(int userId, String searchText,
			Date startDate, Date endDate, String category) {
		//logger.debug("getMemoryCard: "+ startDate + " endDate: "+endDate+" category: "+category);
		List<String> list = null;
		//String hqlquery = "select mediaPathUrl from FunboardMediaContent where funBoardId in (select c.funBoardId from WallFeed a, WallFeedItems b, Funboard c where a.user.userId = :userId and a.wallFeedId = b.wallFeed.wallFeedId and b.funboard.funBoardId = c.funBoardId and c.tag like '%"+ searchText + "%' and (a.createdTime >= :startDate and a.createdTime <= :endDate))";
		String hqlquery = "select mediaPathUrl from FunboardMediaContent where funBoardId in (select distinct c.funBoardId from WallFeed a, WallFeedItems b, Funboard c where a.user.userId = :userId and a.wallFeedId = b.wallFeed.wallFeedId and b.funboard.funBoardId = c.funBoardId and c.category = :category and (a.createdTime >= :startDate and a.createdTime <= :endDate))";
		
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("userId", userId);
			query.setParameter("category", category);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			list = query.list();

		}catch(HibernateException e)
		{
			logger.debug(e.toString());
		}
		return list;
	}

	@Override
	public void saveWallFeedComments(WallFeedComments wallFeedComments) {
		logger.debug("saveWallFeedComments => ");
		try
		{
			sessionFactory.getCurrentSession().saveOrUpdate(wallFeedComments); 
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.error("ERROR IN DAO :: = > "+e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WallFeedComments> getWallFeedCommentsById(int wallFeedId) {

		logger.debug("getWallFeedCommentsById =>"+wallFeedId);
		List<WallFeedComments> wallFeedCommentstList = null;
		String hqlQuery = "from WallFeedComments a where a.wallFeed.wallFeedId = :wallFeedId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("wallFeedId", wallFeedId);
			wallFeedCommentstList = (List<WallFeedComments>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wallFeedCommentstList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public WallFeed getWallFeedDetailsByID(int wallFeedId)
	{
		List<WallFeed> wallFeedList = null;
		String hqlQuery = "from WallFeed a where a.wallFeedId = :wallFeedId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("wallFeedId", wallFeedId);
			wallFeedList = (List<WallFeed>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wallFeedList != null && !wallFeedList.isEmpty() ? wallFeedList.get(0) : null; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WallFeed> getUserWallFeed(int userId) {

		logger.debug("getUserWallFeed => ");
		List<WallFeed> wallFeed = null;
		//String hqlQuery = "select a,b.packageRank from WallFeed a, WallFeedItems b where a.user.userId = :userId and a.wallFeedId = b.wallFeed.wallFeedId group by a.wallFeedId order by b.packageRank desc";
		String hqlQuery = "select a from WallFeed a left join a.wallFeedUserShareMap b left join a.wallFeedItems c where (a.user.userId = :userId or b.shareWithUser.userId = :userId) group by a.wallFeedId order by c.packageRank desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("userId", userId);
			wallFeed = (List<WallFeed>) query.list();
			logger.debug("wallFeed => " + wallFeed);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("ERROR: "+e.getMessage());
		}
		return wallFeed;

	}

	@Override
	public void saveWallFeedShareMap(WallFeedUserShareMap wallFeedUserShareMap) {
		Session session =  sessionFactory.getCurrentSession();
		try
		{
			session.save(wallFeedUserShareMap);

		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.error("ERROR IN DAO :: = > "+e);
		}

	}


	@SuppressWarnings("unchecked")
	@Override
	public WallFeedUserShareMap isWallFeedAlreadyShared(int userId,
			int wallFeedId, int sharedUserId) {
		List<WallFeedUserShareMap> wallFeedList = null;
		String hqlQuery = "from WallFeedUserShareMap a where a.wallFeed.wallFeedId = :wallFeedId and a.shareWithUser.userId = :sharedUserId and a.user.userId = :userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("wallFeedId", wallFeedId);
			query.setParameter("sharedUserId", sharedUserId);
			query.setParameter("userId", userId);
			wallFeedList = (List<WallFeedUserShareMap>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wallFeedList != null && !wallFeedList.isEmpty() ? wallFeedList.get(0) : null; 
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getSharedWithUserWallFeed(int userId) {
		logger.debug("getUserWallFeed => ");
		List<Object[]> wallFeed = null;
		String hqlQuery = "select a,b.packageRank from WallFeed a WallFeedItems b, WallFeedUserShareMap c where a.shareWithUser.userId = :userId and a.wallFeedId = b.wallFeed.wallFeedId and a.wallFeedId = c.wallFeed.wallFeedId group by a.wallFeedId order by b.packageRank desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("userId", userId);
			wallFeed = (List<Object[]>) query.list();
			logger.debug("userList => " + wallFeed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wallFeed;
	}

}
