package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jeeyoh.model.user.UserModel;

public class LoginResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private UserModel user;
	
	/**
	 * @param user the user to set
	 */
	public void setUser(UserModel user) {
		this.user = user;
	}
	/**
	 * @return the user
	 */
	public UserModel getUser() {
		return user;
	}

}
