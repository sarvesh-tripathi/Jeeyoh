package com.jeeyoh.persistence.dao.yelp;

import java.util.List;

import com.jeeyoh.persistence.domain.Countrylocation;


public interface ICountryLocationDAO {
	public List<Countrylocation> getCountryLocations(String country);
}
