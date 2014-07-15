package com.jeeyoh.model.search;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.user.UserModel;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class EventModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int eventId;
	@JsonProperty
	private String description;	
	@JsonProperty
	private String event_date;
	@JsonProperty	
	private String geography_parent;	
	@JsonProperty
	private String venue_name;
	@JsonProperty
	private String city;
	@JsonProperty
	private String state;
	@JsonProperty
	private String genreUrlPath;
	@JsonProperty
	private String urlpath;
	@JsonProperty
	private String channel;
	@JsonProperty
	private String totalTickets;
	@JsonProperty
	private String zipCode;
	@JsonProperty
	private double latitude;
	@JsonProperty
	private double longitude;	                         
	@JsonProperty
	private String	venue_config_name;
	@JsonProperty
	private String	currency_code;
	@JsonProperty
	private double	maxPrice;
	@JsonProperty
	private double	minPrice;
	@JsonProperty
	private double	maxSeatsTogether;
	@JsonProperty
	private double	minSeatsTogether;
	@JsonProperty
	private String	title;
	@JsonProperty
	private String ancestorGenreDescriptions;
	@JsonProperty
	private String suggestionType;
	@JsonProperty
	private String itemType;
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
	private String timeLine;
	@JsonProperty
	private String startDate;
	@JsonProperty
	private String endDate;
	@JsonProperty
    private String address;
	@JsonProperty
	private String timeSlot;
	@JsonProperty
	private boolean isBooked;
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
	 * @param eventId the eventId to set
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	/**
	 * @return the eventId
	 */
	public int getEventId() {
		return eventId;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the event_date
	 */
	public String getEvent_date() {
		return event_date;
	}
	/**
	 * @param event_date the event_date to set
	 */
	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}
	/**
	 * @return the geography_parent
	 */
	public String getGeography_parent() {
		return geography_parent;
	}
	/**
	 * @param geography_parent the geography_parent to set
	 */
	public void setGeography_parent(String geography_parent) {
		this.geography_parent = geography_parent;
	}
	/**
	 * @return the venue_name
	 */
	public String getVenue_name() {
		return venue_name;
	}
	/**
	 * @param venue_name the venue_name to set
	 */
	public void setVenue_name(String venue_name) {
		this.venue_name = venue_name;
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
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the genreUrlPath
	 */
	public String getGenreUrlPath() {
		return genreUrlPath;
	}
	/**
	 * @param genreUrlPath the genreUrlPath to set
	 */
	public void setGenreUrlPath(String genreUrlPath) {
		this.genreUrlPath = genreUrlPath;
	}
	/**
	 * @return the urlpath
	 */
	public String getUrlpath() {
		return urlpath;
	}
	/**
	 * @param urlpath the urlpath to set
	 */
	public void setUrlpath(String urlpath) {
		this.urlpath = urlpath;
	}
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the totalTickets
	 */
	public String getTotalTickets() {
		return totalTickets;
	}
	/**
	 * @param totalTickets the totalTickets to set
	 */
	public void setTotalTickets(String totalTickets) {
		this.totalTickets = totalTickets;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zip the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
	 * @return the venue_config_name
	 */
	public String getVenue_config_name() {
		return venue_config_name;
	}
	/**
	 * @param venue_config_name the venue_config_name to set
	 */
	public void setVenue_config_name(String venue_config_name) {
		this.venue_config_name = venue_config_name;
	}
	/**
	 * @return the currency_code
	 */
	public String getCurrency_code() {
		return currency_code;
	}
	/**
	 * @param currency_code the currency_code to set
	 */
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	/**
	 * @return the maxPrice
	 */
	public double getMaxPrice() {
		return maxPrice;
	}
	/**
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	/**
	 * @return the minPrice
	 */
	public double getMinPrice() {
		return minPrice;
	}
	/**
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	/**
	 * @return the maxSeatsTogether
	 */
	public double getMaxSeatsTogether() {
		return maxSeatsTogether;
	}
	/**
	 * @param maxSeatsTogether the maxSeatsTogether to set
	 */
	public void setMaxSeatsTogether(double maxSeatsTogether) {
		this.maxSeatsTogether = maxSeatsTogether;
	}
	/**
	 * @return the minSeatsTogether
	 */
	public double getMinSeatsTogether() {
		return minSeatsTogether;
	}
	/**
	 * @param minSeatsTogether the minSeatsTogether to set
	 */
	public void setMinSeatsTogether(double minSeatsTogether) {
		this.minSeatsTogether = minSeatsTogether;
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
	 * @return the ancestorGenreDescriptions
	 */
	public String getAncestorGenreDescriptions() {
		return ancestorGenreDescriptions;
	}
	/**
	 * @param ancestorGenreDescriptions the ancestorGenreDescriptions to set
	 */
	public void setAncestorGenreDescriptions(String ancestorGenreDescriptions) {
		this.ancestorGenreDescriptions = ancestorGenreDescriptions;
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
	 * @return the timeSlot
	 */
	public String getTimeSlot() {
		return timeSlot;
	}
	/**
	 * @param timeSlot the timeSlot to set
	 */
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
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
