package com.jeeyoh.model.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Page;

public class MainModel {
	
	private String name;
	private String city;
	private String address;
	private List<Business> businessList = new ArrayList<Business>();
	private List<Page> pageList = new ArrayList<Page>();
	private List<Deals> dealList = new ArrayList<Deals>();
	private Set<Deals> dealModel;
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param businessList the businessList to set
	 */
	public void setBusinessList(List<Business> businessList) {
		this.businessList = businessList;
	}
	/**
	 * @return the businessList
	 */
	public List<Business> getBusinessList() {
		return businessList;
	}
	/**
	 * @param pageList the pageList to set
	 */
	public void setPageList(List<Page> pageList) {
		this.pageList = pageList;
	}
	/**
	 * @return the pageList
	 */
	public List<Page> getPageList() {
		return pageList;
	}
	public void setDealList(List<Deals> dealList) {
		this.dealList = dealList;
	}
	public List<Deals> getDealList() {
		return dealList;
	}
	
	public Set<Deals> getDealModel() {
		return dealModel;
	}

	public void setDealModel(Set<Deals> dealModel) {
		this.dealModel = dealModel;
	}
}
