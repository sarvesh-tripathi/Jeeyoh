package com.jeeyoh.persistence.domain;

import java.io.Serializable;

public class BusinessFunboard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Business business;
	private String category;
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param business the business to set
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}
	/**
	 * @return the business
	 */
	public Business getBusiness() {
		return business;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

}
