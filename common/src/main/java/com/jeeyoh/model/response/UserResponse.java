package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jeeyoh.model.user.UserModel;

public class UserResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private UserModel user;
	
	public void setUser(UserModel user) {
		this.user = user;
	}
	public UserModel getUser() {
		return user;
	}
}
