package com.jeeyoh.model.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jeeyoh.model.user.UserModel;

public class MainModel {
	
	private String name;
	private String city;
	private String address;
	private List<BusinessModel> businessList = new ArrayList<BusinessModel>();
	private List<PageModel> pageList = new ArrayList<PageModel>();
	private List<DealModel> dealList = new ArrayList<DealModel>();
	private List<EventModel> eventsList = new ArrayList<EventModel>();
	private Set<DealModel> dealModel;
	private List<SearchResult> searchResult = new ArrayList<SearchResult>();
	private List<UserModel> usersList = new ArrayList<UserModel>();
	private boolean isUser;
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
	public void setBusinessList(List<BusinessModel> businessList) {
		this.businessList = businessList;
	}
	/**
	 * @return the businessList
	 */
	public List<BusinessModel> getBusinessList() {
		return businessList;
	}
	/**
	 * @param pageList the pageList to set
	 */
	public void setPageList(List<PageModel> pageList) {
		this.pageList = pageList;
	}
	/**
	 * @return the pageList
	 */
	public List<PageModel> getPageList() {
		return pageList;
	}
	public void setDealList(List<DealModel> dealList) {
		this.dealList = dealList;
	}
	public List<DealModel> getDealList() {
		return dealList;
	}
	
	public Set<DealModel> getDealModel() {
		return dealModel;
	}

	public void setDealModel(Set<DealModel> dealModel) {
		this.dealModel = dealModel;
	}
	/**
	 * @param eventsList the eventsList to set
	 */
	public void setEventsList(List<EventModel> eventsList) {
		this.eventsList = eventsList;
	}
	/**
	 * @return the eventsList
	 */
	public List<EventModel> getEventsList() {
		return eventsList;
	}
	/**
	 * @param isUser the isUser to set
	 */
	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}
	/**
	 * @return the isUser
	 */
	public boolean getIsUser() {
		return isUser;
	}
	/**
	 * @param searchResult the searchResult to set
	 */
	public void setSearchResult(List<SearchResult> searchResult) {
		this.searchResult = searchResult;
	}
	/**
	 * @return the searchResult
	 */
	public List<SearchResult> getSearchResult() {
		return searchResult;
	}
	/**
	 * @return the usersList
	 */
	public List<UserModel> getUsersList() {
		return usersList;
	}
	/**
	 * @param usersList the usersList to set
	 */
	public void setUsersList(List<UserModel> usersList) {
		this.usersList = usersList;
	}
}
