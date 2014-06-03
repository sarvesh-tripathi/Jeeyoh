package com.jeeyoh.persistence.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Ldeal implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String dealId;
	private String dealType;
	private String dealUrl;
	private String shortTitle;
	private String longTitle;
	private String merchantName;
	private Date startAt;
	private Date endAt;
	private String cityName;
	private String smallImageUrl;
	private String mediumImageUrl;
	private String largeImageUrl;
	private Long price;
	private String countryName;
	private Long distance;
	private Boolean isSoldOut;
	private Integer orderCount;
	private Set dealOptions = new HashSet(0);
	private Set ldealCategory = new HashSet(0);
	
	
	public Ldeal(){}
	public Ldeal(String dealId, String dealType, String dealUrl, String shortTitle, String longTitle, String merchantName, Date startAt, Date endAt, String cityName, String smallImageUrl, String mediumImageUrl, String largeImageUrl, Long price, String countryName, Long distance, Boolean isSoldOut, Integer orderCount){
		this.dealId =  dealId;
		this.dealType = dealType;
		this.dealUrl = dealUrl;
		this.shortTitle = shortTitle;
		this.longTitle = longTitle;
		this.merchantName = merchantName;
		this.startAt = startAt;
		this.endAt = endAt;
		this.cityName = cityName;
		this.smallImageUrl = smallImageUrl;
		this.largeImageUrl = largeImageUrl;
		this.mediumImageUrl = mediumImageUrl;
		this.price = price;
		this.countryName = countryName;
		this.distance = distance;
		this.isSoldOut = isSoldOut;
		this.orderCount = orderCount;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDealId() {
		return dealId;
	}
	public void setDealId(String dealId) {
		this.dealId = dealId;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public String getDealUrl() {
		return dealUrl;
	}
	public void setDealUrl(String dealUrl) {
		this.dealUrl = dealUrl;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getLongTitle() {
		return longTitle;
	}
	public void setLongTitle(String longTitle) {
		this.longTitle = longTitle;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Date getStartAt() {
		return startAt;
	}
	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}
	public Date getEndAt() {
		return endAt;
	}
	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	public String getMediumImageUrl() {
		return mediumImageUrl;
	}
	public void setMediumImageUrl(String mediumImageUrl) {
		this.mediumImageUrl = mediumImageUrl;
	}
	public String getLargeImageUrl() {
		return largeImageUrl;
	}
	public void setLargeImageUrl(String largeImageUrl) {
		this.largeImageUrl = largeImageUrl;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Long getDistance() {
		return distance;
	}
	public void setDistance(Long distance) {
		this.distance = distance;
	}
	public Boolean getIsSoldOut() {
		return isSoldOut;
	}
	public void setIsSoldOut(Boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	public Set getDealOptions() {
		return dealOptions;
	}
	public void setDealOptions(Set dealOptions) {
		this.dealOptions = dealOptions;
	}
	public Set getLdealCategory() {
		return ldealCategory;
	}
	public void setLdealCategory(Set ldealCatagory) {
		this.ldealCategory = ldealCatagory;
	}
	

}
