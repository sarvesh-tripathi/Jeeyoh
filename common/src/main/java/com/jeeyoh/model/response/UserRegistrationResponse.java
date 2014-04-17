package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.user.UserModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class UserRegistrationResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
    private String confirmationId;
	
	@JsonProperty
	private UserModel user;
	/**
	 * @param confirmationId the confirmationId to set
	 */
	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}
	/**
	 * @return the confirmationId
	 */
	public String getConfirmationId() {
		return confirmationId;
	}
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
