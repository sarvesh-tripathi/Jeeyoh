package com.jeeyoh.persistence.domain;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WallFeedUserShareMap  implements java.io.Serializable {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		/*final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((shareWithUser == null) ? 0 : shareWithUser.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result
				+ ((wallFeed == null) ? 0 : wallFeed.hashCode());*/
		String code = "";
		if(user != null && user.getUserId() != null) {
			code = code + user.getUserId();
		} 
		if(wallFeed != null && wallFeed.getWallFeedId() != null) {
			code += wallFeed.getWallFeedId();
		} else {
			code += "0";
		}
		if(shareWithUser != null && shareWithUser.getUserId() != null) {
			code += shareWithUser.getUserId();
		}
		int result = Integer.parseInt(code);
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
		WallFeedUserShareMap other = (WallFeedUserShareMap) obj; 
		if (shareWithUser == null) {
			if (other.shareWithUser != null)
				return false;
		} else if (!shareWithUser.getUserId().equals(other.shareWithUser.getUserId()))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.getUserId().equals(other.user.getUserId()))
			return false;
		if (wallFeed == null) {
			if (other.wallFeed != null)
				return false;

		} else if(wallFeed.getWallFeedId() != null && other.wallFeed.getWallFeedId() != null) 
			if(!wallFeed.getWallFeedId().equals(other.wallFeed.getWallFeedId()))
				return false;
		return true;
	}
}
