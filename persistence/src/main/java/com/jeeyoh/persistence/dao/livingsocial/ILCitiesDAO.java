package com.jeeyoh.persistence.dao.livingsocial;

import java.util.List;

import com.jeeyoh.persistence.domain.LCities;

public interface ILCitiesDAO {
	public List<LCities> getLCities();

	public void addCities(LCities lCities);

	public LCities getCityByName(String cityName);
	
}
