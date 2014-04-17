package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class WallFeedSharing implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Funboard funboard;
	private Integer itemId;
	private User user;
	private User sharedWithUser;
	private String category;
	private Date createdTime;
	private Date updatedTime;
	private String itemType;
	private int itemRank;
	private String packageName;
	private double packageRank;
	
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public double getPackageRank() {
		return packageRank;
	}
	public void setPackageRank(double packageRank) {
		this.packageRank = packageRank;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getSharedWithUser() {
		return sharedWithUser;
	}
	public void setSharedWithUser(User sharedWithUser) {
		this.sharedWithUser = sharedWithUser;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public Funboard getFunboard() {
		return funboard;
	}
	public void setFunboard(Funboard funboard) {
		this.funboard = funboard;
	}
	/**
	 * @return the itemRank
	 */
	public int getItemRank() {
		return itemRank;
	}
	/**
	 * @param itemRank the itemRank to set
	 */
	public void setItemRank(int itemRank) {
		this.itemRank = itemRank;
	}

}
