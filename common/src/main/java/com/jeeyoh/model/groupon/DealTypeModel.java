package com.jeeyoh.model.groupon;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class DealTypeModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String description;
	private String name;
	@JsonProperty("id")
	private String dealTypeId;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonProperty("id")
	public String getDealTypeId() {
		return dealTypeId;
	}
	@JsonProperty("id")
	public void setDealTypeId(String dealTypeId) {
		this.dealTypeId = dealTypeId;
	}
	

}
