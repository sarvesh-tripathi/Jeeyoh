package com.jeeyoh.persistence.domain;

import java.util.Date;

public class WallFeedComments  implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private WallFeed wallFeed;
	private User user;
	private String comment;
	private Date createdTime;
	private Date updatedTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	

}
