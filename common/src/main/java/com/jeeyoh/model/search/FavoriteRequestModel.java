package com.jeeyoh.model.search;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class FavoriteRequestModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int userId;
	@JsonProperty
	private int itemId;
	@JsonProperty
	private String itemType;
	@JsonProperty
	private boolean isFav;
	@JsonProperty
	private boolean isFollow;
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @return the isFav
	 */
	public boolean getIsFav() {
		return isFav;
	}
	/**
	 * @param isFav the isFav to set
	 */
	public void setIsFav(boolean isFav) {
		this.isFav = isFav;
	}
	/**
	 * @return the isFollow
	 */
	public boolean getIsFollow() {
		return isFollow;
	}
	/**
	 * @param isFollow the isFollow to set
	 */
	public void setIsFollow(boolean isFollow) {
		this.isFollow = isFollow;
	}

}
