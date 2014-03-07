package com.jeeyoh.persistence.domain;

public class EventFunboard extends Funboard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Events events;
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

}
