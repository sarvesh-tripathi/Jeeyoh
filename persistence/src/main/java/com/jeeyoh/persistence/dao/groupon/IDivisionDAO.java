package com.jeeyoh.persistence.dao.groupon;

import java.util.List;

import com.jeeyoh.persistence.domain.Gdivision;

public interface IDivisionDAO {
	public void addDivision(Gdivision division);
	
	public List<Gdivision> getDivisions();
	
	public List<Gdivision> getDivisionsByCountry(String country);
}
