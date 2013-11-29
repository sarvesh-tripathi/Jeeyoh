package com.jeeyoh.model.yelp;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpBusinessResponse extends YelpBusiness implements Serializable {
	private static final long serialVersionUID = 1L;	

	@JsonProperty("menu_provider")
	private String menuProvider;
	
	@JsonProperty("menu_date_updated")
	private String menuUpdatedDate;
	
	private List<YelpDeal> deals;
	
	@JsonProperty("gift_certificates")
	private List<YelpGiftCertificate> giftCertificats;
	
	private List<YelpReview> reviews;

	@JsonProperty("menu_provider")
	public String getMenuProvider() {
		return menuProvider;
	}

	@JsonProperty("menu_provider")
	public void setMenuProvider(String menuProvider) {
		this.menuProvider = menuProvider;
	}

	@JsonProperty("menu_date_updated")
	public String getMenuUpdatedDate() {
		return menuUpdatedDate;
	}

	@JsonProperty("menu_date_updated")
	public void setMenuUpdatedDate(String menuUpdatedDate) {
		this.menuUpdatedDate = menuUpdatedDate;
	}

	public List<YelpDeal> getDeals() {
		return deals;
	}

	public void setDeals(List<YelpDeal> deals) {
		this.deals = deals;
	}

	@JsonProperty("gift_certificates")
	public List<YelpGiftCertificate> getGiftCertificats() {
		return giftCertificats;
	}

	@JsonProperty("gift_certificates")
	public void setGiftCertificats(List<YelpGiftCertificate> giftCertificats) {
		this.giftCertificats = giftCertificats;
	}

	public List<YelpReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<YelpReview> reviews) {
		this.reviews = reviews;
	}
}
