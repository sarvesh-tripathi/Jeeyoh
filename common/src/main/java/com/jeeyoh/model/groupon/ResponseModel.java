package com.jeeyoh.model.groupon;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class ResponseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private List<DealsModel> deals;
	
	@JsonProperty
	private PaginationModel pagination;
	
	private ErrorMessage error;

	public List<DealsModel> getDeals() {
		return deals;
	}

	public void setDeals(List<DealsModel> deals) {
		this.deals = deals;
	}

	public PaginationModel getPagination() {
		return pagination;
	}

	public void setPagination(PaginationModel pagination) {
		this.pagination = pagination;
	}

	public ErrorMessage getError() {
		return error;
	}

	public void setError(ErrorMessage error) {
		this.error = error;
	}

}
