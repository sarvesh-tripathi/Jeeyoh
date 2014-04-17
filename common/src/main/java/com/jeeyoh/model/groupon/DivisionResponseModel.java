package com.jeeyoh.model.groupon;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class DivisionResponseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<DivisionModel> divisions;
	public List<DivisionModel> getDivisions() {
		return divisions;
	}
	public void setDivisions(List<DivisionModel> divisions) {
		this.divisions = divisions;
	}

}
