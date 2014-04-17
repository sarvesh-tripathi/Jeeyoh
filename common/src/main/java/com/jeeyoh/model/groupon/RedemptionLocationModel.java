package com.jeeyoh.model.groupon;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class RedemptionLocationModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private String streetAddress1;
	
	@JsonProperty
	private String state;
	
	@JsonProperty
	private String streetAddress2;
	
	@JsonProperty
	private String city;
	
	@JsonProperty	
	private String postalCode;
	
	@JsonProperty
	private String name;
	
	@JsonProperty("lat")
	private String lattitude;
	
	@JsonProperty
	private String phoneNumber;
	
	@JsonProperty("lng")
	private String longitude;

	public String getStreetAddress1() {
		return streetAddress1;
	}

	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreetAddress2() {
		return streetAddress2;
	}

	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("lat")
	public String getLattitude() {
		return lattitude;
	}

	@JsonProperty("lat")
	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@JsonProperty("lng")
	public String getLongitude() {
		return longitude;
	}

	@JsonProperty("lng")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	

}
