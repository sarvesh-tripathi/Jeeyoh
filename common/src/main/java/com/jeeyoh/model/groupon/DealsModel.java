package com.jeeyoh.model.groupon;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class DealsModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private String dealId;
	@JsonProperty
	private String title;
	
	@JsonProperty
	private DivisionModel division;
	@JsonProperty
	private List<AreaModel> areas;
	
	@JsonProperty
	private String placementPriority;
	
	@JsonProperty
	private String sidebarImageUrl;
	
	@JsonProperty
	private String smallImageUrl;
	
	@JsonProperty
	private String mediumImageUrl;
	
	@JsonProperty
	private String largeImageUrl;
	
	@JsonProperty
	private List<CommentModel> says;
	
	private List<DealTypeModel> dealTypes;
	
	@JsonProperty
	private String announcementTitle;
	
	@JsonProperty
	private List<TagModel> tags;
	
	@JsonProperty
	private String dealUrl;
	
	@JsonProperty
	private String status;
	
	@JsonProperty
	private boolean isTipped;
	
	@JsonProperty
	private int tippingPoint;
	
	@JsonProperty
	private boolean isSoldOut;
	
	@JsonProperty
	private long soldQuantity;
	
	@JsonProperty
	private boolean shippingAddressRequired;
	
	@JsonProperty
	private List<OptionModel> options;
	
	@JsonProperty
	private MerchantModel merchant;
	
	@JsonProperty
	private String highlightsHtml;
	
	@JsonProperty
	private String pitchHtml;
	
	@JsonProperty
	private AddModel textAd;
	
	@JsonProperty
	private String type;
	
	@JsonProperty
	private String startAt;
	
	@JsonProperty
	private String endAt;
	
	@JsonProperty
	private boolean isNowDeal;
	
	@JsonProperty
	private String tippedAt;

	@JsonProperty("id")
	public String getDealId() {
		return dealId;
	}

	@JsonProperty("id")
	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DivisionModel getDivision() {
		return division;
	}

	public void setDivision(DivisionModel division) {
		this.division = division;
	}

	public List<AreaModel> getAreas() {
		return areas;
	}

	public void setAreas(List<AreaModel> areas) {
		this.areas = areas;
	}

	public String getPlacementPriority() {
		return placementPriority;
	}

	public void setPlacementPriority(String placementPriority) {
		this.placementPriority = placementPriority;
	}

	public String getSidebarImageUrl() {
		return sidebarImageUrl;
	}

	public void setSidebarImageUrl(String sidebarImageUrl) {
		this.sidebarImageUrl = sidebarImageUrl;
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

	public List<CommentModel> getSays() {
		return says;
	}

	public void setSays(List<CommentModel> says) {
		this.says = says;
	}

	public String getAnnouncementTitle() {
		return announcementTitle;
	}

	public void setAnnouncementTitle(String announcementTitle) {
		this.announcementTitle = announcementTitle;
	}

	public List<TagModel> getTags() {
		return tags;
	}

	public void setTags(List<TagModel> tags) {
		this.tags = tags;
	}

	public String getDealUrl() {
		return dealUrl;
	}

	public void setDealUrl(String dealUrl) {
		this.dealUrl = dealUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isTipped() {
		return isTipped;
	}

	public void setTipped(boolean isTipped) {
		this.isTipped = isTipped;
	}

	public int getTippingPoint() {
		return tippingPoint;
	}

	public void setTippingPoint(int tippingPoint) {
		this.tippingPoint = tippingPoint;
	}

	public boolean isSoldOut() {
		return isSoldOut;
	}

	public void setSoldOut(boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}

	public long getSoldQuantity() {
		return soldQuantity;
	}

	public void setSoldQuantity(long soldQuantity) {
		this.soldQuantity = soldQuantity;
	}

	public boolean isShippingAddressRequired() {
		return shippingAddressRequired;
	}

	public void setShippingAddressRequired(boolean shippingAddressRequired) {
		this.shippingAddressRequired = shippingAddressRequired;
	}

	public List<OptionModel> getOptions() {
		return options;
	}

	public void setOptions(List<OptionModel> options) {
		this.options = options;
	}

	public MerchantModel getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantModel merchant) {
		this.merchant = merchant;
	}

	public String getHighlightsHtml() {
		return highlightsHtml;
	}

	public void setHighlightsHtml(String highlightsHtml) {
		this.highlightsHtml = highlightsHtml;
	}

	public String getPitchHtml() {
		return pitchHtml;
	}

	public void setPitchHtml(String pitchHtml) {
		this.pitchHtml = pitchHtml;
	}

	public AddModel getTextAd() {
		return textAd;
	}

	public void setTextAd(AddModel textAd) {
		this.textAd = textAd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public boolean isNowDeal() {
		return isNowDeal;
	}

	public void setNowDeal(boolean isNowDeal) {
		this.isNowDeal = isNowDeal;
	}

	public String getTippedAt() {
		return tippedAt;
	}

	public void setTippedAt(String tippedAt) {
		this.tippedAt = tippedAt;
	}

	public List<DealTypeModel> getDealTypes() {
		return dealTypes;
	}

	public void setDealTypes(List<DealTypeModel> dealTypes) {
		this.dealTypes = dealTypes;
	}
	

}
