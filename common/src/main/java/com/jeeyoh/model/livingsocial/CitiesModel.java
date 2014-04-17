package com.jeeyoh.model.livingsocial;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CitiesModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private int cityId;
	@JsonProperty("name")
	private String cityName;
	@JsonProperty("countryName")
	private String countryName;
	@JsonProperty("countryId")
	private int countryId;
	@JsonProperty("latitude")
	private String latitude;
	@JsonProperty("longitude")
	private String longitude;
	@JsonProperty("state")
	private String stateCode;
	@JsonProperty("backgroundImageUrl")
	private String backgroundImageUrl;
	
	
	@JsonProperty("id")
	public int getCityId() {
		return cityId;
	}
	@JsonProperty("id")
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	@JsonProperty("name")
	public String getCityName() {
		return cityName;
	}
	@JsonProperty("name")
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@JsonProperty("countryName")
	public String getCountryName() {
		return countryName;
	}
	@JsonProperty("countryName")
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	@JsonProperty("countryId")
	public int getCountryId() {
		return countryId;
	}
	@JsonProperty("countryId")
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	@JsonProperty("latitude")
	public String getLatitude() {
		return latitude;
	}
	@JsonProperty("latitude")
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@JsonProperty("longitude")
	public String getLongitude() {
		return longitude;
	}
	@JsonProperty("longitude")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	@JsonProperty("state")
	public String getStateCode() {
		return stateCode;
	}
	@JsonProperty("state")
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	@JsonProperty("backgroundImageUrl")
	public String getBackgroundImageUrl() {
		return backgroundImageUrl;
	}
	@JsonProperty("backgroundImageUrl")
	public void setBackgroundImageUrl(String backgroundImageUrl) {
		this.backgroundImageUrl = backgroundImageUrl;
	}
	
}
