package com.jeeyoh.model.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class DirectSuggestionModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String category;
	@JsonProperty
	private Integer suggestionId;
	@JsonProperty
	private String suggestionType;
	@JsonProperty
	private ArrayList<Integer> friendsIdList;
	@JsonProperty
	private List<Integer> groups;
	@JsonProperty
	private String suggestedTime;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getSuggestionId() {
		return suggestionId;
	}
	public void setSuggestionId(Integer suggestionId) {
		this.suggestionId = suggestionId;
	}
	public String getSuggestionType() {
		return suggestionType;
	}
	public void setSuggestionType(String suggestionType) {
		this.suggestionType = suggestionType;
	}
	public ArrayList<Integer> getFriendsIdList() {
		return friendsIdList;
	}
	public void setFriendsIdList(ArrayList<Integer> friendsIdList) {
		this.friendsIdList = friendsIdList;
	}
	/**
	 * @return the groups
	 */
	public List<Integer> getGroups() {
		return groups;
	}
	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<Integer> groups) {
		this.groups = groups;
	}
	/**
	 * @return the suggestedTime
	 */
	public String getSuggestedTime() {
		return suggestedTime;
	}
	/**
	 * @param suggestedTime the suggestedTime to set
	 */
	public void setSuggestedTime(String suggestedTime) {
		this.suggestedTime = suggestedTime;
	}
}
