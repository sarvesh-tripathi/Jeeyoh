package com.jeeyoh.model.responce;

import org.codehaus.jackson.annotate.JsonProperty;

import com.jeeyoh.model.user.UserModel;

public class LoginResponce extends BaseResponce{
	
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
    private UserModel user;

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}
	
}
