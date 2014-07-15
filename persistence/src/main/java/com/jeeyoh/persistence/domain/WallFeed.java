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
	private String tag;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((wallFeedId == null) ? 0 : wallFeedId.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WallFeed other = (WallFeed) obj;
		if (wallFeedId == null) {
			if (other.wallFeedId != null)
				return false;
		} else if (!wallFeedId.equals(other.wallFeedId))
			return false;
		return true;
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
