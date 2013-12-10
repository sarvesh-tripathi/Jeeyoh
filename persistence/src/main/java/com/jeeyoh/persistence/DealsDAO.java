package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Ydeal;

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
	public void saveDeal(Deals deals) 
	{
		sessionFactory.getCurrentSession().saveOrUpdate(deals);	
	}

	@Override
	public void saveFilterdDeal(Deals deal) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(deal);
		/*Session session  = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
			try {
				session.save(deal);
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	
	

}
