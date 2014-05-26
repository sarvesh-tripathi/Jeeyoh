package com.jeeyoh.model.search;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.user.UserModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class DealModel implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int userId;
	@JsonProperty
	private int itemId;
	@JsonProperty
	private String title;
	@JsonProperty
	private String dealUrl;
	@JsonProperty
	private String status;
	@JsonProperty
	private String startAt;
	@JsonProperty
	private String imageUrl;
	@JsonProperty
	private String suggestionType;
	@JsonProperty
	private String websiteUrl;
	@JsonProperty
	private String itemType;
	@JsonProperty
	private String endAt;
	@JsonProperty
	private String category;
	@JsonProperty
	private String source;
	@JsonProperty
	private double rating;
	@JsonProperty
	private boolean isFavorite;
	@JsonProperty
	private String suggestionCriteria;
	@JsonProperty
    private String suggestedBy;
	@JsonProperty
    private UserModel suggestingUser;
	@JsonProperty
    private String price;
	@JsonProperty
    private String merhcantName;
	@JsonProperty
    private String address;
	@JsonProperty
    private int discount;
	@JsonProperty
	private boolean isBooked;
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @return the merhcantName
	 */
	public String getMerhcantName() {
		return merhcantName;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @param merhcantName the merhcantName to set
	 */
	public void setMerhcantName(String merhcantName) {
		this.merhcantName = merhcantName;
	}
	/**
	 * @return the suggestedBy
	 */
	public String getSuggestedBy() {
		return suggestedBy;
	}
	/**
	 * @param suggestedBy the suggestedBy to set
	 */
	public void setSuggestedBy(String suggestedBy) {
		this.suggestedBy = suggestedBy;
	}
	/**
	 * @return the suggestionCriteria
	 */
	public String getSuggestionCriteria() {
		return suggestionCriteria;
	}
	/**
	 * @param suggestionCriteria the suggestionCriteria to set
	 */
	public void setSuggestionCriteria(String suggestionCriteria) {
		this.suggestionCriteria = suggestionCriteria;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDealUrl() {
		return dealUrl;
	}
	public void setDealUrl(String dealUrl) {
		this.dealUrl = dealUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartAt() {
		return startAt;
	}
	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}
	public String getEndAt() {
		return endAt;
	}
	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}
	/**
	 * @param suggestionType the suggestionType to set
	 */
	public void setSuggestionType(String suggestionType) {
		this.suggestionType = suggestionType;
	}
	/**
	 * @return the suggestionType
	 */
	public String getSuggestionType() {
		return suggestionType;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param websiteUrl the websiteUrl to set
	 */
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	/**
	 * @return the websiteUrl
	 */
	public String getWebsiteUrl() {
		return websiteUrl;
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
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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
	 * @return the isFavorite
	 */
	public boolean getIsFavorite() {
		return isFavorite;
	}
	/**
	 * @param isFavorite the isFavorite to set
	 */
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	/**
	 * @return the suggestingUser
	 */
	public UserModel getSuggestingUser() {
		return suggestingUser;
	}
	/**
	 * @param suggestingUser the suggestingUser to set
	 */
	public void setSuggestingUser(UserModel suggestingUser) {
		this.suggestingUser = suggestingUser;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the discount
	 */
	public int getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	/**
	 * @return the isBooked
	 */
	public boolean getIsBooked() {
		return isBooked;
	}
	/**
	 * @param isBooked the isBooked to set
	 */
	public void setIsBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

}
