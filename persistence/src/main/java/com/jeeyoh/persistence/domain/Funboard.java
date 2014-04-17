package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class Funboard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer funBoardId;
	private User user;
	private int itemId;
	private String category;
	private String itemType;
	private Date createdTime;
	private Date updatedTime;
	private Timeline timeline;
	private String imageUrl;
	private Date startDate;
	private Date endDate;
	private String tag;
	
	/**
	 * @return the funBoardId
	 */
	public Integer getFunBoardId() {
		return funBoardId;
	}
	/**
	 * @param funBoardId the funBoardId to set
	 */
	public void setFunBoardId(Integer funBoardId) {
		this.funBoardId = funBoardId;
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
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
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
	 * @return the type
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param type the type to set
	 */
	public void setItemType(String type) {
		this.itemType = type;
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
	
	public Boolean getIsEvent() {
        return false;
    }
	
	public void setIsEvent(Boolean isEvent) {
        
    }
	/**
	 * @param timeline the timeline to set
	 */
	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}
	/**
	 * @return the timeline
	 */
	public Timeline getTimeline() {
		return timeline;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

}
