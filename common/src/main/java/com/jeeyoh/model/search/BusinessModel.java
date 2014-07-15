package com.jeeyoh.model.search;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.user.UserModel;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class BusinessModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int itemId;
	@JsonProperty
	private String businessType;
	@JsonProperty
	private String businessId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String websiteUrl;
	@JsonProperty
	private String mobileUrl;
	@JsonProperty
	private String phone;
	@JsonProperty
	private String displayPhone;
	@JsonProperty
	private String reviewCount;
	@JsonProperty
	private double distance;
	@JsonProperty
	private double rating;
	@JsonProperty
	private String ratingImgUrl;
	@JsonProperty
	private String menuProvider;
	@JsonProperty
	private String city;
	@JsonProperty
	private String displayAddress;
	@JsonProperty
	private String zipCode;
	@JsonProperty
	private String countryCode;
	@JsonProperty
	private String address;
	@JsonProperty
	private String stateCode;
	@JsonProperty
	private double lattitude;
	@JsonProperty
	private double longitude;
	@JsonProperty
	private String ambience;
	@JsonProperty
	private String imageUrl;
	@JsonProperty
	private String workingHours;
	@JsonProperty
	private String musicType;
	@JsonProperty
	private String source;
	@JsonProperty
	private String suggestionType;
	@JsonProperty
	private String itemType;
	@JsonProperty
	private boolean isFavorite;
	@JsonProperty
    private String suggestionCriteria;
	@JsonProperty
    private String suggestedBy;
	@JsonProperty
    private UserModel suggestingUser;
	@JsonProperty
	private boolean isBooked;
	
	
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
	 * @return the businesstype
	 */
	public String getBusinessType() {
		return businessType;
	}
	/**
	 * @param businesstype the businesstype to set
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	/**
	 * @return the businessId
	 */
	public String getBusinessId() {
		return businessId;
	}
	/**
	 * @param businessId the businessId to set
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the websiteUrl
	 */
	public String getWebsiteUrl() {
		return websiteUrl;
	}
	/**
	 * @param websiteUrl the websiteUrl to set
	 */
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	/**
	 * @return the mobileUrl
	 */
	public String getMobileUrl() {
		return mobileUrl;
	}
	/**
	 * @param mobileUrl the mobileUrl to set
	 */
	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the displayPhone
	 */
	public String getDisplayPhone() {
		return displayPhone;
	}
	/**
	 * @param displayPhone the displayPhone to set
	 */
	public void setDisplayPhone(String displayPhone) {
		this.displayPhone = displayPhone;
	}
	/**
	 * @return the reviewCount
	 */
	public String getReviewCount() {
		return reviewCount;
	}
	/**
	 * @param reviewCount the reviewCount to set
	 */
	public void setReviewCount(String reviewCount) {
		this.reviewCount = reviewCount;
	}
	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
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
	 * @return the ratingImgUrl
	 */
	public String getRatingImgUrl() {
		return ratingImgUrl;
	}
	/**
	 * @param ratingImgUrl the ratingImgUrl to set
	 */
	public void setRatingImgUrl(String ratingImgUrl) {
		this.ratingImgUrl = ratingImgUrl;
	}
	/**
	 * @return the menuProvider
	 */
	public String getMenuProvider() {
		return menuProvider;
	}
	/**
	 * @param menuProvider the menuProvider to set
	 */
	public void setMenuProvider(String menuProvider) {
		this.menuProvider = menuProvider;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the displayAddress
	 */
	public String getDisplayAddress() {
		return displayAddress;
	}
	/**
	 * @param displayAddress the displayAddress to set
	 */
	public void setDisplayAddress(String displayAddress) {
		this.displayAddress = displayAddress;
	}
	/**
	 * @return the postalCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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
	 * @return the stateCode
	 */
	public String getStateCode() {
		return stateCode;
	}
	/**
	 * @param stateCode the stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	/**
	 * @return the lattitude
	 */
	public double getLattitude() {
		return lattitude;
	}
	/**
	 * @param lattitude the lattitude to set
	 */
	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
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
	 * @return the ambience
	 */
	public String getAmbience() {
		return ambience;
	}
	/**
	 * @param ambience the ambience to set
	 */
	public void setAmbience(String ambience) {
		this.ambience = ambience;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the workingHours
	 */
	public String getWorkingHours() {
		return workingHours;
	}
	/**
	 * @param workingHours the workingHours to set
	 */
	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}
	/**
	 * @return the musicType
	 */
	public String getMusicType() {
		return musicType;
	}
	/**
	 * @param musicType the musicType to set
	 */
	public void setMusicType(String musicType) {
		this.musicType = musicType;
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
	 * @return the suggestionType
	 */
	public String getSuggestionType() {
		return suggestionType;
	}
	/**
	 * @param suggestionType the suggestionType to set
	 */
	public void setSuggestionType(String suggestionType) {
		this.suggestionType = suggestionType;
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
