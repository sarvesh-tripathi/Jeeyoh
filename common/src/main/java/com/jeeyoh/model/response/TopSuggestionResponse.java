package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.TopCommunitySuggestionModel;
import com.jeeyoh.model.search.TopFriendsSuggestionModel;
import com.jeeyoh.model.search.TopJeeyohSuggestionModel;
import com.jeeyoh.model.search.TopMatchListSuggestionModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class TopSuggestionResponse extends BaseResponse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private TopFriendsSuggestionModel topFriendsSuggestions;
	
	@JsonProperty
	private TopJeeyohSuggestionModel topJeeyohSuggestions;
	
	@JsonProperty
	private TopCommunitySuggestionModel topCommunitySuggestions;
	
	@JsonProperty
	private TopMatchListSuggestionModel topMatchListSuggestions;
	
	@JsonProperty
	private MatchingEventsResponse matchingEventsResponse;
	
	@JsonProperty
	private int totalFridayActivity;
	
	@JsonProperty
	private int totalSaturdayActivity;
	
	@JsonProperty
	private int totalSundayActivity;

	/**
	 * @return the topFriendsSuggestions
	 */
	public TopFriendsSuggestionModel getTopFriendsSuggestions() {
		return topFriendsSuggestions;
	}

	/**
	 * @param topFriendsSuggestions the topFriendsSuggestions to set
	 */
	public void setTopFriendsSuggestions(
			TopFriendsSuggestionModel topFriendsSuggestions) {
		this.topFriendsSuggestions = topFriendsSuggestions;
	}

	/**
	 * @return the topJeeyohSuggestions
	 */
	public TopJeeyohSuggestionModel getTopJeeyohSuggestions() {
		return topJeeyohSuggestions;
	}

	/**
	 * @param topJeeyohSuggestions the topJeeyohSuggestions to set
	 */
	public void setTopJeeyohSuggestions(
			TopJeeyohSuggestionModel topJeeyohSuggestions) {
		this.topJeeyohSuggestions = topJeeyohSuggestions;
	}

	/**
	 * @return the topCommunitySuggestions
	 */
	public TopCommunitySuggestionModel getTopCommunitySuggestions() {
		return topCommunitySuggestions;
	}

	/**
	 * @param topCommunitySuggestions the topCommunitySuggestions to set
	 */
	public void setTopCommunitySuggestions(
			TopCommunitySuggestionModel topCommunitySuggestions) {
		this.topCommunitySuggestions = topCommunitySuggestions;
	}

	/**
	 * @return the matchingEventsResponse
	 */
	public MatchingEventsResponse getMatchingEventsResponse() {
		return matchingEventsResponse;
	}

	/**
	 * @param matchingEventsResponse the matchingEventsResponse to set
	 */
	public void setMatchingEventsResponse(
			MatchingEventsResponse matchingEventsResponse) {
		this.matchingEventsResponse = matchingEventsResponse;
	}

	/**
	 * @return the topMatchListSuggestions
	 */
	public TopMatchListSuggestionModel getTopMatchListSuggestions() {
		return topMatchListSuggestions;
	}

	/**
	 * @param topMatchListSuggestions the topMatchListSuggestions to set
	 */
	public void setTopMatchListSuggestions(TopMatchListSuggestionModel topMatchListSuggestions) {
		this.topMatchListSuggestions = topMatchListSuggestions;
	}

	/**
	 * @return the totalFridayActivity
	 */
	public int getTotalFridayActivity() {
		return totalFridayActivity;
	}

	/**
	 * @param totalFridayActivity the totalFridayActivity to set
	 */
	public void setTotalFridayActivity(int totalFridayActivity) {
		this.totalFridayActivity = totalFridayActivity;
	}

	/**
	 * @return the totalSaturdayActivity
	 */
	public int getTotalSaturdayActivity() {
		return totalSaturdayActivity;
	}

	/**
	 * @param totalSaturdayActivity the totalSaturdayActivity to set
	 */
	public void setTotalSaturdayActivity(int totalSaturdayActivity) {
		this.totalSaturdayActivity = totalSaturdayActivity;
	}

	/**
	 * @return the totalSundayActivity
	 */
	public int getTotalSundayActivity() {
		return totalSundayActivity;
	}

	/**
	 * @param totalSundayActivity the totalSundayActivity to set
	 */
	public void setTotalSundayActivity(int totalSundayActivity) {
		this.totalSundayActivity = totalSundayActivity;
	}

	
	
}
