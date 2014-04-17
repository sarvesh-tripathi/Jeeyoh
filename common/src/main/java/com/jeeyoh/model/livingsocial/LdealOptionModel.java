package com.jeeyoh.model.livingsocial;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class LdealOptionModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("description")
	private String description;
	@JsonProperty("price")
	private Long price;
	@JsonProperty("original_price")
	private Long originalPrice;
	@JsonProperty("sold_out")
	private Boolean isSoldOut;
	@JsonProperty("savings")
	private Long savings;
	@JsonProperty("discount")
	private Integer discount;
	@JsonProperty("monthly_cap_reached")
	private Boolean monthlyCapReached;
	
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonProperty("price")
	public Long getPrice() {
		return price;
	}
	@JsonProperty("price")
	public void setPrice(Long price) {
		this.price = price;
	}
	@JsonProperty("original_price")
	public Long getOriginalPrice() {
		return originalPrice;
	}
	@JsonProperty("original_price")
	public void setOriginalPrice(Long originalPrice) {
		this.originalPrice = originalPrice;
	}
	@JsonProperty("sold_out")
	public Boolean getIsSoldOut() {
		return isSoldOut;
	}
	@JsonProperty("sold_out")
	public void setIsSoldOut(Boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}
	@JsonProperty("savings")
	public Long getSavings() {
		return savings;
	}
	@JsonProperty("savings")
	public void setSavings(Long savings) {
		this.savings = savings;
	}
	@JsonProperty("discount")
	public Integer getDiscount() {
		return discount;
	}
	@JsonProperty("discount")
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	@JsonProperty("monthly_cap_reached")
	public Boolean getMonthlyCapReached() {
		return monthlyCapReached;
	}
	@JsonProperty("monthly_cap_reached")
	public void setMonthlyCapReached(Boolean monthlyCapReached) {
		this.monthlyCapReached = monthlyCapReached;
	}
	
	

}
