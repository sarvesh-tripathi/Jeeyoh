package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Gmerchantrating implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Double rating;
	private String url;
	private String linkText;
	private String ratingId;
	private int reviewsCount;
	private Set gmerchantByRatingId = new HashSet(0);
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Double rating) {
		this.rating = rating;
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
	 * @return the linkText
	 */
	public String getLinkText() {
		return linkText;
	}
	/**
	 * @param linkText the linkText to set
	 */
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}
	/**
	 * @return the ratingId
	 */
	public String getRatingId() {
		return ratingId;
	}
	/**
	 * @param ratingId the ratingId to set
	 */
	public void setRatingId(String ratingId) {
		this.ratingId = ratingId;
	}
	/**
	 * @return the reviewsCount
	 */
	public int getReviewsCount() {
		return reviewsCount;
	}
	/**
	 * @param reviewsCount the reviewsCount to set
	 */
	public void setReviewsCount(int reviewsCount) {
		this.reviewsCount = reviewsCount;
	}
	/**
	 * @param gmerchantByRatingId the gmerchantByRatingId to set
	 */
	public void setGmerchantByRatingId(Set gmerchantByRatingId) {
		this.gmerchantByRatingId = gmerchantByRatingId;
	}
	/**
	 * @return the gmerchantByRatingId
	 */
	public Set getGmerchantByRatingId() {
		return gmerchantByRatingId;
	}
	
}
