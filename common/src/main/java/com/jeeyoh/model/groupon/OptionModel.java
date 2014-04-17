package com.jeeyoh.model.groupon;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class OptionModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private String optionId;
	
	@JsonProperty
	private String title;
	
	@JsonProperty
	private int soldQuantity;
	
	@JsonProperty
	private boolean isSoldOut;
	
	@JsonProperty
	private PriceModel price;
	
	@JsonProperty
	private PriceModel value;
	
	@JsonProperty
	private PriceModel discount;
	
	@JsonProperty
	private int discountPercent;
	
	@JsonProperty
	private boolean isLimitedQuantity;
	
	@JsonProperty 
	private int initialQuantity;
	
	@JsonProperty
	private int remainingQuantity;
	
	@JsonProperty
	private int minimumPurchaseQuantity;
	
	@JsonProperty
	private int maximumPurchaseQuantity;
	
	@JsonProperty
	private String expiresAt;
	
	@JsonProperty
	private List<DetailModel> details;
	
	@JsonProperty
	private List<RedemptionLocationModel> redemptionLocations;
	
	@JsonProperty
	private String externalUrl;
	
	@JsonProperty
	private List<Object> customFields;
	
	@JsonProperty
	private String buyUrl;

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSoldQuantity() {
		return soldQuantity;
	}

	public void setSoldQuantity(int soldQuantity) {
		this.soldQuantity = soldQuantity;
	}

	public boolean isSoldOut() {
		return isSoldOut;
	}

	public void setSoldOut(boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}

	public PriceModel getPrice() {
		return price;
	}

	public void setPrice(PriceModel price) {
		this.price = price;
	}

	public PriceModel getValue() {
		return value;
	}

	public void setValue(PriceModel value) {
		this.value = value;
	}

	public PriceModel getDiscount() {
		return discount;
	}

	public void setDiscount(PriceModel discount) {
		this.discount = discount;
	}

	public int getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(int discountPercent) {
		this.discountPercent = discountPercent;
	}

	public boolean isLimitedQuantity() {
		return isLimitedQuantity;
	}

	public void setLimitedQuantity(boolean isLimitedQuantity) {
		this.isLimitedQuantity = isLimitedQuantity;
	}

	public int getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(int initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public int getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(int remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	public int getMinimumPurchaseQuantity() {
		return minimumPurchaseQuantity;
	}

	public void setMinimumPurchaseQuantity(int minimumPurchaseQuantity) {
		this.minimumPurchaseQuantity = minimumPurchaseQuantity;
	}

	public int getMaximumPurchaseQuantity() {
		return maximumPurchaseQuantity;
	}

	public void setMaximumPurchaseQuantity(int maximumPurchaseQuantity) {
		this.maximumPurchaseQuantity = maximumPurchaseQuantity;
	}

	public String getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

	public List<DetailModel> getDetails() {
		return details;
	}

	public void setDetails(List<DetailModel> details) {
		this.details = details;
	}

	public List<RedemptionLocationModel> getRedemptionLocations() {
		return redemptionLocations;
	}

	public void setRedemptionLocations(
			List<RedemptionLocationModel> redemptionLocations) {
		this.redemptionLocations = redemptionLocations;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public List<Object> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<Object> customFields) {
		this.customFields = customFields;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}
	
	
	
	
	

}
