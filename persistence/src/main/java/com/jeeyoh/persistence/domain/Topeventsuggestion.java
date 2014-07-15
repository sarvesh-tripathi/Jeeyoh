package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class Topeventsuggestion implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer suggestionId;
	private User user;
	private User userContact;
	private Events events;
	private Long rank;
	private int totalLikes;
	private String suggestionType;
	private String categoryType;
	private Date createdTime;
	private Date updatedTime;
	private Date suggestedTime;
	/**
	 * @return the suggestionId
	 */
	public Integer getSuggestionId() {
		return suggestionId;
	}
	/**
	 * @param suggestionId the suggestionId to set
	 */
	public void setSuggestionId(Integer suggestionId) {
		this.suggestionId = suggestionId;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the events
	 */
	public Events getEvents() {
		return events;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(Events events) {
		this.events = events;
	}
	/**
	 * @return the rank
	 */
	public Long getRank() {
		return rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(Long rank) {
		this.rank = rank;
	}
	/**
	 * @param totalLikes the totalLikes to set
	 */
	public void setTotalLikes(int totalLikes) {
		this.totalLikes = totalLikes;
	}
	/**
	 * @return the totalLikes
	 */
	public int getTotalLikes() {
		return totalLikes;
	}
	/**
	 * @return the suggestionType
	 */
	public String getSuggestionType() {
		return suggestionType;
	}
	/**
	 * @param suggestionType the suggestionType to set
	 */
	public void setSuggestionType(String suggestionType) {
		this.suggestionType = suggestionType;
	}
	/**
	 * @param categoryType the categoryType to set
	 */
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	/**
	 * @return the categoryType
	 */
	public String getCategoryType() {
		return categoryType;
	}
	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @return the updatedTime
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}
	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	/**
	 * @return the userContact
	 */
	public User getUserContact() {
		return userContact;
	}
	/**
	 * @param userContact the userContact to set
	 */
	public void setUserContact(User userContact) {
		this.userContact = userContact;
	}
	/**
	 * @return the suggestedTime
	 */
	public Date getSuggestedTime() {
		return suggestedTime;
	}
	/**
	 * @param suggestedTime the suggestedTime to set
	 */
	public void setSuggestedTime(Date suggestedTime) {
		this.suggestedTime = suggestedTime;
	}

}
