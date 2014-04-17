package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class Topcommunitysuggestion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer suggestionId;
	private User user;
	private Page page;
	private Long rank;
	private int totalLikes;
	private String categoryType;
	private Date createdTime;
	private Date updatedTime;
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
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
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
	 * @return the totalLikes
	 */
	public int getTotalLikes() {
		return totalLikes;
	}
	/**
	 * @param totalLikes the totalLikes to set
	 */
	public void setTotalLikes(int totalLikes) {
		this.totalLikes = totalLikes;
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

}
