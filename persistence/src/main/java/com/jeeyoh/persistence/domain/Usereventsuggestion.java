package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class Usereventsuggestion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userEventMapId;
    private User user;
    private User userContact;
    private Events events;
    private Boolean isFavorite;
    private Boolean isFollowing;
    private Boolean isLike;
    private Boolean isRedempted;
    private Date createdTime;
    private Date updatedTime;
    private String suggestionType;
    
    public Usereventsuggestion()
    {
    	
    }
    
	public void setUserEventMapId(Integer userEventMapId) {
		this.userEventMapId = userEventMapId;
	}
	public Integer getUserEventMapId() {
		return userEventMapId;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
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

	public void setEvents(Events events) {
		this.events = events;
	}
	public Events getEvents() {
		return events;
	}
	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public Boolean getIsFavorite() {
		return isFavorite;
	}
	public void setIsFollowing(Boolean isFollowing) {
		this.isFollowing = isFollowing;
	}
	public Boolean getIsFollowing() {
		return isFollowing;
	}
	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}
	public Boolean getIsLike() {
		return isLike;
	}
	public void setIsRedempted(Boolean isRedempted) {
		this.isRedempted = isRedempted;
	}
	public Boolean getIsRedempted() {
		return isRedempted;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param suggestionType the suggestionType to set
	 */
	public void setSuggestionType(String suggestionType) {
		this.suggestionType = suggestionType;
	}

	/**
	 * @return the suggestionType
	 */
	public String getSuggestionType() {
		return suggestionType;
	}

}
