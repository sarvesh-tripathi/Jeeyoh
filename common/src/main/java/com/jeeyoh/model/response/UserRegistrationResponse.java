package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class UserRegistrationResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
    private String confirmationId;
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

}
