package com.jeeyoh.persistence;

import java.awt.print.Book;
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

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.persistence.domain.Ybusiness;

@Repository("dealsDAO")
public class DealsDAO implements IDealsDAO {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Deals> getDeals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deals> getDealsByCategory(String category) {
		List<Business> businessList = null;
		String hqlQuery = "from Deals a where a.businessTypeId = :category";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("businessTypeId", category);
			businessList = (List<Business>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	@Override
	public List<Deals> getDealsByCategory(String category, String location,
			boolean isZipCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deals> getDealsByLocation(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deals> getDealsByZipCode(String zipCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveDeal(Deals deals, int batch_size) 
	{
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.saveOrUpdate(deals);	
			
			if( batch_size % 50 == 0 ) {
				session.flush();
				session.clear();
			}
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}
		finally
		{
			session.close();
		}
	}
	
	 @Override
        public void saveDeal(Deals deals)
        {
                sessionFactory.getCurrentSession().saveOrUpdate(deals);        
        }

        @Override
        public void saveFilterdDeal(Deals deal) {
            
                //sessionFactory.getCurrentSession().saveOrUpdate(deal);
        	Session session = sessionFactory.openSession();
    		Transaction tx = null;
    		try
    		{
    			tx = session.beginTransaction();
    			session.save(deal);
    			tx.commit();
    		}
    		catch (HibernateException e) {
    			if (tx!=null) tx.rollback();
    			e.printStackTrace(); 
    		}
    		finally
    		{
    			session.close();
    		}
        }

		@Override
		public List<Deals> getDealsByBusinessId(String businessId) {
			// TODO Auto-generated method stubbusinessId
			List<Deals> dealsList = null;
			String hqlQuery = "from Deals a where a.businessId = :businessId";
			try {
				Query query = sessionFactory.getCurrentSession().createQuery(
						hqlQuery);
				query.setParameter("businessId", businessId);
				dealsList = (List<Deals>) query.list();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dealsList;
		}

		@Override
		public void saveSuggestions(Userdealssuggestion dealSuggestion) {
			// TODO Auto-generated method stub
			
			Session session = sessionFactory.openSession();
    		Transaction tx = null;
    		try
    		{
    			tx = session.beginTransaction();
    			session.save(dealSuggestion);
    			tx.commit();
    		}
    		catch (HibernateException e) {
    			if (tx!=null) tx.rollback();
    			e.printStackTrace(); 
    		}
    		finally
    		{
    			session.close();
    		}
			
		}

		@Override
		public void saveNonDealSuggestion(Usernondealsuggestion nondeal) {
			// TODO Auto-generated method stub
			Session session = sessionFactory.openSession();
    		Transaction tx = null;
    		try
    		{
    			tx = session.beginTransaction();
    			session.save(nondeal);
    			tx.commit();
    		}
    		catch (HibernateException e) {
    			if (tx!=null) tx.rollback();
    			//e.printStackTrace(); 
    			logger.error("EXCEPTION IN CATCH  "+e);
    		}
    		finally
    		{
    			session.close();
    		}
			
		}

		@Override
		public int getDealsLikeCounts(Integer id) {
			// TODO Auto-generated method stub
			int rowCount = 0;
			String hqlQuery = "from count(*) from Dealsusage a where a.deals.id = :id";
			try {
				Query query = sessionFactory.getCurrentSession().createQuery(
						hqlQuery);
				query.setParameter("id", id);
				rowCount = ((Number)query.uniqueResult()).intValue();						
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rowCount;
		}

		@SuppressWarnings("null")
		@Override
		public List<Deals> getDealsByKeywords(String keyword, String category,
				String location) {
			logger.error("getDealsByKeywords ==== > "+location);
			logger.error("getDealsByKeywords ==== > "+location.isEmpty());
			// TODO Auto-generated method stub
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class);
			criteria.createAlias("business", "business");
			criteria.createAlias("business.businesstype", "businesstype");
			//criteria.createAlias("tags", "tags");
			if(category != null && category.isEmpty() == false)
			{
				logger.debug("IN CATEGORY CHECKING ::: ");
				criteria.add(Restrictions.eq("businesstype.businessType", category));
			}
			if(location != null && location.isEmpty()== false)
			{
				logger.debug("IN LOCATION CHECKING ::: ");
				criteria.add(Restrictions.eq("business.postalCode", location.trim()));
			}
			if(keyword != null && keyword.isEmpty() == false)
			{
				logger.debug("IN KEYWORD CHECKING ::: ");
				criteria.add(Restrictions.like("title", "%" + keyword.trim() + "%"));
				criteria.add(Restrictions.like("dealUrl", "%" + keyword.trim() + "%"));
				criteria.add(Restrictions.like("dealId", "%" + keyword.trim() + "%"));
				//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));
				
			}
			
			List<Deals> dealsList = criteria.list();
			return dealsList;
		}
		
		@Override
		public List<Userdealssuggestion> getDealsSuggestion(int id)
		{
			List<Userdealssuggestion> dealSuggestionList = null;
			String hqlQuery = "from Userdealssuggestion a where a.user.userId = :id";
			try
			{
				Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
				query.setParameter("id", id);
				dealSuggestionList = query.list();
			}
			catch(HibernateException e)
			{
				e.printStackTrace();
			}
			return dealSuggestionList;
			
		}

		@Override
		public List<Deals> getUserDeals(Integer userId) {
			// TODO Auto-generated method stub
			
			List<Deals> deals= null;
			String hqlQuery = "from Deals where id in(select d.deals.id from Dealsusage d where d.user.userId =:userId)";
			try
			{
				Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
				query.setParameter("userId", userId);
				deals = query.list();
			}
			catch(HibernateException e)
			{
				e.printStackTrace();
			}
			logger.debug("Value === "+deals);
			return deals;
		}

		@Override
		public List<Deals> getDealsByBusinessId(Integer id) {
			// TODO Auto-generated method stub
			List<Deals> dealsList = null;
			String hqlQuery = "from Deals a where a.business.id = :id";
			try {
				Query query = sessionFactory.getCurrentSession().createQuery(
						hqlQuery);
				query.setParameter("id", id);
				dealsList = (List<Deals>) query.list();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dealsList;
			
		}

		@Override
		public List<Deals> getDealsByUserCategory(String itemCategory,
				String itemType) {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class);
			criteria.createAlias("business", "business");
			criteria.createAlias("business.businesstype", "businesstype");
			//criteria.createAlias("tags", "tags");
			if(itemCategory != null)
			{
				criteria.add(Restrictions.eq("businesstype.businessType", itemCategory));
			}
			
			if(itemType != null)
			{
				criteria.add(Restrictions.like("title", "%" + itemType + "%"));
				criteria.add(Restrictions.like("dealUrl", "%" + itemType + "%"));
				criteria.add(Restrictions.like("dealId", "%" + itemType + "%"));
				//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));
				
			}
			
			List<Deals> dealsList = criteria.list();
			return dealsList;
			
		}
		
}
