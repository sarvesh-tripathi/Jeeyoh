package com.jeeyoh.persistence.dao.yelp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Countrylocation;

@Repository("countryLocationDAO")
public class CountryLocationDAO implements ICountryLocationDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Countrylocation> getCountryLocations(String countryCode) {
		List<Countrylocation> locationList = null;
		//Session session = sessionFactory.openSession();
		String hqlQuery = "from Countrylocation a where a.countryCode = :countryCode ";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
		
			query.setParameter("countryCode", countryCode);
			locationList = (List<Countrylocation>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Countrylocation getStateNameByStateCode(String stateCode) {
		List<Countrylocation> locationList = null;
		String hqlQuery = "from Countrylocation where stateCode = :stateCode GROUP BY(state)";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("stateCode", stateCode);
			locationList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locationList != null && !locationList.isEmpty() ? locationList.get(0) : null;
	}

}
