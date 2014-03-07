package com.jeeyoh.persistence.domain;

public class BusinessFunboard extends Funboard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Business business;
	
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

}
