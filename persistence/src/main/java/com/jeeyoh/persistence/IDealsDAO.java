package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.Deals;

public interface IDealsDAO {
	public List<Deals> getDeals();
	public List<Deals> getDealsByLocation(String location);
	public List<Deals> getDealsByZipCode(String zipCode);
	public List<Deals> getDealsByCategory(String category);
	public List<Deals> getDealsByCategory(String category, String location, boolean isZipCode);
	public void saveDeal(Deals deals);
}
