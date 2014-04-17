package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.JeeyohGroupModel;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class AddGroupResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private JeeyohGroupModel jeeyohGroupDetails;
	
	
	public JeeyohGroupModel getAddGroupDetails() {
		return jeeyohGroupDetails;
	}
	public void setAddGroupDetails(JeeyohGroupModel jeeyohGroupDetails) {
		this.jeeyohGroupDetails = jeeyohGroupDetails;
	}

}
