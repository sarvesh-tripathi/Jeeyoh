package com.jeeyoh.persistence.domain;

import java.io.Serializable;

public class EventFunboard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Events events;
	private String category;
	/**
	 * @param events the events to set
	 */
	public void setEvents(Events events) {
		this.events = events;
	}
	/**
	 * @return the events
	 */
	public Events getEvents() {
		return events;
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
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
