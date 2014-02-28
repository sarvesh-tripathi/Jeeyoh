package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.PageModel;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CategoryResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<PageModel> movie;
	@JsonProperty
	private List<PageModel> food;
	@JsonProperty
	private List<PageModel> sport;
	
	public List<PageModel> getMovie() {
		return movie;
	}
	public void setMovie(List<PageModel> movie) {
		this.movie = movie;
	}
	public List<PageModel> getFood() {
		return food;
	}
	public void setFood(List<PageModel> food) {
		this.food = food;
	}
	public List<PageModel> getSport() {
		return sport;
	}
	public void setSport(List<PageModel> sport) {
		this.sport = sport;
	}
	

	

	

	
	
	

}
