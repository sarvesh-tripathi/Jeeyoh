package com.jeeyoh.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Gcategory;

@Repository("businessDAO")
public interface IBusinessDAO {
	public List<Business> getBusinesses();
	public List<Business> getBusinessByName(String name);
	public List<Business> getBusinessById(String businessId);
	public List<Business> getBusinessByIdForGroupon(String businessId);
	public List<Business> getBusinessById(int id);
	public void saveBusiness(Business business, int count);
	public List<Businesstype> getBusinesstypeByTypeArray(String[] type);
	public Businesstype getBusinesstypeByType(String type);
	public void saveBusiness(Business business);
	public List<Business> getBusinessByCriteria(String userEmail, String searchText, String category, String location, String rating);
	public List<Business> getBusinessByuserLikes(String likekeyword, String itemCategory, String providerName);
	public Gcategory getBusinessCategory(String name);
	public List<Business> getBusinessBySearchKeyword(String searchText,String category, String location);
	public List<Business> getBusinessByLikeSearchKeyword(String searchText,String category, String location);
	public List<Business> getBusinessByCriteriaWithoutRating(String userEmail, String searchText, String category, String location, String rating);
}
