package com.jeeyoh.model.groupon;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class DivisionModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private String divisionId;
	@JsonProperty
	private String timezoneOffsetInSeconds;
	@JsonProperty
	private String name;
	@JsonProperty("lat")
	private String lattitude;	
	@JsonProperty("lng")
	private String longitude;	
	@JsonProperty
	private String timezone;
	
	@JsonProperty
	private String uuid;
	@JsonProperty
	private boolean isNowCustomerEnabled;
	@JsonProperty
	private boolean isPresenceEnabled;
	@JsonProperty
	private String defaultLocale;
	@JsonProperty
	private String timezoneIdentifier;
	@JsonProperty
	private String country;	
	@JsonProperty
	private boolean isReserveEnabled;
	@JsonProperty
	private boolean isNowMerchantEnabled;
	

	@JsonProperty("id")
	public String getDivisionId() {
		return divisionId;
	}

	@JsonProperty("id")
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getTimezoneOffsetInSeconds() {
		return timezoneOffsetInSeconds;
	}

	public void setTimezoneOffsetInSeconds(String timezoneOffsetInSeconds) {
		this.timezoneOffsetInSeconds = timezoneOffsetInSeconds;
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

	@JsonProperty("lng")
	public String getLongitude() {
		return longitude;
	}

	@JsonProperty("lng")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isNowCustomerEnabled() {
		return isNowCustomerEnabled;
	}

	public void setNowCustomerEnabled(boolean isNowCustomerEnabled) {
		this.isNowCustomerEnabled = isNowCustomerEnabled;
	}

	public boolean isPresenceEnabled() {
		return isPresenceEnabled;
	}

	public void setPresenceEnabled(boolean isPresenceEnabled) {
		this.isPresenceEnabled = isPresenceEnabled;
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public String getTimezoneIdentifier() {
		return timezoneIdentifier;
	}

	public void setTimezoneIdentifier(String timezoneIdentifier) {
		this.timezoneIdentifier = timezoneIdentifier;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isReserveEnabled() {
		return isReserveEnabled;
	}

	public void setReserveEnabled(boolean isReserveEnabled) {
		this.isReserveEnabled = isReserveEnabled;
	}

	public boolean isNowMerchantEnabled() {
		return isNowMerchantEnabled;
	}

	public void setNowMerchantEnabled(boolean isNowMerchantEnabled) {
		this.isNowMerchantEnabled = isNowMerchantEnabled;
	}

}
