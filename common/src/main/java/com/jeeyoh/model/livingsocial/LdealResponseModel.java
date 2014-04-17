package com.jeeyoh.model.livingsocial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class LdealResponseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("deals")
	private List<LdealModel> ldeal;
	@JsonProperty("deals")
	public List<LdealModel> getLdeal() {
		return ldeal;
	}
	@JsonProperty("deals")
	public void setLdeal(List<LdealModel> ldeal) {
		this.ldeal = ldeal;
	}
	
}
