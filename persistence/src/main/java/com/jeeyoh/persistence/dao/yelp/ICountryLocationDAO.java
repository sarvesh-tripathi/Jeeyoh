package com.jeeyoh.persistence.dao.yelp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Countrylocation;
import com.jeeyoh.persistence.domain.Gdivision;


public interface ICountryLocationDAO {
	public List<Countrylocation> getCountryLocations(String country);
}
