package com.jeeyoh.model.yelp;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpSpan implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("latitude_delta")
	private Double lattitudeDelta;
	
	@JsonProperty("longitude_delta")
	private Double longitudeDelta;

	@JsonProperty("latitude_delta")
	public Double getLattitudeDelta() {
		return lattitudeDelta;
	}

	@JsonProperty("latitude_delta")
	public void setLattitudeDelta(Double lattitudeDelta) {
		this.lattitudeDelta = lattitudeDelta;
	}

	@JsonProperty("longitude_delta")
	public Double getLongitudeDelta() {
		return longitudeDelta;
	}

	@JsonProperty("longitude_delta")
	public void setLongitudeDelta(Double longitudeDelta) {
		this.longitudeDelta = longitudeDelta;
	}

}
