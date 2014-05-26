package com.jeeyoh.service.search;

import java.io.Serializable;

import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.User;

public class MatchinEventModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private Events events;
	private Deals deals;
	private Business business;
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @return the events
	 */
	public Events getEvents() {
		return events;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(Events events) {
		this.events = events;
	}
	/**
	 * @return the deals
	 */
	public Deals getDeals() {
		return deals;
	}
	/**
	 * @param deals the deals to set
	 */
	public void setDeals(Deals deals) {
		this.deals = deals;
	}
	/**
	 * @return the business
	 */
	public Business getBusiness() {
		return business;
	}
	/**
	 * @param business the business to set
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

}
