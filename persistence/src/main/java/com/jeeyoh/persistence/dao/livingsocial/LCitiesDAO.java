package com.jeeyoh.persistence.dao.livingsocial;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Gdivision;
import com.jeeyoh.persistence.domain.LCities;

@Repository("lCitiesDAO")
public class LCitiesDAO implements ILCitiesDAO{
	
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	@Override
	public List<LCities> getLCities() {
		List<LCities> lCitiesList = null;
		String query = "from LCities";
		try {
			lCitiesList = (List<LCities>) sessionFactory.getCurrentSession()
					.createQuery(query).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lCitiesList;
	}
	@Override
	public void addCities(LCities lCities) {
		sessionFactory.getCurrentSession().saveOrUpdate(lCities);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LCities getCityByName(String cityName)
	{
		logger.debug("getCityByName ===>"+cityName);
		List<LCities> lCitiesList = null;
		String hqlQuery = "from LCities a where a.cityName=:cityName";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("cityName", cityName);
			lCitiesList = (List<LCities>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lCitiesList!=null && !lCitiesList.isEmpty() ? lCitiesList.get(0) : null;
	}
}

