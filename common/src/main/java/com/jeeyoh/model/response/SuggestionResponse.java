package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.BusinessModel;
import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.model.search.EventModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class SuggestionResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<BusinessModel> nonDealSuggestions;
	
	@JsonProperty
	private List<DealModel> dealSuggestions;
	
	@JsonProperty
	private List<EventModel> eventSuggestions;

	/**
	 * @return the nonDealSuggestions
	 */
	public List<BusinessModel> getNonDealSuggestions() {
		return nonDealSuggestions;
	}

	/**
	 * @param nonDealSuggestions the nonDealSuggestions to set
	 */
	public void setNonDealSuggestions(List<BusinessModel> nonDealSuggestions) {
		this.nonDealSuggestions = nonDealSuggestions;
	}

	/**
	 * @return the dealSuggestions
	 */
	public List<DealModel> getDealSuggestions() {
		return dealSuggestions;
	}

	/**
	 * @param dealSuggestions the dealSuggestions to set
	 */
	public void setDealSuggestions(List<DealModel> dealSuggestions) {
		this.dealSuggestions = dealSuggestions;
	}

	/**
	 * @return the eventSuggestions
	 */
	public List<EventModel> getEventSuggestions() {
		return eventSuggestions;
	}

	/**
	 * @param eventSuggestions the eventSuggestions to set
	 */
	public void setEventSuggestions(List<EventModel> eventSuggestions) {
		this.eventSuggestions = eventSuggestions;
	}
	
}
