package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class WallFeed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer wallFeedId;
	private User user;
	private String packageName;
	private Date createdTime;
	private Date updatedTime;
	 private Set wallFeedItems = new HashSet(0);
	 private Set wallFeedUserShareMap = new HashSet(0);
	 private Set wallFeedComments = new HashSet(0);
	
	public Integer getWallFeedId() {
		return wallFeedId;
	}
	public void setWallFeedId(Integer wallFeedId) {
		this.wallFeedId = wallFeedId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the wallFeedItems
	 */
	public Set getWallFeedItems() {
		return wallFeedItems;
	}
	/**
	 * @param wallFeedItems the wallFeedItems to set
	 */
	public void setWallFeedItems(Set wallFeedItems) {
		this.wallFeedItems = wallFeedItems;
	}
	/**
	 * @return the wallFeedUserShareMap
	 */
	public Set getWallFeedUserShareMap() {
		return wallFeedUserShareMap;
	}
	/**
	 * @param wallFeedUserShareMap the wallFeedUserShareMap to set
	 */
	public void setWallFeedUserShareMap(Set wallFeedUserShareMap) {
		this.wallFeedUserShareMap = wallFeedUserShareMap;
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
	 * @return the wallFeedComments
	 */
	public Set getWallFeedComments() {
		return wallFeedComments;
	}
	/**
	 * @param wallFeedComments the wallFeedComments to set
	 */
	public void setWallFeedComments(Set wallFeedComments) {
		this.wallFeedComments = wallFeedComments;
	}
	
	
	

}
