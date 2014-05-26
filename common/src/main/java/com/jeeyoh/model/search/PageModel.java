package com.jeeyoh.model.search;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class PageModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private int pageUserLikeId;
	@JsonProperty
	private int userId;
	@JsonProperty
	private int pageId;
	@JsonProperty
	private String pageType;
	@JsonProperty
    private String about;
	@JsonProperty
    private String pageUrl;
	@JsonProperty
    private String profilePicture;
	@JsonProperty
    private String owner;
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String imageUrl;
	@JsonProperty
	private String itemType;
	@JsonProperty
	private boolean isFavorite;
	@JsonProperty
	private double rating;
	@JsonProperty
	private String source;
	@JsonProperty
	private boolean isFollowed;
	@JsonProperty
	private String startDate;
	@JsonProperty
	private String endDate;
	@JsonProperty
    private String timeLine;
	@JsonProperty
    private String suggestionCriteria;
	@JsonProperty
	private String suggestedBy;
	@JsonProperty
	private boolean isBooked;
	
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
	/**
	 * @return the pagetype
	 */
	public String getPageType() {
		return pageType;
	}
	/**
	 * @param pagetype the pagetype to set
	 */
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	/**
	 * @return the about
	 */
	public String getAbout() {
		return about;
	}
	/**
	 * @param about the about to set
	 */
	public void setAbout(String about) {
		this.about = about;
	}
	/**
	 * @return the pageUrl
	 */
	public String getPageUrl() {
		return pageUrl;
	}
	/**
	 * @param pageUrl the pageUrl to set
	 */
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	/**
	 * @return the profilePicture
	 */
	public String getProfilePicture() {
		return profilePicture;
	}
	/**
	 * @param profilePicture the profilePicture to set
	 */
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param pageId the pageId to set
	 */
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	/**
	 * @return the pageId
	 */
	public int getPageId() {
		return pageId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param pageUserLikeId the pageUserLikeId to set
	 */
	public void setPageUserLikeId(int pageUserLikeId) {
		this.pageUserLikeId = pageUserLikeId;
	}
	/**
	 * @return the pageUserLikeId
	 */
	public int getPageUserLikeId() {
		return pageUserLikeId;
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
	 * @return the isFollowed
	 */
	public boolean getIsFollowed() {
		return isFollowed;
	}
	/**
	 * @param isFollowed the isFollowed to set
	 */
	public void setIsFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the timeLine
	 */
	public String getTimeLine() {
		return timeLine;
	}
	/**
	 * @param timeLine the timeLine to set
	 */
	public void setTimeLine(String timeLine) {
		this.timeLine = timeLine;
	}
	/**
	 * @return the suggestionCriteria
	 */
	public String getSuggestionCriteria() {
		return suggestionCriteria;
	}
	/**
	 * @return the suggestedBy
	 */
	public String getSuggestedBy() {
		return suggestedBy;
	}
	/**
	 * @param suggestionCriteria the suggestionCriteria to set
	 */
	public void setSuggestionCriteria(String suggestionCriteria) {
		this.suggestionCriteria = suggestionCriteria;
	}
	/**
	 * @param suggestedBy the suggestedBy to set
	 */
	public void setSuggestedBy(String suggestedBy) {
		this.suggestedBy = suggestedBy;
	}
	

}
