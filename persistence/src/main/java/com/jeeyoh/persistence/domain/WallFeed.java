package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class WallFeed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer packageId;
	private User user;
	private String packageName;
	 private Set wallFeedItems = new HashSet(0);
	 private Set wallFeedUserShareMap = new HashSet(0);
	
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
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
	
	
	

}
