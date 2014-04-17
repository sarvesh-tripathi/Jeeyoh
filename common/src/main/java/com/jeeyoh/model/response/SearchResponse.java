package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.SearchResult;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class SearchResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<SearchResult> searchResult;
	@JsonProperty
	private int exactMatchBusinessCount;
	@JsonProperty
	private int likeMatchBusinessCount;
	@JsonProperty
	private int exactMatchDealCount;
	@JsonProperty
	private int likeMatchDealCount;
	@JsonProperty
	private int exactMatchEventCount;
	@JsonProperty
	private int likeMatchEventCount;
	@JsonProperty
	private int exactMatchCommunityCount;
	@JsonProperty
	private int likeMatchCommunityCount;
	@JsonProperty
	private int totalCount;
	
	

	public List<SearchResult> getSearchResult()
	{
		return searchResult;
	}

	public void setSearchResult(List<SearchResult> searchResult)
	{
		this.searchResult = searchResult;
	}

	/**
	 * @return the exactMatchBusinessCount
	 */
	public int getExactMatchBusinessCount() {
		return exactMatchBusinessCount;
	}

	/**
	 * @param exactMatchBusinessCount the exactMatchBusinessCount to set
	 */
	public void setExactMatchBusinessCount(int exactMatchBusinessCount) {
		this.exactMatchBusinessCount = exactMatchBusinessCount;
	}

	/**
	 * @return the likeMatchBusinessCount
	 */
	public int getLikeMatchBusinessCount() {
		return likeMatchBusinessCount;
	}

	/**
	 * @param likeMatchBusinessCount the likeMatchBusinessCount to set
	 */
	public void setLikeMatchBusinessCount(int likeMatchBusinessCount) {
		this.likeMatchBusinessCount = likeMatchBusinessCount;
	}

	/**
	 * @return the exactMatchDealCount
	 */
	public int getExactMatchDealCount() {
		return exactMatchDealCount;
	}

	/**
	 * @param exactMatchDealCount the exactMatchDealCount to set
	 */
	public void setExactMatchDealCount(int exactMatchDealCount) {
		this.exactMatchDealCount = exactMatchDealCount;
	}

	/**
	 * @return the likeMatchDealCount
	 */
	public int getLikeMatchDealCount() {
		return likeMatchDealCount;
	}

	/**
	 * @param likeMatchDealCount the likeMatchDealCount to set
	 */
	public void setLikeMatchDealCount(int likeMatchDealCount) {
		this.likeMatchDealCount = likeMatchDealCount;
	}

	/**
	 * @return the exactMatchEventCount
	 */
	public int getExactMatchEventCount() {
		return exactMatchEventCount;
	}

	/**
	 * @param exactMatchEventCount the exactMatchEventCount to set
	 */
	public void setExactMatchEventCount(int exactMatchEventCount) {
		this.exactMatchEventCount = exactMatchEventCount;
	}

	/**
	 * @return the likeMatchEventCount
	 */
	public int getLikeMatchEventCount() {
		return likeMatchEventCount;
	}

	/**
	 * @param likeMatchEventCount the likeMatchEventCount to set
	 */
	public void setLikeMatchEventCount(int likeMatchEventCount) {
		this.likeMatchEventCount = likeMatchEventCount;
	}

	/**
	 * @return the exactMatchCommunityCount
	 */
	public int getExactMatchCommunityCount() {
		return exactMatchCommunityCount;
	}

	/**
	 * @param exactMatchCommunityCount the exactMatchCommunityCount to set
	 */
	public void setExactMatchCommunityCount(int exactMatchCommunityCount) {
		this.exactMatchCommunityCount = exactMatchCommunityCount;
	}

	/**
	 * @return the likeMatchCommunityCount
	 */
	public int getLikeMatchCommunityCount() {
		return likeMatchCommunityCount;
	}

	/**
	 * @param likeMatchCommunityCount the likeMatchCommunityCount to set
	 */
	public void setLikeMatchCommunityCount(int likeMatchCommunityCount) {
		this.likeMatchCommunityCount = likeMatchCommunityCount;
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
