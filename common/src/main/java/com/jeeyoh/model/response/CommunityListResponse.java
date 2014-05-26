package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.PageModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CommunityListResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<PageModel> communityList;
	@JsonProperty
	private int exactMatchCommunityCount;
	@JsonProperty
	private int totalCount;
	/**
	 * @return the communityList
	 */
	public List<PageModel> getCommunityList() {
		return communityList;
	}
	/**
	 * @param communityList the communityList to set
	 */
	public void setCommunityList(List<PageModel> communityList) {
		this.communityList = communityList;
	}
	/**
	 * @return the exactMatchCommunityCount
	 */
	public int getExactMatchCommunityCount() {
		return exactMatchCommunityCount;
	}
	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}
	/**
	 * @param exactMatchCommunityCount the exactMatchCommunityCount to set
	 */
	public void setExactMatchCommunityCount(int exactMatchCommunityCount) {
		this.exactMatchCommunityCount = exactMatchCommunityCount;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
