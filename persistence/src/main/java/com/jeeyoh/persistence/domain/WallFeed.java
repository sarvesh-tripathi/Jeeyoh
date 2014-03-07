package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class WallFeed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer wallFeedId;
	private Integer itemId;
	private Integer weightCount;
	private String itemType;
	private String category;
	private User user;
	private Date createdTime;
	private Date updatedTime; 
	
	public Integer getWallFeedId() {
		return wallFeedId;
	}
	public void setWallFeedId(Integer wallFeedId) {
		this.wallFeedId = wallFeedId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getWeightCount() {
		return weightCount;
	}
	public void setWeightCount(Integer weightCount) {
		this.weightCount = weightCount;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	/**
	 * @return the updatedTime
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

}
