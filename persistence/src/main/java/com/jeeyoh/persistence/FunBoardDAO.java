package com.jeeyoh.persistence;

import java.sql.Time;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Funboard;
import com.jeeyoh.persistence.domain.FunboardComments;
import com.jeeyoh.persistence.domain.FunboardMediaContent;
import com.jeeyoh.persistence.domain.Timeline;
import com.jeeyoh.persistence.domain.WallFeedSharing;
import com.jeeyoh.utils.Utils;

@Repository("funBoardDAO")
public class FunBoardDAO implements IFunBoardDAO{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveFunBoard(Funboard funboard, int batch_size) {
		logger.debug("saveFunBoard => ");
		Session session =  sessionFactory.getCurrentSession();
		try
		{
			session.save(funboard);
			if(batch_size % 20 == 0)
			{
				//session.flush();
				//session.clear();
			}
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.error("ERROR IN DAO :: = > "+e);
		}
	}

	@Override
	public void deleteFunBoard(int id, int userId) {
		String queryString = "delete from Funboard where funBoardId =:id and user.userId =:userId";
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

	@Override
	public void saveFunBoardComment(FunboardComments funboardComments) {
		sessionFactory.getCurrentSession().save(funboardComments);
	}

	@Override
	public void saveFunBoardMediaContent(
			FunboardMediaContent funboardMediaContent) {
		logger.debug("saveFunBoardMediaContent..");
		sessionFactory.getCurrentSession().save(funboardMediaContent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Funboard> getUserFunBoardItems(int userId) {
		List<Funboard>  funBoardList = null;
	    String hqlQuery = "from Funboard where user.userId =:userId order by createdTime asc";
	    
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	query.setParameter("userId",userId);
	    	funBoardList = query.list();
	    	logger.debug("funBoardList:  "+funBoardList);
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    }
		return funBoardList;
	}

	
	@Override
	public int getNotificationCount(int id) {
		int rowCount = 0;
		String hqlQuery = "select count(a.id) from FunboardComments a where a.funboard.funBoardId = :funBoardId and a.isComment is false";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("funBoardId", id);

			rowCount = ((Number)query.uniqueResult()).intValue();

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FunboardComments> getComments(int funBoardId) {
		List<FunboardComments>  comments = null;
	    String hqlQuery = "from FunboardComments where funboard.funBoardId = :funBoardId";
	    
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	query.setParameter("funBoardId",funBoardId);
	    	comments = query.list();
	    	logger.debug("comments::  "+comments);
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    }
		return comments;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<FunboardMediaContent> getmediaContent(int funBoardId) {
		List<FunboardMediaContent>  mediaContent = null;
	    String hqlQuery = "from FunboardMediaContent where funboard.funBoardId = :funBoardId";
	    
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	query.setParameter("funBoardId",funBoardId);
	    	mediaContent = query.list();
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    }
		return mediaContent;
	}

	@Override
	public void deleteFunBoardMediaContent(int funBoardId) {
		String queryString = "delete from FunboardMediaContent where funboard.funBoardId = :funBoardId";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(queryString);
			query.setParameter("funBoardId",funBoardId);
		    query.executeUpdate();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteFunBoardComments(int funBoardId) {
		String queryString = "delete from FunboardComments where funboard.funBoardId = :funBoardId";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(queryString);
			query.setParameter("funBoardId",funBoardId);
		    query.executeUpdate();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Timeline getTimeLine(Time funboardCreationTime) {
		logger.debug("getTimeLine::");
		List<Timeline>  timelines = null;
	    String hqlQuery = "from Timeline where startTime <= :funboardCreationTime and endTime >= :funboardCreationTime";
	    Timeline foundTimeline = null;
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	query.setParameter("funboardCreationTime",funboardCreationTime);
	    	timelines = query.list();
	    	/*if(timelines != null) {
	    		for(Timeline timeline : timelines) {
	    			logger.debug("time start: "+timeline.getStartTime().before(funboardCreationTime) +" after: "+timeline.getEndTime().after(funboardCreationTime));
	    			if(timeline.getStartTime().before(funboardCreationTime) && timeline.getEndTime().after(funboardCreationTime))
	    			{
	    				foundTimeline = timeline;
	    				break;
	    			}
	    		}
	    	}*/
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    	e.printStackTrace();
	    }
	    
	    /*if(foundTimeline != null) {
	    	return foundTimeline;
	    } else {*/
	    	return timelines != null && !timelines.isEmpty() ? timelines.get(0) : null;
	  //  }
	}

	@SuppressWarnings("unchecked")
	@Override
	public Funboard isFunBoardExists(int userId, int itemId) {
		List<Funboard>  funBoardList = null;
	    String hqlQuery = "from Funboard where user.userId =:userId and itemId = :itemId";
	    
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	query.setParameter("userId",userId);
	    	query.setParameter("itemId",itemId);
	    	funBoardList = query.list();
	    	logger.debug("funBoardList:  "+funBoardList);
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    }
		return funBoardList != null && !funBoardList.isEmpty() ? funBoardList.get(0) : null;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Funboard> getUserFunBoardItemsByCurrentsDate(int userId) {
		List<Funboard>  funBoardList = null;
	    String hqlQuery = "from Funboard where user.userId =:userId and endDate >= :currentDate";
	    
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	query.setParameter("userId",userId);
	    	query.setParameter("currentDate", Utils.getCurrentDate());
	    	funBoardList = query.list();
	    	logger.debug("funBoardList:  "+funBoardList);
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    }
		return funBoardList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Funboard getFunboardById(int funBoardId) {
		List<Funboard>  funBoardList = null;
	    String hqlQuery = "from Funboard where funBoardId =:funBoardId";
	    
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	query.setParameter("funBoardId",funBoardId);
	    	funBoardList = query.list();
	    	logger.debug("funBoardList:  "+funBoardList);
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    }
		return funBoardList != null && !funBoardList.isEmpty() ? funBoardList.get(0) : null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WallFeedSharing> getWallFeedSharing() {
		// TODO Auto-generated method stub
		
		List<WallFeedSharing> list = null;
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
	public List<WallFeedSharing> getDistinctWallFeedSharing() {
		List<WallFeedSharing> list = null;
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
	public void updateWallFeedSharing(WallFeedSharing feedSharing) {
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
	public void deleteWallFeedSharing(WallFeedSharing feedSharing) {
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
	public int getFunboardComment(Integer funBoardId, Integer userId) {
		logger.debug("funboard :: "+funBoardId+"userId :::"+userId);
		int rowCount = 0;
		String hqlquery = "select count(a.id) from FunboardComments a where a.funboard.funBoardId =:funBoardId and a.user.userId =:userId";
		try{
			//Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("funBoardId", funBoardId);
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
	
	@Override
	public double getAVGItemRank(String packageName) {
		double rowCount = 0;
		String hqlquery = "select avg(itemRank) from WallFeedSharing where packageName=:packageName";
		try{
			//Query query = sessionFactory.getCurrentSession().createSQLQuery(hqlquery);
			Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
			query.setParameter("packageName", packageName);
					
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

}
