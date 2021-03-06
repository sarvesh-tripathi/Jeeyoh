package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Dealredemptionlocation;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Userdealssuggestion;

public interface IDealsDAO {
	public List<Deals> getDeals();
	public List<Deals> getDealsByLocation(String location);
	public List<Deals> getDealsByZipCode(String zipCode);
	public List<Deals> getDealsByCategory(String category);
	public List<Deals> getDealsByCategory(String category, String location, boolean isZipCode);
	public void saveDeal(Deals deals);
	public void saveDeal(Deals deals, int batch_size);
	public void saveDeal(List<Deals> deals, int batch_size);
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
	public List<Deals> getDealsByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating, int minPrice, int maxPrice);
	public List<Deals> getDealsBySearchKeyword(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating, int minPrice, int maxPrice, boolean forExactMatch);
	public List<Userdealssuggestion> getUserDealSuggestions(String userEmail, int offset, int limit, String category, String suggestionType, double lat, double lon, int distance, double rating, int minPrice, int maxPrice);
	public List<Object[]> getTopDealsByRating(String idsStr);
	public List<Object[]> getDealLikeCountByPage(String idsStr);
	public Deals getDealById(int dealId);
	public int isDealExists(String dealId);
	public int getTotalDealsBySearchKeyWord(String searchText,String category, String location, double lat, double lon, int distance, double rating, int minPrice, int maxPrice);
	public List<Deals> getDealsByUserCategory(String itemCategory,
			String itemType, String providerName);
	public boolean saveDealUserLikes(Dealsusage dealsusage);
	public boolean updateDealUserLikes(Dealsusage dealsusage);
	public Dealoption getDealOptionByDealId(int dealId, int minPrice, int maxPrice);
	public Dealredemptionlocation getRedemptionLocationByDealOption(int dealOptionId);
	public List<Deals> getBookedDealList(int userId, String category);
	public List<Deals> getDealsByuserLikesForCurrentWeekend(String itemCategory,
			String itemType,String providerName, double latitude, double longitude);
	public void updateDeal(Deals deals);
	
}
