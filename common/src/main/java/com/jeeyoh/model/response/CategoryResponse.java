package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.PageModel;
import com.jeeyoh.model.user.UserModel;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CategoryResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private UserModel user;
	@JsonProperty
	private List<PageModel> movie;
	@JsonProperty
	private List<PageModel> food;
	@JsonProperty
	private List<PageModel> sport;
	@JsonProperty
	private List<PageModel> nightlife;
	
	/**
	 * @return the user
	 */
	public UserModel getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(UserModel user) {
		this.user = user;
	}
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
	/**
	 * @return the nightlife
	 */
	public List<PageModel> getNightlife() {
		return nightlife;
	}
	/**
	 * @param nightlife the nightlife to set
	 */
	public void setNightlife(List<PageModel> nightlife) {
		this.nightlife = nightlife;
	}
}
