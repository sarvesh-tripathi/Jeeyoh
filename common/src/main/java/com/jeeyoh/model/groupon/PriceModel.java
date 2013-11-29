package com.jeeyoh.model.groupon;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class PriceModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private int amount;
	@JsonProperty
	private String currencyCode;	
	@JsonProperty
	private String formattedAmount;
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getFormattedAmount() {
		return formattedAmount;
	}
	public void setFormattedAmount(String formattedAmount) {
		this.formattedAmount = formattedAmount;
	}
	
	

}
