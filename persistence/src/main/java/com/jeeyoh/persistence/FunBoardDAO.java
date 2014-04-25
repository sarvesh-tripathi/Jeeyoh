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
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	query.setParameter("funboardCreationTime",funboardCreationTime);
	    	timelines = query.list();
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    	e.printStackTrace();
	    }
	    
	    return timelines != null && !timelines.isEmpty() ? timelines.get(0) : null;
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
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Timeline getDefaultTimeLine() {
		logger.debug("getTimeLine::");
		List<Timeline>  timelines = null;
	    String hqlQuery = "from Timeline where timeLineId = 6";
	    try{
	    	Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
	    	timelines = query.list();
	    }
	    catch(HibernateException e)
	    {
	    	logger.error(e.toString());
	    	e.printStackTrace();
	    }
	    
	    return timelines != null && !timelines.isEmpty() ? timelines.get(0) : null;
	}

	@Override
	public void updateFunBoard(Funboard funboard) {
		try
		{
			sessionFactory.getCurrentSession().update(funboard);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
	}

}
