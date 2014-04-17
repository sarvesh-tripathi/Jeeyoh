package com.jeeyoh.model.yelp;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class YelpGiftCertificate extends YelpDeal implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	@JsonProperty("unused_balance")
	private String unusedBalance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("unused_balance")
	public String getUnusedBalance() {
		return unusedBalance;
	}

	@JsonProperty("unused_balance")
	public void setUnusedBalance(String unusedBalance) {
		this.unusedBalance = unusedBalance;
	}

}
