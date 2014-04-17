package com.jeeyoh.persistence.domain;

import java.util.Date;

public class WallFeedUserShareMap  implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer wallFeedUserMapId;
	private WallFeed wallFeed;
	private User user;
	private User shareWithUser;
	private Date createdTime;
	private Date updatedTime;
	
	public Integer getWallFeedUserMapId() {
		return wallFeedUserMapId;
	}
	public void setWallFeedUserMapId(Integer wallFeedUserMapId) {
		this.wallFeedUserMapId = wallFeedUserMapId;
	}
	public WallFeed getWallFeed() {
		return wallFeed;
	}
	public void setWallFeed(WallFeed wallFeed) {
		this.wallFeed = wallFeed;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getShareWithUser() {
		return shareWithUser;
	}
	public void setShareWithUser(User shareWithUser) {
		this.shareWithUser = shareWithUser;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}
