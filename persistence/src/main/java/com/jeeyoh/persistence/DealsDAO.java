package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Deals;

@Repository("dealsDAO")
public class DealsDAO implements IDealsDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Deals> getDeals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deals> getDealsByCategory(String category) {
		// TODO Auto-generated method stub
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
}
