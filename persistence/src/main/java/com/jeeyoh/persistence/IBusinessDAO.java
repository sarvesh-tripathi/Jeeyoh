package com.jeeyoh.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Gcategory;
import com.jeeyoh.persistence.domain.Lcategory;

@Repository("businessDAO")
public interface IBusinessDAO {
	public List<Business> getBusinesses();
	public List<Business> getBusinessByName(String name);
	public List<Business> getBusinessById(String businessId);
	public List<Business> getBusinessByIdForGroupon(String businessId);
	public Business getBusinessById(int id);
	public void saveBusiness(Business business, int count);
	public List<Businesstype> getBusinesstypeByTypeArray(String[] type);
	public Businesstype getBusinesstypeByType(String type);
	public void saveBusiness(Business business);
	public List<Business> getBusinessByCriteria(String userEmail, String searchText, String category, String location, String rating);
	public List<Business> getBusinessByuserLikes(String likekeyword, String itemCategory, String providerName, double latitude, double longitude);
	public Gcategory getBusinessCategory(String name);
	public List<Business> getBusinessBySearchKeyword(String searchText,String category, String location,int offset, int limit, double lat, double lon, int distance, double rating);
	public List<Business> getBusinessByLikeSearchKeyword(String searchText,String category, String location,int offset, int limit, double lat, double lon, int distance, double rating);
	public List<Business> getBusinessByCriteriaWithoutRating(String userEmail, String searchText, String category, String location, String rating);
	public List<Business> getUserNonDealSuggestions(String userEmail, int offset,int limit, String category, String suggestionType, double lat, double lon, int distance, double rating);
	public List<Object[]> getTopBusinessByRating(String idsArray);
	public List<Object[]> getNonDealLikeCountByPage(String idsStr);
	public int getTotalBusinessBySearchKeyWord(String searchText,String category, String location, double lat, double lon, int distance, double rating);
	public Lcategory getLivingSocialBusinessCategory(String name);
	public List<Business> getBusinessByIdForLivingSocial(String businessId);
	public List<Business> getBusinessByNameForLivingSocial(String merchantName);
	public List<Businesstype> getBusinesstypes();
}
