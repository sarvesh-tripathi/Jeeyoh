package com.jeeyoh.model.search;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class TopCommunitySuggestionModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<PageModel> topSportsSuggestions;
	
	@JsonProperty
	private List<PageModel> topRestaurantSuggestions;
	
	@JsonProperty
	private List<PageModel> topMovieSuggestions;
	
	@JsonProperty
	private List<PageModel> topTheaterSuggestions;
	
	@JsonProperty
	private List<PageModel> topConcertSuggestions;
	
	@JsonProperty
	private List<PageModel> topNightLifeSuggestions;

	/**
	 * @return the topSportsSuggestions
	 */
	public List<PageModel> getTopSportsSuggestions() {
		return topSportsSuggestions;
	}

	/**
	 * @param topSportsSuggestions the topSportsSuggestions to set
	 */
	public void setTopSportsSuggestions(List<PageModel> topSportsSuggestions) {
		this.topSportsSuggestions = topSportsSuggestions;
	}

	/**
	 * @return the topRestaurantSuggestions
	 */
	public List<PageModel> getTopRestaurantSuggestions() {
		return topRestaurantSuggestions;
	}

	/**
	 * @param topRestaurantSuggestions the topRestaurantSuggestions to set
	 */
	public void setTopRestaurantSuggestions(
			List<PageModel> topRestaurantSuggestions) {
		this.topRestaurantSuggestions = topRestaurantSuggestions;
	}

	/**
	 * @return the topMovieSuggestions
	 */
	public List<PageModel> getTopMovieSuggestions() {
		return topMovieSuggestions;
	}

	/**
	 * @param topMovieSuggestions the topMovieSuggestions to set
	 */
	public void setTopMovieSuggestions(List<PageModel> topMovieSuggestions) {
		this.topMovieSuggestions = topMovieSuggestions;
	}

	/**
	 * @return the topTheaterSuggestions
	 */
	public List<PageModel> getTopTheaterSuggestions() {
		return topTheaterSuggestions;
	}

	/**
	 * @param topTheaterSuggestions the topTheaterSuggestions to set
	 */
	public void setTopTheaterSuggestions(List<PageModel> topTheaterSuggestions) {
		this.topTheaterSuggestions = topTheaterSuggestions;
	}

	/**
	 * @param topConcertSuggestions the topConcertSuggestions to set
	 */
	public void setTopConcertSuggestions(List<PageModel> topConcertSuggestions) {
		this.topConcertSuggestions = topConcertSuggestions;
	}

	/**
	 * @return the topConcertSuggestions
	 */
	public List<PageModel> getTopConcertSuggestions() {
		return topConcertSuggestions;
	}

	/**
	 * @return the topNightLifeSuggestions
	 */
	public List<PageModel> getTopNightLifeSuggestions() {
		return topNightLifeSuggestions;
	}

	/**
	 * @param topNightLifeSuggestions the topNightLifeSuggestions to set
	 */
	public void setTopNightLifeSuggestions(List<PageModel> topNightLifeSuggestions) {
		this.topNightLifeSuggestions = topNightLifeSuggestions;
	}

}
