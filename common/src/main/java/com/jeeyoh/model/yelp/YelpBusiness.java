package com.jeeyoh.model.yelp;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpBusiness implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("is_claimed")
	private boolean isClaimed;
	
	private double rating;
	
	@JsonProperty("mobile_url")
	private String mobileUrl;
	
	@JsonProperty("rating_img_url")
	private String ratingImageUrl;
	
	@JsonProperty("review_count")
	private int reviewCount;
	
	private String name;
	
	@JsonProperty("snippet_image_url")
	private String snippetImageUrl;
	
	@JsonProperty("rating_img_url_small")
	private String ratingSmallImageUrl;
	
	private String url;
	
	private String phone;
	
	@JsonProperty("snippet_text")
	private String snippetText;
	
	@JsonProperty("image_url")
	private String imageUrl;
	
	private List<List<String>> categories;
	
	@JsonProperty("display_phone")
	private String displayPhone;
	
	@JsonProperty("rating_img_url_large")
	private String ratingLargeImageUrl;
	
	@JsonProperty("id")
	private String businessId;
	
	@JsonProperty("is_closed")
	private boolean isClosed;
	
	private YelpLocation location;

	@JsonProperty("is_claimed")
	public boolean isClaimed() {
		return isClaimed;
	}

	@JsonProperty("is_claimed")
	public void setClaimed(boolean isClaimed) {
		this.isClaimed = isClaimed;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	@JsonProperty("mobile_url")
	public String getMobileUrl() {
		return mobileUrl;
	}

	@JsonProperty("mobile_url")
	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	@JsonProperty("rating_img_url")
	public String getRatingImageUrl() {
		return ratingImageUrl;
	}

	@JsonProperty("rating_img_url")
	public void setRatingImageUrl(String ratingImageUrl) {
		this.ratingImageUrl = ratingImageUrl;
	}

	@JsonProperty("review_count")
	public int getReviewCount() {
		return reviewCount;
	}

	@JsonProperty("review_count")
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("snippet_image_url")
	public String getSnippetImageUrl() {
		return snippetImageUrl;
	}

	@JsonProperty("snippet_image_url")
	public void setSnippetImageUrl(String snippetImageUrl) {
		this.snippetImageUrl = snippetImageUrl;
	}

	@JsonProperty("rating_img_url_small")
	public String getRatingSmallImageUrl() {
		return ratingSmallImageUrl;
	}

	@JsonProperty("rating_img_url_small")
	public void setRatingSmallImageUrl(String ratingSmallImageUrl) {
		this.ratingSmallImageUrl = ratingSmallImageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonProperty("snippet_text")
	public String getSnippetText() {
		return snippetText;
	}

	@JsonProperty("snippet_text")
	public void setSnippetText(String snippetText) {
		this.snippetText = snippetText;
	}

	@JsonProperty("image_url")
	public String getImageUrl() {
		return imageUrl;
	}

	@JsonProperty("image_url")
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<List<String>> getCategories() {
		return categories;
	}

	public void setCategories(List<List<String>> categories) {
		this.categories = categories;
	}

	@JsonProperty("display_phone")
	public String getDisplayPhone() {
		return displayPhone;
	}

	@JsonProperty("display_phone")
	public void setDisplayPhone(String displayPhone) {
		this.displayPhone = displayPhone;
	}

	@JsonProperty("rating_img_url_large")
	public String getRatingLargeImageUrl() {
		return ratingLargeImageUrl;
	}

	@JsonProperty("rating_img_url_large")
	public void setRatingLargeImageUrl(String ratingLargeImageUrl) {
		this.ratingLargeImageUrl = ratingLargeImageUrl;
	}

	@JsonProperty("id")
	public String getBusinessId() {
		return businessId;
	}

	@JsonProperty("id")
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@JsonProperty("is_closed")
	public boolean isClosed() {
		return isClosed;
	}

	@JsonProperty("is_closed")
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	public YelpLocation getLocation() {
		return location;
	}

	public void setLocation(YelpLocation location) {
		this.location = location;
	} 
}
