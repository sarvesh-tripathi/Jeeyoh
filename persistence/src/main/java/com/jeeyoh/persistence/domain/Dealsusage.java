package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Dealsusage generated by hbm2java
 */
public class Dealsusage  implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer dealUsageId;
	private User user;
	private Deals deals;
	private Boolean isRedempted;
	private Boolean isLike;
	private Boolean isFavorite;
	private Boolean isFollowing;
	private Boolean isSuggested;
	private Boolean isVisited;
	private Date createdtime;
	private Date updatedtime;
	private Boolean isBooked;

	public Dealsusage() {
	}


	public Dealsusage(User user, Deals deals, Date createdtime, Date updatedtime) {
		this.user = user;
		this.deals = deals;
		this.createdtime = createdtime;
		this.updatedtime = updatedtime;
	}
	public Dealsusage(User user, Deals deals, Boolean isRedempted, Boolean isLike, Boolean isFavorite, Boolean isFollowing, Boolean isSuggested, Date createdtime, Date updatedtime) {
		this.user = user;
		this.deals = deals;
		this.isRedempted = isRedempted;
		this.isLike = isLike;
		this.isFavorite = isFavorite;
		this.isFollowing = isFollowing;
		this.isSuggested = isSuggested;
		this.createdtime = createdtime;
		this.updatedtime = updatedtime;
	}

	public Integer getDealUsageId() {
		return this.dealUsageId;
	}

	public void setDealUsageId(Integer dealUsageId) {
		this.dealUsageId = dealUsageId;
	}
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public Deals getDeals() {
		return this.deals;
	}

	public void setDeals(Deals deals) {
		this.deals = deals;
	}
	public Boolean getIsRedempted() {
		return this.isRedempted;
	}

	public void setIsRedempted(Boolean isRedempted) {
		this.isRedempted = isRedempted;
	}
	public Boolean getIsLike() {
		return this.isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}
	public Boolean getIsFavorite() {
		return this.isFavorite;
	}

	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public Boolean getIsFollowing() {
		return this.isFollowing;
	}

	public void setIsFollowing(Boolean isFollowing) {
		this.isFollowing = isFollowing;
	}
	public Boolean getIsSuggested() {
		return this.isSuggested;
	}

	public void setIsSuggested(Boolean isSuggested) {
		this.isSuggested = isSuggested;
	}
	public Date getCreatedtime() {
		return this.createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}
	public Date getUpdatedtime() {
		return this.updatedtime;
	}

	public void setUpdatedtime(Date updatedtime) {
		this.updatedtime = updatedtime;
	}


	/**
	 * @return the isVisited
	 */
	 public Boolean getIsVisited() {
		return isVisited;
	}


	/**
	 * @param isVisited the isVisited to set
	 */
	 public void setIsVisited(Boolean isVisited) {
		this.isVisited = isVisited;
	}


	/**
	 * @return the isBooked
	 */
	 public Boolean getIsBooked() {
		return isBooked;
	}


	/**
	 * @param isBooked the isBooked to set
	 */
	 public void setIsBooked(Boolean isBooked) {
		 this.isBooked = isBooked;
	 }




}


