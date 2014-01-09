package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserCategory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userCategoryId ;
    private String itemType ;
    private String itemCategory ;
    private String itemSubCategory;
    private String providerName;
    private Long rating;
    private String providerLocation;
    private String providerLongitude;
    private String providerLattitude;
    private Date createdTime;
    private Date updatedTime;
    private Set userCategoryLikes = new HashSet(0);
    
    
    public UserCategory() {
    } 
    
	/**
	 * @param userCategoryId the userCategoryId to set
	 */
	public void setUserCategoryId(Integer userCategoryId) {
		this.userCategoryId = userCategoryId;
	}
	/**
	 * @return the userCategoryId
	 */
	public Integer getUserCategoryId() {
		return userCategoryId;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemCategory the itemCategory to set
	 */
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
	/**
	 * @return the itemCategory
	 */
	public String getItemCategory() {
		return itemCategory;
	}
	/**
	 * @param itemSubCategory the itemSubCategory to set
	 */
	public void setItemSubCategory(String itemSubCategory) {
		this.itemSubCategory = itemSubCategory;
	}
	/**
	 * @return the itemSubCategory
	 */
	public String getItemSubCategory() {
		return itemSubCategory;
	}
	/**
	 * @param providerName the providerName to set
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	/**
	 * @return the providerName
	 */
	public String getProviderName() {
		return providerName;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Long rating) {
		this.rating = rating;
	}
	/**
	 * @return the rating
	 */
	public Long getRating() {
		return rating;
	}
	/**
	 * @param providerLocation the providerLocation to set
	 */
	public void setProviderLocation(String providerLocation) {
		this.providerLocation = providerLocation;
	}
	/**
	 * @return the providerLocation
	 */
	public String getProviderLocation() {
		return providerLocation;
	}
	/**
	 * @param providerLongitude the providerLongitude to set
	 */
	public void setProviderLongitude(String providerLongitude) {
		this.providerLongitude = providerLongitude;
	}
	/**
	 * @return the providerLongitude
	 */
	public String getProviderLongitude() {
		return providerLongitude;
	}
	/**
	 * @param providerLattitude the providerLattitude to set
	 */
	public void setProviderLattitude(String providerLattitude) {
		this.providerLattitude = providerLattitude;
	}
	/**
	 * @return the providerLattitude
	 */
	public String getProviderLattitude() {
		return providerLattitude;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	/**
	 * @return the updatedTime
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}
	/**
	 * @param userCategoryLikes the userCategoryLikes to set
	 */
	public void setUserCategoryLikes(Set userCategoryLikes) {
		this.userCategoryLikes = userCategoryLikes;
	}
	/**
	 * @return the userCategoryLikes
	 */
	public Set getUserCategoryLikes() {
		return userCategoryLikes;
	}

}
