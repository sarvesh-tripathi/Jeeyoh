package com.jeeyoh.persistence.dao.livingsocial;

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

import com.jeeyoh.persistence.domain.Ldeal;
import com.jeeyoh.persistence.domain.LdealCategory;
import com.jeeyoh.persistence.domain.LdealOption;
import com.jeeyoh.utils.Utils;

@Repository("livingSocialDAO")
public class LivingSocialDAO implements ILivingSocialDAO{
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void addLdeals(Ldeal ldeal, int batch_size) {
		logger.debug("LivingSocialDAO addDeals ====>");
		Session session =  sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(ldeal);	
			tx.commit();
			if( batch_size % 20 == 0 ) {
				session.flush();
				session.clear();
			}
			
		}
		catch (HibernateException e) {
			//if (tx!=null) tx.rollback();
			logger.error("ERROR::: == "+e);
			
			}
		finally
		{
			session.close();
			logger.debug("close session ::: ");
		}
	}

	@Override
	public void addLdealOptions(LdealOption ldealOption) {
		logger.debug("LivingSocialDAO addLdealOptions ====>");
		sessionFactory.getCurrentSession().saveOrUpdate(ldealOption);
		
	}

	@Override
	public void addLdealCategory(LdealCategory ldealCategory) {
		logger.debug("LivingSocialDAO addLdealCategory ====>");
		sessionFactory.getCurrentSession().saveOrUpdate(ldealCategory);
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public Ldeal getLdeal(int dealId) {
		logger.debug("LivingSocialDAO getLdeal ====>");
		List<Ldeal> ldealList=null;
		String hqlQuery = "from Ldeal a where a.dealId=:dealId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("dealId", dealId);
			ldealList = (List<Ldeal>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ldealList!=null?ldealList.get(0):null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LdealOption> getDeals() {

		List<LdealOption> gDealList = null;
		logger.debug("loadDeals => getDeals... ");
		String h = "select b from Ldeal a, LdealOption b, LdealCategory c, Lcategory d where a.endAt >= :currentDate and d.category = c.categoryName and b.discount > 20 and c.ldeal.id = a.id and b.ldeal.id = a.id";
		
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					h);
			query.setParameter("currentDate", Utils.getCurrentDate());
			logger.debug("loadDeals => query.list() size " + query.list().size());
			gDealList = (List<LdealOption>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return gDealList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ldeal> getLDeals() {
		List<Ldeal> gDealList = null;
		logger.debug("loadDeals => getDeals... ");
		String h = "select distinct a from Ldeal a, LdealCategory b,Lcategory c where a.endAt >= :currentDate and c.category = b.categoryName and b.ldeal.id = a.id";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					h);
			query.setParameter("currentDate", Utils.getCurrentDate());
			logger.debug("loadDeals => query.list() size " + query.list().size());
			gDealList = (List<Ldeal>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return gDealList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LdealOption> getDealOptions(int dealId) {
		List<LdealOption> gDealOptionsList = null;
		logger.debug("loadDeals => getDeals... ");
		String h = "select a from LdealOption a where b.ldeal.id = :dealId and a.discount > 20";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					h);
			query.setParameter("dealId", dealId);
			logger.debug("loadDeals => query.list() size " + query.list().size());
			gDealOptionsList = (List<LdealOption>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return gDealOptionsList;
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public Lcategory getLCategory(String categoryName) {
		logger.debug("getLCategoryId ====>"+categoryName);
		List<Lcategory> lCategoryList = null;
		String hqlQuery = "select a from Lcategory a where a.category like '%"+categoryName+"%'";
		try{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			logger.debug("getLCategoryId => query.list() size " + query.list().size());
			lCategoryList = (List<Lcategory>)query.list();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return lCategoryList!=null?lCategoryList.get(0):null;
	}*/
}
