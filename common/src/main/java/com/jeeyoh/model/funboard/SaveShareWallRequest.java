package com.jeeyoh.model.funboard;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jeeyoh.model.search.SuggestionModel;


public class SaveShareWallRequest {
	
	
	@JsonProperty
	Integer userId;
	@JsonProperty
	Integer wallFeedId;
	
	@JsonProperty
	List<Integer> friends ;
	@JsonProperty
	List<Integer> groups;
	@JsonProperty
	List<SuggestionModel> items;
	@JsonProperty
	private String sheduleDate;
	
	public String getSheduleDate() {
		return sheduleDate;
	}
	public void setSheduleDate(String sheduleDate) {
		this.sheduleDate = sheduleDate;
	}
	public List<Integer> getFriends() {
		return friends;
	}
	public void setFriends(List<Integer> friends) {
		this.friends = friends;
	}
	public List<Integer> getGroups() {
		return groups;
	}
	public void setGroups(List<Integer> groups) {
		this.groups = groups;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getWallFeedId() {
		return wallFeedId;
	}
	public void setWallFeedId(Integer wallFeedId) {
		this.wallFeedId = wallFeedId;
	}
	public List<SuggestionModel> getItems() {
		return items;
	}
	public void setItems(List<SuggestionModel> items) {
		this.items = items;
	}

}
