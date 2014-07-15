package com.jeeyoh.model.funboard;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.SuggestionModel;
import com.jeeyoh.model.user.UserModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class WallFeedModel {
	@JsonProperty
	private int wallFeedId;
	@JsonProperty
	private String tag;
	@JsonProperty
    private UserModel creatorUser;
	@JsonProperty
	private List<SuggestionModel> items;
	@JsonProperty
	private List<CommentModel> comments;
	
	public List<CommentModel> getComments() {
		return comments;
	}
	public void setComments(List<CommentModel> comments) {
		this.comments = comments;
	}
	public int getWallFeedId() {
		return wallFeedId;
	}
	public void setWallFeedId(int wallFeedId) {
		this.wallFeedId = wallFeedId;
	}
	public List<SuggestionModel> getItems() {
		return items;
	}
	public void setItems(List<SuggestionModel> items) {
		this.items = items;
	}
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * @return the creatorUser
	 */
	public UserModel getCreatorUser() {
		return creatorUser;
	}
	/**
	 * @param creatorUser the creatorUser to set
	 */
	public void setCreatorUser(UserModel creatorUser) {
		this.creatorUser = creatorUser;
	}
	
	

}
