package com.jeeyoh.persistence.domain;

import java.io.Serializable;

public class DealFunboard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Deals deal;
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
	 * @param deal the deal to set
	 */
	public void setDeal(Deals deal) {
		this.deal = deal;
	}
	/**
	 * @return the deal
	 */
	public Deals getDeal() {
		return deal;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

}
