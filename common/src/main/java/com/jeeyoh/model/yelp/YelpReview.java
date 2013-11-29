package com.jeeyoh.model.yelp;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpReview implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String excerpt;
	private String id;
	private long rating;
	
	@JsonProperty("rating_image_large_url")
	private String ratingLargeImgUrl;
	
	@JsonProperty("rating_image_small_url")
	private String ratingSmallImgUrl;
	
	@JsonProperty("rating_image_url")
	private String ratingImgUrl;
	
	@JsonProperty("time_created")
	private long createdTime;
	
	private YelpUser user;

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getRating() {
		return rating;
	}

	public void setRating(long rating) {
		this.rating = rating;
	}

	@JsonProperty("rating_image_large_url")
	public String getRatingLargeImgUrl() {
		return ratingLargeImgUrl;
	}

	@JsonProperty("rating_image_large_url")
	public void setRatingLargeImgUrl(String ratingLargeImgUrl) {
		this.ratingLargeImgUrl = ratingLargeImgUrl;
	}

	@JsonProperty("rating_image_small_url")
	public String getRatingSmallImgUrl() {
		return ratingSmallImgUrl;
	}

	@JsonProperty("rating_image_small_url")
	public void setRatingSmallImgUrl(String ratingSmallImgUrl) {
		this.ratingSmallImgUrl = ratingSmallImgUrl;
	}

	@JsonProperty("rating_image_url")
	public String getRatingImgUrl() {
		return ratingImgUrl;
	}

	@JsonProperty("rating_image_url")
	public void setRatingImgUrl(String ratingImgUrl) {
		this.ratingImgUrl = ratingImgUrl;
	}

	@JsonProperty("time_created")
	public long getCreatedTime() {
		return createdTime;
	}

	@JsonProperty("time_created")
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public YelpUser getUser() {
		return user;
	}

	public void setUser(YelpUser user) {
		this.user = user;
	}

}
