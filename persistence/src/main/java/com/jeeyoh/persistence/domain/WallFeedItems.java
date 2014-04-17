package com.jeeyoh.persistence.domain;

import java.io.Serializable;

public class WallFeedItems implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer wallFeddItemId;
	private WallFeed wallFeed;
	private Funboard funboard;
	private Integer itemId;
	private String category;
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
	
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public WallFeed getWallFeed() {
		return wallFeed;
	}
	public void setWallFeed(WallFeed wallFeed) {
		this.wallFeed = wallFeed;
	}
	public Integer getWallFeddItemId() {
		return wallFeddItemId;
	}
	public void setWallFeddItemId(Integer wallFeddItemId) {
		this.wallFeddItemId = wallFeddItemId;
	}

}
