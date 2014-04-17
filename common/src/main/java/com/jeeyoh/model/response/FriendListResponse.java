package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jeeyoh.model.user.UserModel;

public class FriendListResponse extends BaseResponse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<UserModel> user;
	public List<UserModel> getUser() {
		return user;
	}
	public void setUser(List<UserModel> user) {
		this.user = user;
	}
}
