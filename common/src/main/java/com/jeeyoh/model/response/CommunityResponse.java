package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.EventModel;
import com.jeeyoh.model.search.PageModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CommunityResponse extends BaseResponse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private PageModel communityDetails;
	@JsonProperty
	private List<EventModel> currentEvents;
	@JsonProperty
	private List<EventModel> upcomingEvents;
	@JsonProperty
	private List<EventModel> pastEvents;
	/**
	 * @return the currentEvents
	 */
	public List<EventModel> getCurrentEvents() {
		return currentEvents;
	}
	/**
	 * @param currentEvents the currentEvents to set
	 */
	public void setCurrentEvents(List<EventModel> currentEvents) {
		this.currentEvents = currentEvents;
	}
	/**
	 * @return the upcomingEvents
	 */
	public List<EventModel> getUpcomingEvents() {
		return upcomingEvents;
	}
	/**
	 * @param upcomingEvents the upcomingEvents to set
	 */
	public void setUpcomingEvents(List<EventModel> upcomingEvents) {
		this.upcomingEvents = upcomingEvents;
	}
	/**
	 * @return the pastEvents
	 */
	public List<EventModel> getPastEvents() {
		return pastEvents;
	}
	/**
	 * @param pastEvents the pastEvents to set
	 */
	public void setPastEvents(List<EventModel> pastEvents) {
		this.pastEvents = pastEvents;
	}
	/**
	 * @param communityDetails the communityDetails to set
	 */
	public void setCommunityDetails(PageModel communityDetails) {
		this.communityDetails = communityDetails;
	}
	/**
	 * @return the communityDetails
	 */
	public PageModel getCommunityDetails() {
		return communityDetails;
	}
	

}
