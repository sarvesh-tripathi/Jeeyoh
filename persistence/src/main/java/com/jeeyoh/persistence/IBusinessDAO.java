package com.jeeyoh.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;

@Repository("businessDAO")
public interface IBusinessDAO {
	public List<Business> getBusinesses();
	
	public List<Business> getBusinessByName(String name);
	
	public List<Business> getBusinessById(String businessId);
	public List<Business> getBusinessByIdForGroupon(String businessId);
	public List<Business> getBusinessById(int id);
	public void saveBusiness(Business business, int count);
	public Businesstype getBusinesstypeByType(String type);
	public void saveBusiness(Business business);
}
