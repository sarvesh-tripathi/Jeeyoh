package com.jeeyoh.model.groupon;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class AreaModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private String areaId;

	@JsonProperty("id")
	public String getAreaId() {
		return areaId;
	}

	@JsonProperty("id")
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	
	

}
