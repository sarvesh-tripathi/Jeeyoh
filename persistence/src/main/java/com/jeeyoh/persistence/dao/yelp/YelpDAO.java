package com.jeeyoh.persistence.dao.yelp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Gdivision;
import com.jeeyoh.persistence.domain.Ybusiness;
import com.jeeyoh.persistence.domain.Ycategoryfilter;
import com.jeeyoh.persistence.domain.Ydeal;
import com.jeeyoh.persistence.domain.Yreview;

@Repository("yelpDAO")
public class YelpDAO implements IYelpDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
     
	
	@Override
	public Ycategoryfilter getCategories(String category) {
		// TODO Auto-generated method stub
		Ycategoryfilter ycategory = null;
		if(category == null)
		{
			category = "No Category";
		}
		
		String hqlQuery = "from Ycategoryfilter a where a.category = :category";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", category);
			ycategory = (Ycategoryfilter) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
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
	public void saveBusiness(Ybusiness business) {
		// TODO Auto-generated method stub
		//sessionFactory.getCurrentSession().saveOrUpdate(business);	
		sessionFactory.getCurrentSession().save(business);	
		//sessionFactory.getCurrentSession().
	}

	@Override
	public List<Ybusiness> getBusiness() {

		List<Ybusiness> businessList = null;
		String hqlQuery = "from Ybusiness";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			
			businessList = (List<Ybusiness>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return businessList;
	}

	@Override
	public void saveDeals(Ydeal ydeal) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(ydeal);
		
	}
	@Override
	public void saveReviews(Yreview yreview) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(yreview);
		
	}

	@Override
	public List<Ydeal> filterdDealByDiscount() {
		// TODO Auto-generated method stub
		List<Ydeal> dealList = null;
		String hqlQuery = "from Ydeal y , Ydealoption o where y.id = o.ydeal.id and ((o.price/o.originalPrice)*100)>20 and o.remainingCount>0";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			
			dealList = (List<Ydeal>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealList;
		
	}

}
