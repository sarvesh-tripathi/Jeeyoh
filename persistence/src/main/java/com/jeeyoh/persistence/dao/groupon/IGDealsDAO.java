package com.jeeyoh.persistence.dao.groupon;

import java.util.List;

import com.jeeyoh.persistence.domain.Gdeal;
import com.jeeyoh.persistence.domain.Gdealoption;
import com.jeeyoh.persistence.domain.Goptiondetail;

public interface IGDealsDAO {
	public void addDeals(Gdeal deal, int count);
	public List<Gdealoption> getDeals();
	public Gdeal getDealById(String dealId);
	public List<Gdeal> getGDeals();
	public List<Gdealoption> getDealOptions(int dealId);
	public Goptiondetail getOptionDeatils(int optonId);
	

}
