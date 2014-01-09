package com.jeeyoh.persistence.dao.groupon;

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

import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;
@Repository("gDealsDAO")
public class GDealsDAO implements IGDealsDAO {

	private static Logger logger = LoggerFactory.getLogger("debugLogger");
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addDeals(Gdeal deal,int count) {
		
		sessionFactory.getCurrentSession().save(deal);
		if( count % 20 == 0 ) {
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
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
		String h = "select b from Gdeal a, Gdealoption b, Gtags c, Gcategory d where d.category = c.name and b.discountPercent > 20 and c.gdeal.id = a.id and b.gdeal.id = a.id";
			try {
				Query query = sessionFactory.getCurrentSession().createQuery(
						h);
			//gDealList = template.find("select * from Gdeal gDeal,GDealOption gdealOption where gDeal.id=gdealOption.gdeal.id and gdealOption.discountPercent > 20");
				logger.debug("loadDeals => query.list() size " + query.list().size());
				gDealList = (List<Gdealoption>) query.list();

			} catch (Exception e) {
			e.printStackTrace();
		}
		return gDealList;
	}

}
