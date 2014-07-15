package com.jeeyoh.model.search;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class SearchRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int userId;
	@JsonProperty
	private String emailId;
	@JsonProperty
	private String searchText;
	@JsonProperty
	private String location;
	@JsonProperty
	private double rating;
	@JsonProperty
	private String category;
	@JsonProperty
	private int offset;
	@JsonProperty
	private int limit;
	@JsonProperty
	private String abbreviation;
	@JsonProperty
	private int exactMatchBusinessCount;
	@JsonProperty
	private int likeMatchBusinessCount;
	@JsonProperty
	private int exactMatchDealCount;
	@JsonProperty
	private int likeMatchDealCount;
	@JsonProperty
	private int exactMatchEventCount;
	@JsonProperty
	private int likeMatchEventCount;
	@JsonProperty
	private int exactMatchCommunityCount;
	@JsonProperty
	private int likeMatchCommunityCount;
	@JsonProperty
	private int exactMatchEventCommunityCount;
	@JsonProperty
	private int likeMatchEventCommunityCount;
	@JsonProperty
	private double latitude;
	@JsonProperty
	private double longitude;
	@JsonProperty
	private int distance;
	@JsonProperty
	private int minPricae;
	@JsonProperty
	private int maxPrice;
	@JsonProperty
	private String zipCode;
	@JsonProperty
	private String itemType;
	
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
	 * @return the userEmail
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}
	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the rating
	 */
	public double getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}
	/**
	 * @param abbreviation the abbreviation to set
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	/**
	 * @return the exactMatchBusinessCount
	 */
	public int getExactMatchBusinessCount() {
		return exactMatchBusinessCount;
	}
	/**
	 * @param exactMatchBusinessCount the exactMatchBusinessCount to set
	 */
	public void setExactMatchBusinessCount(int exactMatchBusinessCount) {
		this.exactMatchBusinessCount = exactMatchBusinessCount;
	}
	/**
	 * @return the likeMatchBusinessCount
	 */
	public int getLikeMatchBusinessCount() {
		return likeMatchBusinessCount;
	}
	/**
	 * @param likeMatchBusinessCount the likeMatchBusinessCount to set
	 */
	public void setLikeMatchBusinessCount(int likeMatchBusinessCount) {
		this.likeMatchBusinessCount = likeMatchBusinessCount;
	}
	/**
	 * @return the exactMatchDealCount
	 */
	public int getExactMatchDealCount() {
		return exactMatchDealCount;
	}
	/**
	 * @param exactMatchDealCount the exactMatchDealCount to set
	 */
	public void setExactMatchDealCount(int exactMatchDealCount) {
		this.exactMatchDealCount = exactMatchDealCount;
	}
	/**
	 * @return the likeMatchDealCount
	 */
	public int getLikeMatchDealCount() {
		return likeMatchDealCount;
	}
	/**
	 * @param likeMatchDealCount the likeMatchDealCount to set
	 */
	public void setLikeMatchDealCount(int likeMatchDealCount) {
		this.likeMatchDealCount = likeMatchDealCount;
	}
	/**
	 * @return the exactMatchEventCount
	 */
	public int getExactMatchEventCount() {
		return exactMatchEventCount;
	}
	/**
	 * @param exactMatchEventCount the exactMatchEventCount to set
	 */
	public void setExactMatchEventCount(int exactMatchEventCount) {
		this.exactMatchEventCount = exactMatchEventCount;
	}
	/**
	 * @return the likeMatchEventCount
	 */
	public int getLikeMatchEventCount() {
		return likeMatchEventCount;
	}
	/**
	 * @param likeMatchEventCount the likeMatchEventCount to set
	 */
	public void setLikeMatchEventCount(int likeMatchEventCount) {
		this.likeMatchEventCount = likeMatchEventCount;
	}
	/**
	 * @return the exactMatchCommunityCount
	 */
	public int getExactMatchCommunityCount() {
		return exactMatchCommunityCount;
	}
	/**
	 * @param exactMatchCommunityCount the exactMatchCommunityCount to set
	 */
	public void setExactMatchCommunityCount(int exactMatchCommunityCount) {
		this.exactMatchCommunityCount = exactMatchCommunityCount;
	}
	/**
	 * @return the likeMatchCommunityCount
	 */
	public int getLikeMatchCommunityCount() {
		return likeMatchCommunityCount;
	}
	/**
	 * @param likeMatchCommunityCount the likeMatchCommunityCount to set
	 */
	public void setLikeMatchCommunityCount(int likeMatchCommunityCount) {
		this.likeMatchCommunityCount = likeMatchCommunityCount;
	}
	/**
	 * @return the likeMatchEventCommunityCount
	 */
	public int getLikeMatchEventCommunityCount() {
		return likeMatchEventCommunityCount;
	}
	/**
	 * @param likeMatchEventCommunityCount the likeMatchEventCommunityCount to set
	 */
	public void setLikeMatchEventCommunityCount(int likeMatchEventCommunityCount) {
		this.likeMatchEventCommunityCount = likeMatchEventCommunityCount;
	}
	/**
	 * @return the exactMatchEventCommunityCount
	 */
	public int getExactMatchEventCommunityCount() {
		return exactMatchEventCommunityCount;
	}
	/**
	 * @param exactMatchEventCommunityCount the exactMatchEventCommunityCount to set
	 */
	public void setExactMatchEventCommunityCount(int exactMatchEventCommunityCount) {
		this.exactMatchEventCommunityCount = exactMatchEventCommunityCount;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
	/**
	 * @return the minPricae
	 */
	public int getMinPricae() {
		return minPricae;
	}
	/**
	 * @param minPricae the minPricae to set
	 */
	public void setMinPricae(int minPricae) {
		this.minPricae = minPricae;
	}
	/**
	 * @return the maxPrice
	 */
	public int getMaxPrice() {
		return maxPrice;
	}
	/**
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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

}
