package com.jeeyoh.model.yelp;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpDealOption implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("formatted_original_price")
	private String formattedOrgPrice;
	
	@JsonProperty("formatted_price")
	private String formattedPrice;
	
	@JsonProperty("is_quantity_limited")
	private boolean isLimitedQuantity;
	
	@JsonProperty("original_price")
	private long orgPrice;
	
	private long price;
	
	@JsonProperty("purchase_url")
	private String purchaseUrl;
	
	@JsonProperty("remaining_count")
	private long remainingCount;
	
	private String title;

	@JsonProperty("formatted_original_price")
	public String getFormattedOrgPrice() {
		return formattedOrgPrice;
	}

	@JsonProperty("formatted_original_price")
	public void setFormattedOrgPrice(String formattedOrgPrice) {
		this.formattedOrgPrice = formattedOrgPrice;
	}

	@JsonProperty("formatted_price")
	public String getFormattedPrice() {
		return formattedPrice;
	}

	@JsonProperty("formatted_price")
	public void setFormattedPrice(String formattedPrice) {
		this.formattedPrice = formattedPrice;
	}

	@JsonProperty("is_quantity_limited")
	public boolean isLimitedQuantity() {
		return isLimitedQuantity;
	}

	@JsonProperty("is_quantity_limited")
	public void setLimitedQuantity(boolean isLimitedQuantity) {
		this.isLimitedQuantity = isLimitedQuantity;
	}

	@JsonProperty("original_price")
	public long getOrgPrice() {
		return orgPrice;
	}

	@JsonProperty("original_price")
	public void setOrgPrice(long orgPrice) {
		this.orgPrice = orgPrice;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	@JsonProperty("purchase_url")
	public String getPurchaseUrl() {
		return purchaseUrl;
	}

	@JsonProperty("purchase_url")
	public void setPurchaseUrl(String purchaseUrl) {
		this.purchaseUrl = purchaseUrl;
	}

	@JsonProperty("remaining_count")
	public long getRemainingCount() {
		return remainingCount;
	}

	@JsonProperty("remaining_count")
	public void setRemainingCount(long remainingCount) {
		this.remainingCount = remainingCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
