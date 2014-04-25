package com.jeeyoh.model.stubhub;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Description {
	@JsonProperty
	private String description;
	
	@JsonProperty
	private String event_date;
		
	@JsonProperty
	private String event_date_local;
	
	@JsonProperty
	private String event_time_local;	
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
	private String leaf;
	@JsonProperty
	private String channel;
	@JsonProperty
	private String totalTickets;
	@JsonProperty
	private String zip;
	@JsonProperty
	private String latitude;
	@JsonProperty
	private String longitude;
	
	/*@JsonProperty
	private <List>ancestorGenreDescriptions[];*/
	                         
	@JsonProperty
  	private String timezone;
  	@JsonProperty
	private String	timezone_id;
	@JsonProperty
	private String	venue_config_name;
	@JsonProperty
	private String	currency_code;
	@JsonProperty
	private String	event_config_template_id;
	@JsonProperty
	private String	event_config_template;
	@JsonProperty
	private String	keywords_en_US;
	@JsonProperty
	private String	maxPrice;
	@JsonProperty
	private String	minPrice;
	@JsonProperty
	private String	maxSeatsTogether;
	@JsonProperty
	private String	minSeatsTogether;
	@JsonProperty
	private String	title;
	
	@JsonProperty
	private List<String>ancestorGenreDescriptions;
	
	@JsonProperty
	private String image_url;
	
	@JsonProperty
	private String genre_parent_name;
	@JsonProperty
	private String title_en_US;
	
	public String getEvent_date() {
		return event_date;
	}

	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimezone_id() {
		return timezone_id;
	}

	public void setTimezone_id(String timezone_id) {
		this.timezone_id = timezone_id;
	}

	public String getVenue_config_name() {
		return venue_config_name;
	}

	public void setVenue_config_name(String venue_config_name) {
		this.venue_config_name = venue_config_name;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getEvent_config_template_id() {
		return event_config_template_id;
	}

	public void setEvent_config_template_id(String event_config_template_id) {
		this.event_config_template_id = event_config_template_id;
	}

	public String getEvent_config_template() {
		return event_config_template;
	}

	public void setEvent_config_template(String event_config_template) {
		this.event_config_template = event_config_template;
	}

	public String getKeywords_en_US() {
		return keywords_en_US;
	}

	public void setKeywords_en_US(String keywords_en_US) {
		this.keywords_en_US = keywords_en_US;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxSeatsTogether() {
		return maxSeatsTogether;
	}

	public void setMaxSeatsTogether(String maxSeatsTogether) {
		this.maxSeatsTogether = maxSeatsTogether;
	}

	public String getMinSeatsTogether() {
		return minSeatsTogether;
	}

	public void setMinSeatsTogether(String minSeatsTogether) {
		this.minSeatsTogether = minSeatsTogether;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEvent_date_local() {
		return event_date_local;
	}

	public void setEvent_date_local(String event_date_local) {
		this.event_date_local = event_date_local;
	}

	public String getEvent_time_local() {
		return event_time_local;
	}

	public void setEvent_time_local(String event_time_local) {
		this.event_time_local = event_time_local;
	}

	public String getGeography_parent() {
		return geography_parent;
	}

	public void setGeography_parent(String geography_parent) {
		this.geography_parent = geography_parent;
	}

	public String getVenue_name() {
		return venue_name;
	}

	public void setVenue_name(String venue_name) {
		this.venue_name = venue_name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGenreUrlPath() {
		return genreUrlPath;
	}

	public void setGenreUrlPath(String genreUrlPath) {
		this.genreUrlPath = genreUrlPath;
	}

	public String getUrlpath() {
		return urlpath;
	}

	public void setUrlpath(String urlpath) {
		this.urlpath = urlpath;
	}

	public String getLeaf() {
		return leaf;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTotalTickets() {
		return totalTickets;
	}

	public void setTotalTickets(String totalTickets) {
		this.totalTickets = totalTickets;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param ancestorGenreDescriptions the ancestorGenreDescriptions to set
	 */
	public void setAncestorGenreDescriptions(
			List<String> ancestorGenreDescriptions) {
		this.ancestorGenreDescriptions = ancestorGenreDescriptions;
	}

	/**
	 * @return the ancestorGenreDescriptions
	 */
	public List<String> getAncestorGenreDescriptions() {
		return ancestorGenreDescriptions;
	}

	/**
	 * @return the image_url
	 */
	public String getImage_url() {
		return image_url;
	}

	/**
	 * @param image_url the image_url to set
	 */
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	/**
	 * @return the genre_parent_name
	 */
	public String getGenre_parent_name() {
		return genre_parent_name;
	}

	/**
	 * @param genre_parent_name the genre_parent_name to set
	 */
	public void setGenre_parent_name(String genre_parent_name) {
		this.genre_parent_name = genre_parent_name;
	}

	/**
	 * @return the title_en_US
	 */
	public String getTitle_en_US() {
		return title_en_US;
	}

	/**
	 * @param title_en_US the title_en_US to set
	 */
	public void setTitle_en_US(String title_en_US) {
		this.title_en_US = title_en_US;
	}


}
