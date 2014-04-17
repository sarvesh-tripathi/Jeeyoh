package com.jeeyoh.model.livingsocial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class LdealModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("id")
	private Integer dealId;
	@JsonProperty("deal_type")
	private String dealType;
	@JsonProperty("url")
	private String dealUrl;
	@JsonProperty("short_title")
	private String shortTitle;
	@JsonProperty("long_title")
	private String longTitle;
	@JsonProperty("merchant_name")
	private String merchantName;
	@JsonProperty("offer_starts_at")
	private Date startAt;
	@JsonProperty("offer_ends_at")
	private Date endAt;
	@JsonProperty("city_ids")
	private ArrayList<Integer> cityId;
	@JsonProperty("images")
	private ArrayList<ImagesModel> images;
	@JsonProperty("price")
	private Long price;
	@JsonProperty("country_id")
	private int countryId;
	@JsonProperty("distance")
	private Long distance;
	@JsonProperty("sold_out")
	private Boolean isSoldOut;
	@JsonProperty("orders_count")
	private Integer orderCount;
	@JsonProperty("options")
	private ArrayList<LdealOptionModel> ldealOptions;
	@JsonProperty("categories")
	private ArrayList<String> categoryName;
	
	
	@JsonProperty("id")
	public Integer getDealId() {
		return dealId;
	}
	@JsonProperty("id")
	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}
	@JsonProperty("deal_type")
	public String getDealType() {
		return dealType;
	}
	@JsonProperty("deal_type")
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	@JsonProperty("url")
	public String getDealUrl() {
		return dealUrl;
	}
	@JsonProperty("url")
	public void setDealUrl(String dealUrl) {
		this.dealUrl = dealUrl;
	}
	@JsonProperty("short_title")
	public String getShortTitle() {
		return shortTitle;
	}
	@JsonProperty("short_title")
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	@JsonProperty("long_title")
	public String getLongTitle() {
		return longTitle;
	}
	@JsonProperty("long_title")
	public void setLongTitle(String longTitle) {
		this.longTitle = longTitle;
	}
	@JsonProperty("merchant_name")
	public String getMerchantName() {
		return merchantName;
	}
	@JsonProperty("merchant_name")
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	@JsonProperty("offer_starts_at")
	public Date getStartAt() {
		return startAt;
	}
	@JsonProperty("offer_starts_at")
	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}
	@JsonProperty("offer_ends_at")
	public Date getEndAt() {
		return endAt;
	}
	@JsonProperty("offer_ends_at")
	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}
	@JsonProperty("city_ids")
	public ArrayList<Integer> getCityId() {
		return cityId;
	}
	@JsonProperty("city_ids")
	public void setCityId(ArrayList<Integer> cityId) {
		this.cityId = cityId;
	}
	@JsonProperty("images")
	public ArrayList<ImagesModel> getImages() {
		return images;
	}
	@JsonProperty("images")
	public void setImages(ArrayList<ImagesModel> images) {
		this.images = images;
	}
	@JsonProperty("price")
	public Long getPrice() {
		return price;
	}
	@JsonProperty("price")
	public void setPrice(Long price) {
		this.price = price;
	}
	@JsonProperty("country_id")
	public int getCountryId() {
		return countryId;
	}
	@JsonProperty("country_id")
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	@JsonProperty("distance")
	public Long getDistance() {
		return distance;
	}
	@JsonProperty("distance")
	public void setDistance(Long distance) {
		this.distance = distance;
	}
	@JsonProperty("sold_out")
	public Boolean getIsSoldOut() {
		return isSoldOut;
	}
	@JsonProperty("sold_out")
	public void setIsSoldOut(Boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}
	@JsonProperty("orders_count")
	public Integer getOrderCount() {
		return orderCount;
	}
	@JsonProperty("orders_count")
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	@JsonProperty("options")
	public ArrayList<LdealOptionModel> getLdealOptions() {
		return ldealOptions;
	}
	@JsonProperty("options")
	public void setLdealOptions(ArrayList<LdealOptionModel> ldealOptions) {
		this.ldealOptions = ldealOptions;
	}
	@JsonProperty("categories")
	public ArrayList<String> getCategoryName() {
		return categoryName;
	}
	@JsonProperty("categories")
	public void setCategoryName(ArrayList<String> categoryName) {
		this.categoryName = categoryName;
	}
	
	

}
