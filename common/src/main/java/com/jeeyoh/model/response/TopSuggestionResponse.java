package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.TopCommunitySuggestionModel;
import com.jeeyoh.model.search.TopFriendsSuggestionModel;
import com.jeeyoh.model.search.TopJeeyohSuggestionModel;

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

	
	
}
