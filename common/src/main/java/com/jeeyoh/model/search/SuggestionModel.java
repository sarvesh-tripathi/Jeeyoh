package com.jeeyoh.model.search;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class SuggestionModel implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty
	private String categoryType;
	@JsonProperty
	private String title;
	@JsonProperty
	private String url;
	@JsonProperty
	private String mobileUrl;
	@JsonProperty
	private String phone;
	@JsonProperty
	private String displayPhone;
	@JsonProperty
	private String reviewCount;
	@JsonProperty
	private Long distance;
	@JsonProperty
	private Long rating;
	@JsonProperty
	private String ratingImgUrl;
	@JsonProperty
	private String menuProvider;
	@JsonProperty
	private String city;
	@JsonProperty
	private String displayAddress;
	@JsonProperty
	private String postalCode;
	@JsonProperty
	private String countryCode;
	@JsonProperty
	private String address;
	@JsonProperty
	private String stateCode;
	@JsonProperty
	private String lattitude;
	@JsonProperty
	private String longitude;
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
	private String startAt;
	@JsonProperty
	private String endAt;
	@JsonProperty
    private String profilePicture;
	@JsonProperty
    private String owner;
	@JsonProperty
    private String description;
	@JsonProperty
    private String event_date;
	@JsonProperty
    private String venue_name;
	@JsonProperty
    private String totalTickets;
	@JsonProperty
    private String ancestorGenreDescriptions;
	/**
	 * @return the categoryType
	 */
	public String getCategoryType() {
		return categoryType;
	}
	/**
	 * @param categoryType the categoryType to set
	 */
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	public Long getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Long distance) {
		this.distance = distance;
	}
	/**
	 * @return the rating
	 */
	public Long getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Long rating) {
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
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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
	public String getLattitude() {
		return lattitude;
	}
	/**
	 * @param lattitude the lattitude to set
	 */
	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
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
	 * @return the startAt
	 */
	public String getStartAt() {
		return startAt;
	}
	/**
	 * @param startAt the startAt to set
	 */
	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}
	/**
	 * @return the endAt
	 */
	public String getEndAt() {
		return endAt;
	}
	/**
	 * @param endAt the endAt to set
	 */
	public void setEndAt(String endAt) {
		this.endAt = endAt;
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param event_date the event_date to set
	 */
	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}
	/**
	 * @return the event_date
	 */
	public String getEvent_date() {
		return event_date;
	}
	/**
	 * @param venue_name the venue_name to set
	 */
	public void setVenue_name(String venue_name) {
		this.venue_name = venue_name;
	}
	/**
	 * @return the venue_name
	 */
	public String getVenue_name() {
		return venue_name;
	}
	/**
	 * @param totalTickets the totalTickets to set
	 */
	public void setTotalTickets(String totalTickets) {
		this.totalTickets = totalTickets;
	}
	/**
	 * @return the totalTickets
	 */
	public String getTotalTickets() {
		return totalTickets;
	}
	/**
	 * @param ancestorGenreDescriptions the ancestorGenreDescriptions to set
	 */
	public void setAncestorGenreDescriptions(String ancestorGenreDescriptions) {
		this.ancestorGenreDescriptions = ancestorGenreDescriptions;
	}
	/**
	 * @return the ancestorGenreDescriptions
	 */
	public String getAncestorGenreDescriptions() {
		return ancestorGenreDescriptions;
	}

}
