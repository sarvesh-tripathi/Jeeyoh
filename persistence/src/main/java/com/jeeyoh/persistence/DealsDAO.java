package com.jeeyoh.persistence;

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

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;

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
}
