package com.jeeyoh.persistence.dao.yelp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Countrylocation;
import com.jeeyoh.persistence.domain.Gdivision;

@Repository("countryLocationDAO")
public class CountryLocationDAO implements ICountryLocationDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Countrylocation> getCountryLocations(String countryCode) {
		List<Countrylocation> locationList = null;
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

}
