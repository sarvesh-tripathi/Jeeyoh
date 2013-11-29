package com.jeeyoh.model.yelp;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpLocation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("cross_streets")
	private String crossStreets;
	
	private String city;
	
	@JsonProperty("display_address")
	private List<String> displayAddress;	
	
	private List<String> neighborhoods;
	
	@JsonProperty("postal_code")
	private String postalCode;
	
	@JsonProperty("country_code")
	private String countryCode;
	
	private List<String> address;
	
	@JsonProperty("state_code")
	private String stateCode;

	@JsonProperty("cross_streets")
	public String getCrossStreets() {
		return crossStreets;
	}

	@JsonProperty("cross_streets")
	public void setCrossStreets(String crossStreets) {
		this.crossStreets = crossStreets;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("display_address")
	public List<String> getDisplayAddress() {
		return displayAddress;
	}

	@JsonProperty("display_address")
	public void setDisplayAddress(List<String> displayAddress) {
		this.displayAddress = displayAddress;
	}

	public List<String> getNeighborhoods() {
		return neighborhoods;
	}

	public void setNeighborhoods(List<String> neighborhoods) {
		this.neighborhoods = neighborhoods;
	}

	@JsonProperty("postal_code")
	public String getPostalCode() {
		return postalCode;
	}

	@JsonProperty("postal_code")
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@JsonProperty("country_code")
	public String getCountryCode() {
		return countryCode;
	}

	@JsonProperty("country_code")
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	@JsonProperty("state_code")
	public String getStateCode() {
		return stateCode;
	}

	@JsonProperty("state_code")
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

}
