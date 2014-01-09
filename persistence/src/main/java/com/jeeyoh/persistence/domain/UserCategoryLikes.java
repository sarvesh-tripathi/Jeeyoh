package com.jeeyoh.persistence.domain;

import java.io.Serializable;

public class UserCategoryLikes implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userCategoryLikesId ;
    private User user ;
    private UserCategory userCategory;
    private Long rank;
    
    public UserCategoryLikes() {
    } 
	/**
	 * @param userCategoryLikesId the userCategoryLikesId to set
	 */
	public void setUserCategoryLikesId(Integer userCategoryLikesId) {
		this.userCategoryLikesId = userCategoryLikesId;
	}
	/**
	 * @return the userCategoryLikesId
	 */
	public Integer getUserCategoryLikesId() {
		return userCategoryLikesId;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param userCategory the userCategory to set
	 */
	public void setUserCategory(UserCategory userCategory) {
		this.userCategory = userCategory;
	}
	/**
	 * @return the userCategory
	 */
	public UserCategory getUserCategory() {
		return userCategory;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(Long rank) {
		this.rank = rank;
	}
	/**
	 * @return the rank
	 */
	public Long getRank() {
		return rank;
	}

}
