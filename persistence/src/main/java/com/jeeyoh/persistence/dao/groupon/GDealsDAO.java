package com.jeeyoh.persistence.dao.groupon;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;
import com.jeeyoh.persistence.domain.Gdivision;
@Repository("gDealsDAO")
public class GDealsDAO implements IGDealsDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addDeals(Gdeal deal) {
		sessionFactory.getCurrentSession().saveOrUpdate(deal);	

	}

	@Override
	public Gdeal getDealById(String dealId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getDeals() {

		List<Gdeal> gDealList = null;
		//HibernateTemplate template = getHibernateTemplate();

		//String query = "from Gdeal";
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Gdealoption.class).setProjection(Projections.projectionList()
				.add( Projections.property("gdeal"))
				.add( Projections.property("gdeal.gmerchant")));*/
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Gdealoption.class);

		criteria.createAlias("gdeal", "gdeal");
		criteria.createAlias("gdeal.gmerchant", "gmerchant");
		criteria.add(Restrictions.gt("discountPercent", 20));

		//gDealList= criteria.list();
		List<Object> rows = criteria.list();

		/*String hqlQuery = "from Gdeal gDeal,Gdealoption gdealOption where gDeal.id=gdealOption.gdeal.id and gdealOption.discountPercent > 20";
			try {
				Query query = sessionFactory.getCurrentSession().createQuery(
						hqlQuery);
			//gDealList = template.find("select * from Gdeal gDeal,GDealOption gdealOption where gDeal.id=gdealOption.gdeal.id and gdealOption.discountPercent > 20");
				gDealList = (List<Gdeal>) query.list();

			} catch (Exception e) {
			e.printStackTrace();
		}*/
		return rows;
	}

	@Override
	public List<Gdeal> getDealsByDivision(String divisionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Gdeal> getDealsByEndDate() {
		List<Gdeal> gDealList = null;
		//HibernateTemplate template = getHibernateTemplate();

		//String query = "from Gdeal";


		String hqlQuery = "from Gdeal a where a.endAt > :endAt";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			//gDealList = template.find("select * from Gdeal gDeal,GDealOption gdealOption where gDeal.id=gdealOption.gdeal.id and gdealOption.discountPercent > 20");
			
			Calendar calendar = Calendar.getInstance();
			
			java.util.Date now = (java.util.Date) calendar.getTime();
			
			query.setParameter("endAt", now);
			gDealList = (List<Gdeal>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return gDealList;
	}

}
