package com.jeeyoh.model.funboard;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.user.UserModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class WallFeedRequest implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int userId;
	@JsonProperty
	private List<FunBoardModel> sharedfunBoardItemsList;
	@JsonProperty
	private List<Integer> friends;
	@JsonProperty
	private List<Integer> groups;
	@JsonProperty
	private String tag;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<FunBoardModel> getSharedfunBoardItemsList() {
		return sharedfunBoardItemsList;
	}
	public void setSharedfunBoardItemsList(
			List<FunBoardModel> sharedfunBoardItemsList) {
		this.sharedfunBoardItemsList = sharedfunBoardItemsList;
	}
	/**
	 * @return the friends
	 */
	public List<Integer> getFriends() {
		return friends;
	}
	/**
	 * @param friends the friends to set
	 */
	public void setFriends(List<Integer> friends) {
		this.friends = friends;
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
	
}
