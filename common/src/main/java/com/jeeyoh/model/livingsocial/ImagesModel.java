package com.jeeyoh.model.livingsocial;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class ImagesModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("size220")
	private String smallImageUrl;
	@JsonProperty("size360")
	private String mediumImageUrl;
	@JsonProperty("size700")
	private String largeImageUrl;
	
	@JsonProperty("size220")
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	@JsonProperty("size220")
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	@JsonProperty("size360")
	public String getMediumImageUrl() {
		return mediumImageUrl;
	}
	@JsonProperty("size360")
	public void setMediumImageUrl(String mediumImageUrl) {
		this.mediumImageUrl = mediumImageUrl;
	}
	@JsonProperty("size700")
	public String getLargeImageUrl() {
		return largeImageUrl;
	}
	@JsonProperty("size700")
	public void setLargeImageUrl(String largeImageUrl) {
		this.largeImageUrl = largeImageUrl;
	}
	

}
