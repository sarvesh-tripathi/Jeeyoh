package com.jeeyoh.persistence.dao.groupon;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;
@Repository("gDealsDAO")
public class GDealsDAO implements IGDealsDAO {

	private static Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addDeals(Gdeal deal) {
		Session session =  sessionFactory.getCurrentSession();
		try
		{
			session.save(deal);
			if(batch_size % 20 == 0)
			{
				session.flush();
				session.clear();
			}
		}
		catch (HibernateException e) {
			e.printStackTrace(); 
		}
	}

	@Override
	public Gdeal getDealById(String dealId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Gdealoption> getDeals() {

		List<Gdealoption> gDealList = null;
		logger.debug("loadDeals => getDeals... ");

		//HibernateTemplate template = getHibernateTemplate();

		//String query = "from Gdeal";
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Gdealoption.class).setProjection(Projections.projectionList()
				.add( Projections.property("gdeal"))
				.add( Projections.property("gdeal.gmerchant")));*/
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Gdealoption.class);

		criteria.createAlias("gdeal", "gdeal");
		criteria.createAlias("gdeal.gmerchant", "gmerchant");
		criteria.add(Restrictions.gt("discountPercent", 20));*/

		//gDealList= criteria.list();
		//List<Object> rows = criteria.list();

		//String hqlQuery = "from Gdeal gDeal,Gdealoption gdealOption where gDeal.id=gdealOption.gdeal.id and gdealOption.discountPercent > 20 and gDeal.";

		String h = "select b from Gdeal a, Gdealoption b, Gtags c, Gcategory d where d.category = c.name and b.discountPercent > 20 and c.gdeal.id = a.id and b.gdeal.id = a.id";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					h);
			logger.debug("loadDeals => query.list() size " + query.list().size());
			gDealList = (List<Gdealoption>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return gDealList;
	}

}
