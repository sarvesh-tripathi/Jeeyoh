package com.jeeyoh.persistence.domain;

public class DealFunboard extends Funboard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Deals deal;
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

}
