package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.EventModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class MatchingEventsResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<EventModel> matchingevents;
	public List<EventModel> getMatchingevents() {
		return matchingevents;
	}
	public void setMatchingevents(List<EventModel> matchingevents) {
		this.matchingevents = matchingevents;
	}

}
