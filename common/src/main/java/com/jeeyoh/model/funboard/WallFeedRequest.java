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
	private List<UserModel> sharedWithUserList;
	
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
	public List<UserModel> getSharedWithUserList() {
		return sharedWithUserList;
	}
	public void setSharedWithUserList(List<UserModel> sharedWithUserList) {
		this.sharedWithUserList = sharedWithUserList;
	}
}
