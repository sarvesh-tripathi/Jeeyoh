package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.JeeyohGroupModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class GroupDetailResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private JeeyohGroupModel groupDetail;
	/**
	 * @return the groupDetail
	 */
	public JeeyohGroupModel getGroupDetail() {
		return groupDetail;
	}
	/**
	 * @param groupDetail the groupDetail to set
	 */
	public void setGroupDetail(JeeyohGroupModel groupDetail) {
		this.groupDetail = groupDetail;
	}

}
