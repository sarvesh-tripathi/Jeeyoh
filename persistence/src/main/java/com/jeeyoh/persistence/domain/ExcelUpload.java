package com.jeeyoh.persistence.domain;

public class ExcelUpload implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String eventType;
	private String description;
	private String place;
	private String zipCode;
	private String OriginalPrice;
	private String discountedPrice;
	private String pictureUrl;
	private String validityStartDate;
	private String validityEndDate;
	private String validityTiming;
	private String ifDeal;
	private String highlight;
	private String finePrints;
	private String offerDetails;
	private String vendorDealProvider;
	private String addressVendor;
	private String contactVendor;
	private String paymentOption;
	private String offers;
	private String uploadByUsername;
	private String timeStamp;
	private String changeUserUpdate;
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getOriginalPrice() {
		return OriginalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		OriginalPrice = originalPrice;
	}
	public String getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(String discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getValidityStartDate() {
		return validityStartDate;
	}
	public void setValidityStartDate(String validityStartDate) {
		this.validityStartDate = validityStartDate;
	}
	public String getValidityEndDate() {
		return validityEndDate;
	}
	public void setValidityEndDate(String validityEndDate) {
		this.validityEndDate = validityEndDate;
	}
	public String getValidityTiming() {
		return validityTiming;
	}
	public void setValidityTiming(String validityTiming) {
		this.validityTiming = validityTiming;
	}
	public String getIfDeal() {
		return ifDeal;
	}
	public void setIfDeal(String ifDeal) {
		this.ifDeal = ifDeal;
	}
	public String getHighlight() {
		return highlight;
	}
	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
	public String getFinePrints() {
		return finePrints;
	}
	public void setFinePrints(String finePrints) {
		this.finePrints = finePrints;
	}
	public String getOfferDetails() {
		return offerDetails;
	}
	public void setOfferDetails(String offerDetails) {
		this.offerDetails = offerDetails;
	}
	public String getVendorDealProvider() {
		return vendorDealProvider;
	}
	public void setVendorDealProvider(String vendorDealProvider) {
		this.vendorDealProvider = vendorDealProvider;
	}
	public String getAddressVendor() {
		return addressVendor;
	}
	public void setAddressVendor(String addressVendor) {
		this.addressVendor = addressVendor;
	}
	public String getContactVendor() {
		return contactVendor;
	}
	public void setContactVendor(String contactVendor) {
		this.contactVendor = contactVendor;
	}
	public String getPaymentOption() {
		return paymentOption;
	}
	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}
	public String getOffers() {
		return offers;
	}
	public void setOffers(String offers) {
		this.offers = offers;
	}
	public String getUploadByUsername() {
		return uploadByUsername;
	}
	public void setUploadByUsername(String uploadByUsername) {
		this.uploadByUsername = uploadByUsername;
	}
	public String getChangeUserUpdate() {
		return changeUserUpdate;
	}
	public void setChangeUserUpdate(String changeUserUpdate) {
		this.changeUserUpdate = changeUserUpdate;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
}
