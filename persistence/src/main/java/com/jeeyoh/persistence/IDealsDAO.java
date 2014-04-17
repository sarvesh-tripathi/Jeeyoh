package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Userdealssuggestion;

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
	public int getDealsLikeCounts(Integer id);
	public List<Deals> getDealsByKeywords(String keyword, String category,
			String location);
	List<Userdealssuggestion> getDealsSuggestion(int emailId);
	public List<Deals> getUserDeals(Integer userId);
	public List<Deals> getDealsByBusinessId(Integer id);
	public List<Deals> getDealsByUserCategory(String itemCategory,
			String itemType, String providerName, double latitude, double longitude);
	public List<Deals> getDealsByUserEmail(String userEmail);
	public List<Userdealssuggestion>userDealsSuggestedByJeeyoh(String keyword, String category,
			String location, String emailId);
	public int userCategoryLikeCount(Integer userCategoryId);
	public List<Deals> getDealsByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit);
	public List<Deals> getDealsBySearchKeyword(String searchText,String category, String location, int offset, int limit);
	public List<Userdealssuggestion> getUserDealSuggestions(String userEmail, int offset, int limit);
	public List<Object[]> getTopDealsByRating(String idsStr);
	public List<Object[]> getDealLikeCountByPage(String idsStr);
	public Deals getDealById(int dealId);
	public Deals isDealExists(String dealId);
	public int getTotalDealsBySearchKeyWord(String searchText,String category, String location);
	public List<Deals> getDealsByUserCategory(String itemCategory,
			String itemType, String providerName);
	
}
