package com.jeeyoh.persistence.domain;

import java.util.Date;

public class StubhubEvent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private int id;
	
	private String description;	
	
	private Date event_date;
		
	
	private Date event_date_local;
	
	private Date event_date_time_local;
	
	private String event_time_local;	
	
	private String geography_parent;	
	
	
	private String venue_name;
	
	private String city;
	
	private String state;
	
	private String genreUrlPath;
	
	private String urlpath;
	
	private String leaf;
	
	private String channel;
	
	private String totalTickets;
	
	private String zip;
	
	private String latitude;
	
	private String longitude;	                         
	
  	private String timezone;
  	
	private String	timezone_id;
	
	private String	venue_config_name;
	
	private String	currency_code;
	
	private String	event_config_template_id;
	
	private String	event_config_template;
	
	private String	keywords_en_US;
	
	private double	maxPrice;
	
	private double	minPrice;
	
	private double	maxSeatsTogether;
	
	private double	minSeatsTogether;
	
	private String	title;
	
	private String ancestorGenreDescriptions;
	
	private String image_url;
	
	private String genre_parent_name;
	
	private String ancestorGeoDescriptions;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEvent_date() {
		return event_date;
	}

	public void setEvent_date(Date event_date) {
		this.event_date = event_date;
	}

	public Date getEvent_date_local() {
		return event_date_local;
	}

	public void setEvent_date_local(Date event_date_local) {
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

	public void setTimezone_id(String string) {
		this.timezone_id = string;
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

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public double getMaxSeatsTogether() {
		return maxSeatsTogether;
	}

	public void setMaxSeatsTogether(double maxSeatsTogether) {
		this.maxSeatsTogether = maxSeatsTogether;
	}

	public double getMinSeatsTogether() {
		return minSeatsTogether;
	}

	public void setMinSeatsTogether(double minSeatsTogether) {
		this.minSeatsTogether = minSeatsTogether;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	 * @return the event_date_time_local
	 */
	public Date getEvent_date_time_local() {
		return event_date_time_local;
	}

	/**
	 * @param event_date_time_local the event_date_time_local to set
	 */
	public void setEvent_date_time_local(Date event_date_time_local) {
		this.event_date_time_local = event_date_time_local;
	}

	/**
	 * @return the ancestorGeoDescriptions
	 */
	public String getAncestorGeoDescriptions() {
		return ancestorGeoDescriptions;
	}

	/**
	 * @param ancestorGeoDescriptions the ancestorGeoDescriptions to set
	 */
	public void setAncestorGeoDescriptions(String ancestorGeoDescriptions) {
		this.ancestorGeoDescriptions = ancestorGeoDescriptions;
	}

}
