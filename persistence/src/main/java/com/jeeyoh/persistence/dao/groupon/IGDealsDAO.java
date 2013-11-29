package com.jeeyoh.persistence.dao.groupon;

import java.util.List;

import com.jeeyoh.persistence.domain.Gdeal;

public interface IGDealsDAO {
	public void addDeals(Gdeal deal);
	public List<Object> getDeals();
	public Gdeal getDealById(String dealId);
	public List<Gdeal> getDealsByDivision(String divisionId);
	public List<Gdeal> getDealsByEndDate();

}
