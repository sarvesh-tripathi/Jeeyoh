package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;
import com.jeeyoh.persistence.domain.Ydeal;

public interface IDealsDAO {
	public List<Deals> getDeals();
	public List<Deals> getDealsByLocation(String location);
	public List<Deals> getDealsByZipCode(String zipCode);
	public List<Deals> getDealsByCategory(String category);
	public List<Deals> getDealsByCategory(String category, String location, boolean isZipCode);
	public void saveDeal(Deals deals);
	public void saveDeal(Deals deals, int batch_size);
	public void saveFilterdDeal(Deals deal);
	public List<Deals> getDealsByBusinessId(String businessId);
	public void saveSuggestions(Userdealssuggestion dealSuggestion);
	public void saveNonDealSuggestion(Usernondealsuggestion nondeal);
	public int getDealsLikeCounts(Integer id);
	public List<Deals> getDealsByKeywords(String keyword, String category,
			String location);
	List<Userdealssuggestion> getDealsSuggestion(int emailId);
	public List<Deals> getUserDeals(Integer userId);
	public List<Deals> getDealsByBusinessId(Integer id);
	public List<Deals> getDealsByUserCategory(String itemCategory,
			String itemType);
	
}
