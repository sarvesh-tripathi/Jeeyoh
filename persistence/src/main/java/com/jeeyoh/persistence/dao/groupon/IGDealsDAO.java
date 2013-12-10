package com.jeeyoh.persistence.dao.groupon;

import java.util.List;

import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;

public interface IGDealsDAO {
	public void addDeals(Gdeal deal);
	public List<Gdealoption> getDeals();
	public Gdeal getDealById(String dealId);

}
