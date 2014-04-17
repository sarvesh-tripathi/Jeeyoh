package com.jeeyoh.model.yelp;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpDeal implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("currency_code")
	private String currencyCode;
	
	@JsonProperty("image_url")
	private String imageUrl;
	
	private String url;
	
	@JsonProperty("is_popular")
	private boolean isPopular;
	
	@JsonProperty("time_start")
	private long startTime;
	
	private String title;
	
	private List<YelpDealOption> options;	

	@JsonProperty("currency_code")
	public String getCurrencyCode() {
		return currencyCode;
	}

	@JsonProperty("currency_code")
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@JsonProperty("image_url")
	public String getImageUrl() {
		return imageUrl;
	}

	@JsonProperty("image_url")
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty("is_popular")
	public boolean isPopular() {
		return isPopular;
	}

	@JsonProperty("is_popular")
	public void setPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}

	@JsonProperty("time_start")
	public long getStartTime() {
		return startTime;
	}

	@JsonProperty("time_start")
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<YelpDealOption> getOptions() {
		return options;
	}

	public void setOptions(List<YelpDealOption> options) {
		this.options = options;
	}

}
