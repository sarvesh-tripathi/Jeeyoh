package com.jeeyoh.model.search;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class TopFriendsSuggestionModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<SuggestionModel> topSportsSuggestions;
	
	@JsonProperty
	private List<SuggestionModel> topRestaurantSuggestions;
	
	@JsonProperty
	private List<SuggestionModel> topMovieSuggestions;
	
	@JsonProperty
	private List<SuggestionModel> topTheaterSuggestions;
	
	@JsonProperty
	private List<SuggestionModel> topConcertSuggestions;
	
	/**
	 * @return the topSportsSuggestions
	 */
	public List<SuggestionModel> getTopSportsSuggestions() {
		return topSportsSuggestions;
	}

	/**
	 * @param topSportsSuggestions the topSportsSuggestions to set
	 */
	public void setTopSportsSuggestions(List<SuggestionModel> topSportsSuggestions) {
		this.topSportsSuggestions = topSportsSuggestions;
	}

	/**
	 * @return the topRestaurantSuggestions
	 */
	public List<SuggestionModel> getTopRestaurantSuggestions() {
		return topRestaurantSuggestions;
	}

	/**
	 * @param topRestaurantSuggestions the topRestaurantSuggestions to set
	 */
	public void setTopRestaurantSuggestions(
			List<SuggestionModel> topRestaurantSuggestions) {
		this.topRestaurantSuggestions = topRestaurantSuggestions;
	}

	/**
	 * @return the topMovieSuggestions
	 */
	public List<SuggestionModel> getTopMovieSuggestions() {
		return topMovieSuggestions;
	}

	/**
	 * @param topMovieSuggestions the topMovieSuggestions to set
	 */
	public void setTopMovieSuggestions(List<SuggestionModel> topMovieSuggestions) {
		this.topMovieSuggestions = topMovieSuggestions;
	}

	/**
	 * @return the topTheaterSuggestions
	 */
	public List<SuggestionModel> getTopTheaterSuggestions() {
		return topTheaterSuggestions;
	}

	/**
	 * @param topTheaterSuggestions the topTheaterSuggestions to set
	 */
	public void setTopTheaterSuggestions(List<SuggestionModel> topTheaterSuggestions) {
		this.topTheaterSuggestions = topTheaterSuggestions;
	}

	/**
	 * @param topConcertSuggestions the topConcertSuggestions to set
	 */
	public void setTopConcertSuggestions(List<SuggestionModel> topConcertSuggestions) {
		this.topConcertSuggestions = topConcertSuggestions;
	}

	/**
	 * @return the topConcertSuggestions
	 */
	public List<SuggestionModel> getTopConcertSuggestions() {
		return topConcertSuggestions;
	}

	

}
