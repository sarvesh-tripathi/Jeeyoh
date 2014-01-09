package com.jeeyoh.persistence.dao.yelp;

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

import com.jeeyoh.persistence.domain.Ybusiness;
import com.jeeyoh.persistence.domain.Ycategoryfilter;
import com.jeeyoh.persistence.domain.Ydeal;
import com.jeeyoh.persistence.domain.Ydealoption;
import com.jeeyoh.persistence.domain.Yreview;

@Repository("yelpDAO")
public class YelpDAO implements IYelpDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
     
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Override
	public Ycategoryfilter getCategories(String category) {
		// TODO Auto-generated method stub
		Ycategoryfilter ycategory = null;
		Session session = sessionFactory.openSession();
		if(category == null)
		{
			category = "No Category";
		}
		
		String hqlQuery = "from Ycategoryfilter a where a.category = :category";
		try {
			/*Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);*/
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("category", category);
			ycategory = (Ycategoryfilter) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
		if(ycategory == null)
		{
			ycategory = noCategoryObject("No Category");
		}
		
		return ycategory;

	}
	
	private Ycategoryfilter noCategoryObject(String category)
	{
		Ycategoryfilter ycategory = null;
		String hqlQuery = "from Ycategoryfilter a where a.category = :category";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", category);
			ycategory = (Ycategoryfilter) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ycategory;
	}

	@Override
	public void saveBusiness(Ybusiness business, int count) {
		// TODO Auto-generated method stub
		logger.debug("Save business ::: ");
		//Session session = sessionFactory.openSession();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(business);	
			tx.commit();
			if( count % 20 == 0 ) {
				session.flush();
				session.clear();
			}
			
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			logger.error("ERROR::: == "+e);
			}
		finally
		{
			//session.close();
			logger.debug("close session ::: ");
		}
	}

	@Override
	public List<Ybusiness> getBusiness() {

		List<Ybusiness> businessList = null;
		/*Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();*/
		String hqlQuery = "from Ybusiness";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			//Query query = session.createQuery(hqlQuery);	
			businessList = (List<Ybusiness>) query.list();
			/*tx.commit();
			session.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessList;
	}

	@Override
	public void saveDeals(Ydeal ydeal, int count) {
		// TODO Auto-generated method stub
		//Session session = sessionFactory.openSession();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(ydeal);	
			tx.commit();
			if( count % 20 == 0 ) {
				session.flush();
				session.clear();
			}
			
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			logger.error("ERROR::: == "+e);
			}
		finally
		{
			//session.close();
			logger.debug("close session ::: ");
		}
		
	}
	@Override
	public void saveReviews(Yreview yreview, int count) {
		// TODO Auto-generated method stub
		//Session session = sessionFactory.openSession();
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(yreview);	
			tx.commit();
			if( count % 20 == 0 ) {
				session.flush();
				session.clear();
			}
			
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			logger.error("ERROR::: == "+e);
			}
		finally
		{
			//session.close();
			logger.debug("close session ::: ");
		}
	}

	@Override
	public List<Ydealoption> filterdDealByDiscount() {
		// TODO Auto-generated method stub
		List<Ydealoption> dealList = null;
		//Session session = sessionFactory.openSession();
	     //Transaction tx = session.beginTransaction();
		String hqlQuery = "select o from Ydeal y ,Ydealoption o where y.id = o.ydeal.id and ((o.price/o.originalPrice)*100)>20 and o.remainingCount>0";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			
			dealList = (List<Ydealoption>) query.list();
			//tx.commit();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealList;
		
	}

	@Override
	public List<Ybusiness> getFilterBusiness() {
		// TODO Auto-generated method stub
		//List<Ybusiness> ybusinessList = null;
		String[] categoryFilters = {"Restaurants","Spa","Sport"};
		//select * from ybusiness where id in (select businessId from ybusinesscategorymap where categoryFilterId in (select id from ycategoryfilter where category in ('Restaurants','Sports')));
		/*String hqlQuery = "from Ybusiness where id in (select businessId from Ybusinesscategorymap where Ybusinesscategorymap.ycategoryfilter.categoryFilterId in (select id from Ycategoryfilter where category in ('Restaurants','Sports')))";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			
			ybusinessList = (List<Ybusiness>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ybusinessList;*/
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ybusiness.class);

		criteria.createAlias("ybusinesscategorymaps", "categoryMap");
		criteria.createAlias("categoryMap.ycategoryfilter", "categoryFilter");
		criteria.add(Restrictions.in("categoryFilter.category", categoryFilters));

		//gDealList= criteria.list();
		List<Ybusiness> ybusinessList = criteria.list();
		return ybusinessList;
	}

}
