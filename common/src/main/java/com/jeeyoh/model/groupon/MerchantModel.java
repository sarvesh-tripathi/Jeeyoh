package com.jeeyoh.model.groupon;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class MerchantModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private String websiteUrl;
	
	@JsonProperty("id")
	private String merchantId;
	
	@JsonProperty
	private List<RatingModel> ratings;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	@JsonProperty("id")
	public String getMerchantId() {
		return merchantId;
	}

	@JsonProperty("id")
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @param ratings the ratings to set
	 */
	public void setRatings(List<RatingModel> ratings) {
		this.ratings = ratings;
	}

	/**
	 * @return the ratings
	 */
	public List<RatingModel> getRatings() {
		return ratings;
	}
}
